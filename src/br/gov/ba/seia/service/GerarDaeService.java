package br.gov.ba.seia.service;

import java.util.ArrayList;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Level;
import org.hibernate.*;

import br.gov.ba.seia.controller.GerarDaeController;
import br.gov.ba.seia.controller.ProcuradorPessoaFisicaController;
import br.gov.ba.seia.dao.DAOFactory;
import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.dto.DetalhamentoDaeDTO;
import br.gov.ba.seia.dto.GerarDaeDTO;
import br.gov.ba.seia.dto.ParcelaDaeDTO;
import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.HistSituacaoDae;
import br.gov.ba.seia.entity.MemoriaCalculoDae;
import br.gov.ba.seia.entity.MemoriaCalculoDaeParcela;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.RequerimentoPessoa;
import br.gov.ba.seia.entity.TaxaIndiceCobranca;
import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.enumerator.IndiceCobrancaEnum;
import br.gov.ba.seia.enumerator.SituacaoDaeEnum;
import br.gov.ba.seia.factory.DetalhamentoDaeFactory;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.SessaoUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class GerarDaeService {
	
	@Inject
	IDAO<Usuario> usuarioDao;

	@EJB
	private AtoAmbientalService atoAmbientalService;
	
	@EJB
	private DetalhamentoDaeFactory detalhamentoDaeFactory;
	
	@EJB
	private MemoriaCalculoDaeService memoriaCalculoDaeService;
	
	@EJB
	private HistSituacaoDaeService histSituacaoDaeService;
	
	@EJB
	private TaxaIndiceCobrancaService taxaIndiceCobrancaService;
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void carregarFormulario(GerarDaeDTO gerarDaeDTO) throws Exception {
		Collection<AtoAmbiental> listaAtoAmbiental = atoAmbientalService.filtrarListaAtoAmbientalPorEnquadramentoRequerimento(gerarDaeDTO.getRequerimento().getIdeRequerimento());
		
		if (Util.isNullOuVazio(gerarDaeDTO.getListaDetalhamentoDaeDTO())) {
			gerarDaeDTO.setListaDetalhamentoDaeDTO(new ArrayList<DetalhamentoDaeDTO>());
		}
		
		for (AtoAmbiental atoAmbiental : listaAtoAmbiental) {
			DetalhamentoDaeDTO detalhamentoDaeDTO = detalhamentoDaeFactory.gerar(atoAmbiental, gerarDaeDTO.getRequerimento());
			gerarDaeDTO.getListaDetalhamentoDaeDTO().add(detalhamentoDaeDTO);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public TaxaIndiceCobranca obterIGPMMesAnterior(Date date) throws Exception {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		  
		cal.add(Calendar.MONTH, -1);
		
		Integer mesAnterior = (cal.get(Calendar.MONTH) + 1);
		Integer anoAnterior = cal.get(Calendar.YEAR);
		
		return taxaIndiceCobrancaService.obterTaxaporIndiceCobrancaEMes(IndiceCobrancaEnum.IGPM.getId(), mesAnterior, anoAnterior);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void gerarParcelas(GerarDaeDTO gerarDaeDTO) {
		try {
			detalhamentoDaeFactory.gerarParcelas(gerarDaeDTO);
		} catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	private void gerarParcelasDTO(GerarDaeDTO gerarDaeDTO, Collection<MemoriaCalculoDaeParcela> parcelas) throws Exception {
		List<ParcelaDaeDTO> parcelasDae = new ArrayList<ParcelaDaeDTO>();
		List<ParcelaDaeDTO> parcelaUnica = new ArrayList<ParcelaDaeDTO>();
		boolean existeParcelaUnica= false;
		
		
		if(!Util.isNullOuVazio(parcelas) && parcelas.size() > 1) {
			for (MemoriaCalculoDaeParcela memoriaCalculoDaeParcela : parcelas) {
				if (memoriaCalculoDaeParcela.getIndParcelaUnica()) {
					existeParcelaUnica = true;
					GerarDaeController gerarDaeController = (GerarDaeController) SessaoUtil.recuperarManagedBean("#{gerarDaeController}", GerarDaeController.class);
					gerarDaeController.setExibirMsgNovoDae(true);
					break;
				}
			}
		}
		
		Requerimento req = new Requerimento();
		req = (gerarDaeDTO.getRequerimento());
		
		if(!existeParcelaUnica) {
			for (MemoriaCalculoDaeParcela memoriaCalculoDaeParcela : parcelas) {
				
				ParcelaDaeDTO pDTO = montarParcelaDaeDTO(memoriaCalculoDaeParcela, req);
				
				if (memoriaCalculoDaeParcela.getIndParcelaUnica()) {
					parcelaUnica.add(pDTO);
				} else {
					parcelasDae.add(pDTO);
				}
			}	
			GerarDaeController gerarDaeController = (GerarDaeController) SessaoUtil.recuperarManagedBean("#{gerarDaeController}", GerarDaeController.class);
			gerarDaeController.setExibirMsgNovoDae(false);
		}else {
			for (MemoriaCalculoDaeParcela memoriaCalculoDaeParcela : parcelas) {
				ParcelaDaeDTO pDTO = montarParcelaDaeDTO(memoriaCalculoDaeParcela, req);

				if (!Util.isNullOuVazio(memoriaCalculoDaeParcela.getIdeDae())) {
					parcelasDae.add(pDTO);
				}
			}
		}
		
		gerarDaeDTO.setParcelaUnica(parcelaUnica);
		gerarDaeDTO.setParcelaDaeDTO(parcelasDae);
	}

	private ParcelaDaeDTO montarParcelaDaeDTO(MemoriaCalculoDaeParcela memoriaCalculoDaeParcela, Requerimento req) {
		ParcelaDaeDTO pDTO = new ParcelaDaeDTO();
		
		pDTO.setNumParcela(memoriaCalculoDaeParcela.getNumParcelaReferencia());
		pDTO.setValorDae(memoriaCalculoDaeParcela.getValor());
		pDTO.setDae(memoriaCalculoDaeParcela.getIdeDae());
		pDTO.setIdeMemoriaCalculoDaeParcela(memoriaCalculoDaeParcela.getIdeMemoriaCalculoDaeParcela());
		pDTO.setIndParcelaUnica(memoriaCalculoDaeParcela.getIndParcelaUnica());
		
		if (!Util.isNullOuVazio(memoriaCalculoDaeParcela.getIdeDae())) {
			pDTO.setUrlDae(memoriaCalculoDaeParcela.getIdeDae().getUrlDae());
			pDTO.setVencimento(memoriaCalculoDaeParcela.getIdeDae().getDtVencimento());
			
			HistSituacaoDae histSituacaoDae = histSituacaoDaeService.obterHistSituacaoDae(memoriaCalculoDaeParcela.getIdeDae(), req);
			pDTO.setSituacaoDae(histSituacaoDae.getIdeSituacaoDae());
		}
		return pDTO;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void carregarDados(GerarDaeDTO gerarDaeDTO) throws Exception {
		MemoriaCalculoDae memoriaCalculoDae = memoriaCalculoDaeService.obterMemoriaCalculoDaePorRequerimento(gerarDaeDTO.getRequerimento());
		
		if (!Util.isNullOuVazio(memoriaCalculoDae)) {
			gerarDaeDTO.setIdeMemoriaCalculoDae(memoriaCalculoDae.getIdeMemoriaCalculoDae());
			gerarDaeDTO.setQtdeParcelas(memoriaCalculoDae.getQtdParcelas());
			gerarDaeDTO.setIndParcela(memoriaCalculoDae.getIndParcelado());
			
			gerarParcelasDTO(gerarDaeDTO, memoriaCalculoDae.getMemoriaCalculoDaeCollection());
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void gerarParcelaDae(GerarDaeDTO gerarDaeDTO, ParcelaDaeDTO parcelaDaeDTO) throws Exception {
		AtoAmbiental atoAmbiental = null;
		
		for (DetalhamentoDaeDTO detalhamentoDaeDTO : gerarDaeDTO.getListaDetalhamentoDaeDTO()) {
			atoAmbiental = detalhamentoDaeDTO.getAtoAmbiental();
			break;
		}
		detalhamentoDaeFactory.gerarParcelaDae(atoAmbiental, parcelaDaeDTO, gerarDaeDTO);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ParcelaDaeDTO verificarParcelaDaeVencido(List<ParcelaDaeDTO> listaParcelaDaeDTO)  {
		ParcelaDaeDTO vencido = null;
		if (!Util.isNullOuVazio(listaParcelaDaeDTO)) {
			for (ParcelaDaeDTO parcelaDae : listaParcelaDaeDTO) {
				if (!Util.isNullOuVazio(parcelaDae.getDae().getHistSituacaoDae())) {
					vencido = null;
					for(int i = 0; i < parcelaDae.getDae().getHistSituacaoDae().size(); i++) {
						if(SituacaoDaeEnum.VENCIDO.getId() == parcelaDae.getDae().getHistSituacaoDae().get(i).getIdeSituacaoDae().getIdeSitucaoDae()) {
							vencido = parcelaDae;
							break;
						}
					}
				}
			}
		}
		return vencido;
	}

}
