package br.gov.ba.seia.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.dto.DetalhamentoDaeDTO;
import br.gov.ba.seia.dto.GerarDaeDTO;
import br.gov.ba.seia.dto.ParcelaDaeDTO;
import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.Dae;
import br.gov.ba.seia.entity.HistSituacaoDae;
import br.gov.ba.seia.entity.MemoriaCalculoDae;
import br.gov.ba.seia.entity.MemoriaCalculoDaeParcela;
import br.gov.ba.seia.entity.Parametro;
import br.gov.ba.seia.entity.ParametroCalculo;
import br.gov.ba.seia.entity.RequerimentoPessoa;
import br.gov.ba.seia.entity.SefazCodigoReceita;
import br.gov.ba.seia.entity.SituacaoDae;
import br.gov.ba.seia.entity.TaxaIndiceCobranca;
import br.gov.ba.seia.entity.TipoPessoaRequerimento;
import br.gov.ba.seia.enumerator.AtoAmbientalEnum;
import br.gov.ba.seia.enumerator.IndiceCobrancaEnum;
import br.gov.ba.seia.enumerator.MesEnum;
import br.gov.ba.seia.enumerator.ParametroEnum;
import br.gov.ba.seia.enumerator.SefazCodigoReceitaEnum;
import br.gov.ba.seia.enumerator.SituacaoDaeEnum;
import br.gov.ba.seia.facade.EnderecoFacade;
import br.gov.ba.seia.middleware.bcb.service.BancoCentralService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.DaeUtil;
import br.gov.ba.seia.util.DataUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class GerarDaeCrfService {

	@Inject
	private IDAO<MemoriaCalculoDae> memoriaCalculoDaeDAO;
	
	@Inject
	private IDAO<MemoriaCalculoDaeParcela> memoriaCalculoDaeParcelaDAO;
	
	@Inject
	private IDAO<Dae> daeDAO;
	
	@EJB
	private CumprimentoReposicaoFlorestalService cumprimentoReposicaoFlorestalService;
	
	@EJB
	private ParametroCalculoService parametroCalculoService;
	
	@EJB
	private SefazCodigoReceitaService sefazCodigoReceitaService;
	
	@EJB
	private EnderecoFacade enderecoFacade;
	
	@EJB
	private RequerimentoPessoaService requerimentoPessoaService;
	
	@EJB
	private SituacaoDaeService situacaoDaeService;
	
	@EJB
	private HistSituacaoDaeService histSituacaoDaeService;
	
	@Inject
	private DaeUtil daeUtil;
	
	@EJB
	private TaxaIndiceCobrancaService taxaIndiceCobrancaService;
	
	@EJB
	private MemoriaCalculoDaeParcelaService memoriaCalculoDaeParcelaService;
	
	@EJB
	private MemoriaCalculoDaeService memoriaCalculoDaeService;
	
	@EJB 
	private CalendarioService calendarioService;
	
	@EJB 
	private BancoCentralService bancoCentralService;
	
	@EJB 
	private ParametroService parametroService;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void gerarParcelas(GerarDaeDTO gerarDaeDTO) throws Exception {
		if (Util.isNullOuVazio(gerarDaeDTO.getDaeVencido())) {
			if (!gerarDaeDTO.isIndParcela()) {
				gerarDaeDTO.setQtdeParcelas(1);
			}
			
			MemoriaCalculoDae memoriaCalculoDae = new MemoriaCalculoDae();
			
			memoriaCalculoDae.setIdeCumprimentoReposicaoFlorestal(cumprimentoReposicaoFlorestalService.obterCumprimentoReposicaoFlorestalPorRequerimento(gerarDaeDTO.getRequerimento()));
			memoriaCalculoDae.setIdeParametroCalculo(obterParametroCalculoCRF());
			memoriaCalculoDae.setQtdParcelas(gerarDaeDTO.getQtdeParcelas());
			memoriaCalculoDae.setIndParcelado(gerarDaeDTO.isIndParcela());
			memoriaCalculoDae.setValorTotal(obterValorTotal(gerarDaeDTO));
			
			memoriaCalculoDaeDAO.salvarOuAtualizar(memoriaCalculoDae);
			
			salvarMemoriaCalculoDaeParcela(memoriaCalculoDae, gerarDaeDTO);
		} else {
			gerarDaeVencido(gerarDaeDTO);
		}
	}	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void gerarDaeVencido(GerarDaeDTO gerarDaeDTO) throws Exception {
		Double valorDae = calcularValorDaeVencido(gerarDaeDTO);
		Double valorAcrescimo = 0.00;
		
		MemoriaCalculoDae memoriaCalculoDae = memoriaCalculoDaeService.obterMemoriaCalculoDaePorRequerimento(gerarDaeDTO.getRequerimento());
		
		MemoriaCalculoDaeParcela memoriaCalculoDaeParcela = new MemoriaCalculoDaeParcela();
		memoriaCalculoDaeParcela.setIdeMemoriaCalculoDae(memoriaCalculoDae);
		memoriaCalculoDaeParcela.setNumParcelaReferencia(1);
		memoriaCalculoDaeParcela.setValor(valorDae);
		memoriaCalculoDaeParcela.setIndExcluido(false);
		memoriaCalculoDaeParcela.setIndParcelaUnica(true);
		
		ParcelaDaeDTO parcelasDaeDTO = new ParcelaDaeDTO();
		parcelasDaeDTO.setValorAcrescimo(valorAcrescimo);
		parcelasDaeDTO.setValorDae(valorDae);
		parcelasDaeDTO.setNumParcela(1);
		
		Dae dae = gerarDae(memoriaCalculoDaeParcela, parcelasDaeDTO, SituacaoDaeEnum.EM_ABERTO);
		parcelasDaeDTO.setDae(dae);
		parcelasDaeDTO.setSituacaoDae(situacaoDaeService.buscarSituacaoByIde(SituacaoDaeEnum.EM_ABERTO.getId()));
		parcelasDaeDTO.setUrlDae(dae.getUrlDae());
		parcelasDaeDTO.setVencimento(dae.getDtVencimento());
		memoriaCalculoDaeParcela.setIdeDae(dae);
		
		memoriaCalculoDaeParcelaDAO.salvarOuAtualizar(memoriaCalculoDaeParcela);
		
		parcelasDaeDTO.setIdeMemoriaCalculoDaeParcela(memoriaCalculoDaeParcela.getIdeMemoriaCalculoDaeParcela());
		gerarDaeDTO.getParcelaUnica().add(parcelasDaeDTO);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void gerarParcelaDae(AtoAmbiental atoAmbiental,	ParcelaDaeDTO parcelaDaeDTO,GerarDaeDTO gerarDaeDTO) throws Exception {
		
		Date vencimento = null;
		Date mesAnteriorPagamento = null;
		
		if(!Util.isNullOuVazio(gerarDaeDTO.getDaeVencido())) {
			vencimento = gerarDaeDTO.getDaeVencido().getDataDaeVencido();
			mesAnteriorPagamento = gerarDaeDTO.getDaeVencido().getDataDaeAnteriorPago();
		}
		
		MemoriaCalculoDaeParcela memoriaCalculoDaeParcela = new MemoriaCalculoDaeParcela();
		if(!Util.isNullOuVazio(vencimento) && Util.validarDuasDatasReposicao(vencimento, new Date())) {
			Double valorDae = calcularMultaDaeVencidoSemIdeDae(gerarDaeDTO, vencimento, mesAnteriorPagamento);
			MemoriaCalculoDae memoriaCalculoDae = memoriaCalculoDaeService.obterMemoriaCalculoDaePorRequerimento(gerarDaeDTO.getRequerimento());
			
			memoriaCalculoDaeParcela.setIdeMemoriaCalculoDae(memoriaCalculoDae);
			memoriaCalculoDaeParcela.setNumParcelaReferencia(1);
			memoriaCalculoDaeParcela.setValor(valorDae);
			memoriaCalculoDaeParcela.setIndExcluido(false);
			memoriaCalculoDaeParcela.setIndParcelaUnica(true);
			
			ParcelaDaeDTO parcelasDaeDTO = new ParcelaDaeDTO();
			parcelasDaeDTO.setValorAcrescimo(0.00);
			parcelasDaeDTO.setValorDae(valorDae);
			parcelasDaeDTO.setNumParcela(1);
			
			Dae dae = gerarDae(memoriaCalculoDaeParcela, parcelasDaeDTO, SituacaoDaeEnum.EM_ABERTO);
			parcelasDaeDTO.setDae(dae);
			parcelasDaeDTO.setSituacaoDae(situacaoDaeService.buscarSituacaoByIde(SituacaoDaeEnum.EM_ABERTO.getId()));
			parcelasDaeDTO.setUrlDae(dae.getUrlDae());
			parcelasDaeDTO.setVencimento(dae.getDtVencimento());
			memoriaCalculoDaeParcela.setIdeDae(dae);
			
			memoriaCalculoDaeParcelaDAO.salvarOuAtualizar(memoriaCalculoDaeParcela);
			
			parcelasDaeDTO.setIdeMemoriaCalculoDaeParcela(memoriaCalculoDaeParcela.getIdeMemoriaCalculoDaeParcela());
			gerarDaeDTO.getParcelaUnica().add(parcelasDaeDTO);
		}else {
			memoriaCalculoDaeParcela = memoriaCalculoDaeParcelaService.obterMemoriaCalculoDaeParcela(parcelaDaeDTO.getIdeMemoriaCalculoDaeParcela());
			
			Double valorDae = calcularValorParcela(memoriaCalculoDaeParcela.getIdeMemoriaCalculoDae(), true);
			
			memoriaCalculoDaeParcela.setValor(valorDae);
			memoriaCalculoDaeParcela.setIndExcluido(false);
			memoriaCalculoDaeParcela.setIndParcelaUnica(false);
			
			parcelaDaeDTO.setValorDae(valorDae);
			parcelaDaeDTO.setValorAcrescimo(0.0);
			
			Dae dae = gerarDae(memoriaCalculoDaeParcela, parcelaDaeDTO, SituacaoDaeEnum.EM_ABERTO);
			
			parcelaDaeDTO.setUrlDae(dae.getUrlDae());
			parcelaDaeDTO.setVencimento(dae.getDtVencimento());
			memoriaCalculoDaeParcela.setIdeDae(dae);
			
			memoriaCalculoDaeParcelaDAO.salvarOuAtualizar(memoriaCalculoDaeParcela);
		}

		
	}

	private Double calcularMultaDaeVencidoSemIdeDae(GerarDaeDTO gerarDaeDTO, Date vencimento, Date mesAnteriorPagamento)
			throws URISyntaxException, IOException {
		MemoriaCalculoDaeParcela memoriaCalculoDaeParcela = memoriaCalculoDaeParcelaService.obterMemoriaCalculoDaeParcela(gerarDaeDTO.getDaeVencido().getIdeMemoriaCalculoDaeParcela());
		Double calculo = montarValoresDaeVencido(gerarDaeDTO, vencimento, mesAnteriorPagamento,
				memoriaCalculoDaeParcela);
		return new BigDecimal(calculo).setScale(2, RoundingMode.HALF_UP).doubleValue();
	}	
	
	public Double calcularValorDaeVencido(GerarDaeDTO gerarDaeDTO) throws Exception {
		Date vencimento = gerarDaeDTO.getDaeVencido().getDae().getDtVencimento();
		Date mesAnterioPagamento = DataUtil.somarMes(new Date(), -1);
		 
		MemoriaCalculoDaeParcela memoriaCalculoDaeParcela = memoriaCalculoDaeParcelaService.obterMemoriaCalculoDaeParcela(gerarDaeDTO.getDaeVencido().getIdeMemoriaCalculoDaeParcela());
		
		Double calculo = montarValoresDaeVencido(gerarDaeDTO, vencimento, mesAnterioPagamento,
				memoriaCalculoDaeParcela);
		
		return new BigDecimal(calculo).setScale(2, RoundingMode.HALF_UP).doubleValue();
	}

	
	private Double montarValoresDaeVencido(GerarDaeDTO gerarDaeDTO, Date vencimento, Date mesAnteriorPagamento,
			MemoriaCalculoDaeParcela memoriaCalculoDaeParcela) throws URISyntaxException, IOException {
		Parametro parametro = parametroService.obterPorEnum(ParametroEnum.MULTA_ATRASO_CRF);
		
		Double valorSelic = bancoCentralService.obterTotalValorSelicMesPorPeriodo(vencimento, mesAnteriorPagamento);
		Double valorTotal = memoriaCalculoDaeParcela.getIdeMemoriaCalculoDae().getValorTotal();
		Double valorRestante = (valorTotal - obterValorPago(gerarDaeDTO.getParcelaDaeDTO()));
		
		Double calculo = 0.0;
		
		if (vencimento.getMonth() != (new Date()).getMonth()) {
			calculo = (valorRestante * (Double.valueOf(parametro.getDscValor()) / 100)) + (valorRestante * (valorSelic / 100)) + valorRestante;
			calculo = calculo + (valorRestante * 0.01);
		} else {
			calculo = (valorRestante * (Double.valueOf(parametro.getDscValor()) / 100)) + valorRestante;
		}
		return calculo;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void excluirMemoriaCalculoDaeParcelas(List<ParcelaDaeDTO> listaParcelaDaeDTO){
		for (ParcelaDaeDTO parcelaDaeDTO : listaParcelaDaeDTO) {
			if (Util.isNullOuVazio(parcelaDaeDTO.getDae())) {
				MemoriaCalculoDaeParcela memoriaCalculoDaeParcela = memoriaCalculoDaeParcelaService.obterMemoriaCalculoDaeParcela(parcelaDaeDTO.getIdeMemoriaCalculoDaeParcela());
				memoriaCalculoDaeParcela.setIndExcluido(true);
				memoriaCalculoDaeParcela.setDtcExclusao(new Date());
				memoriaCalculoDaeParcelaDAO.salvarOuAtualizar(memoriaCalculoDaeParcela);
			}
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarMemoriaCalculoDaeParcela(MemoriaCalculoDae memoriaCalculoDae, GerarDaeDTO gerarDaeDTO) throws Exception {
		
		if (Util.isNullOuVazio(gerarDaeDTO.getParcelaDaeDTO())) {
			gerarDaeDTO.setParcelaDaeDTO(new ArrayList<ParcelaDaeDTO>());
		}
		
		if (Util.isNullOuVazio(gerarDaeDTO.getParcelaUnica())) {
			gerarDaeDTO.setParcelaUnica(new ArrayList<ParcelaDaeDTO>());
		}
		
		for (int i = 1; i <= memoriaCalculoDae.getQtdParcelas(); i++) {
			Boolean primeiraParcela = (i == 1);
			Double valorDae = calcularValorParcela(memoriaCalculoDae, primeiraParcela);
			
			MemoriaCalculoDaeParcela memoriaCalculoDaeParcela = new MemoriaCalculoDaeParcela();
			memoriaCalculoDaeParcela.setIdeMemoriaCalculoDae(memoriaCalculoDae);
			memoriaCalculoDaeParcela.setNumParcelaReferencia(i);
			memoriaCalculoDaeParcela.setValor(valorDae);
			memoriaCalculoDaeParcela.setIndExcluido(false);
			memoriaCalculoDaeParcela.setIndParcelaUnica(false);
			
			if (memoriaCalculoDae.getQtdParcelas() == 1) {
				memoriaCalculoDaeParcela.setIndParcelaUnica(true);
			}
			
			ParcelaDaeDTO parcelasDaeDTO = new ParcelaDaeDTO();
			
			parcelasDaeDTO.setValorAcrescimo(0.0);
			parcelasDaeDTO.setValorDae(valorDae);
			parcelasDaeDTO.setNumParcela(i);
			
			//Gera somente o primeiro DAE 
			if (primeiraParcela) {
				Dae dae = gerarDae(memoriaCalculoDaeParcela, parcelasDaeDTO, SituacaoDaeEnum.EM_ABERTO);
				parcelasDaeDTO.setDae(dae);
				parcelasDaeDTO.setSituacaoDae(situacaoDaeService.buscarSituacaoByIde(SituacaoDaeEnum.EM_ABERTO.getId()));
				parcelasDaeDTO.setUrlDae(dae.getUrlDae());
				parcelasDaeDTO.setVencimento(dae.getDtVencimento());
				memoriaCalculoDaeParcela.setIdeDae(dae);
			} 
			
			memoriaCalculoDaeParcelaDAO.salvarOuAtualizar(memoriaCalculoDaeParcela);
			
			parcelasDaeDTO.setIdeMemoriaCalculoDaeParcela(memoriaCalculoDaeParcela.getIdeMemoriaCalculoDaeParcela());
			
			if (memoriaCalculoDae.getQtdParcelas() == 1) {
				gerarDaeDTO.getParcelaUnica().add(parcelasDaeDTO);
			} else {
				gerarDaeDTO.getParcelaDaeDTO().add(parcelasDaeDTO);
			}
		}
	}
	
	public TaxaIndiceCobranca obterIGPMMesAnterior(Date date) throws Exception {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		  
		cal.add(Calendar.MONTH, -1);
		
		Integer mesAnterior = (cal.get(Calendar.MONTH) + 1);
		Integer anoAnterior = cal.get(Calendar.YEAR);
		
		return taxaIndiceCobrancaService.obterTaxaporIndiceCobrancaEMes(IndiceCobrancaEnum.IGPM.getId(), mesAnterior, anoAnterior);
	}
	
	private Double calcularValorParcela(MemoriaCalculoDae memoriaCalculoDae, Boolean calcularIGPM) throws Exception {
		if (memoriaCalculoDae.getQtdParcelas() > 1) {
			TaxaIndiceCobranca taxaIndiceCobranca = obterIGPMMesAnterior(new Date());

			if (!Util.isNullOuVazio(taxaIndiceCobranca) && calcularIGPM) {
				Double valorParcela = (memoriaCalculoDae.getValorTotal() / memoriaCalculoDae.getQtdParcelas());
				return BigDecimal.valueOf(valorParcela + ((valorParcela * taxaIndiceCobranca.getValor()) / 100)).setScale(2, RoundingMode.HALF_UP).doubleValue();
			}
			return BigDecimal.valueOf((memoriaCalculoDae.getValorTotal() / memoriaCalculoDae.getQtdParcelas())).setScale(2, RoundingMode.HALF_UP).doubleValue();
		} 
		
		return memoriaCalculoDae.getValorTotal();
	}
	
	private Double valorAcrescimo(MemoriaCalculoDae memoriaCalculoDae, Boolean calcularIGPM) throws Exception {
		return BigDecimal.valueOf((calcularValorParcela(memoriaCalculoDae, calcularIGPM) - (memoriaCalculoDae.getValorTotal() / memoriaCalculoDae.getQtdParcelas()))).setScale(2, RoundingMode.CEILING).doubleValue();
	}
	
	private Double obterValorTotal(GerarDaeDTO gerarDaeDTO) {
		Double valor = 0.0;
		for (DetalhamentoDaeDTO detalhamentoDaeDTO : gerarDaeDTO.getListaDetalhamentoDaeDTO()) {
			return detalhamentoDaeDTO.getValor();
		}
		
		return valor;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private ParametroCalculo obterParametroCalculoCRF() throws Exception {
		Collection<ParametroCalculo> listaParametroCalculo = parametroCalculoService.listarParametrosDAE(AtoAmbientalEnum.CRF.getId());
		return listaParametroCalculo.iterator().next();
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private Dae gerarDae(MemoriaCalculoDaeParcela memoriaCalculoDaeParcela, ParcelaDaeDTO parcelaDaeDTO, SituacaoDaeEnum situacaoDaeEnum) throws Exception {
		
		MemoriaCalculoDaeParcela memoriaCalculoDaeParcelaAnterior = memoriaCalculoDaeParcelaService.obterMemoriaCalculoDaeParcelaPorParcela(memoriaCalculoDaeParcela.getIdeMemoriaCalculoDae().getIdeMemoriaCalculoDae(), (memoriaCalculoDaeParcela.getNumParcelaReferencia() - 1));
		
		Date vencimento = null;
		parcelaDaeDTO.setDae(new Dae());
		
		if (Util.isNullOuVazio(memoriaCalculoDaeParcelaAnterior)) {
			vencimento = new Date();
		} else {
			vencimento = memoriaCalculoDaeParcelaAnterior.getIdeDae().getDtVencimento();
		}
		
		vencimento = DataUtil.somarMes(vencimento, 1);
		parcelaDaeDTO.getDae().setDtcMaxPagamento(vencimento);
		
		if (!calendarioService.isDiaUtil(vencimento)) {
			parcelaDaeDTO.getDae().setDtcMaxPagamento(calendarioService.proximoDiaUtilApartir(vencimento));
		}
		
		SituacaoDae situacaoDae = situacaoDaeService.buscarSituacaoByIde(situacaoDaeEnum.getId());
		
		SefazCodigoReceita cerhCodigoReceita = sefazCodigoReceitaService.buscarSefazCodigo(SefazCodigoReceitaEnum.REPO_FLORESTAL.getId());
		parcelaDaeDTO.getDae().setCerhCodigoReceita(cerhCodigoReceita);
		parcelaDaeDTO.getDae().setDtVencimento(vencimento);
		parcelaDaeDTO.getDae().setDtEmissao(new Date());
		//Integer mesReferencia = 0;
		/*
		 * if(parcelaDaeDTO.getDae().getDtcMaxPagamento().getMonth()+1 !=
		 * MesEnum.getMesEnum(Calendar.getInstance()).getValue()){ mesReferencia =
		 * parcelaDaeDTO.getDae().getDtcMaxPagamento().getMonth()+1; }else {
		 * mesReferencia = MesEnum.getMesEnum(Calendar.getInstance()).getValue(); }
		 */
		parcelaDaeDTO.getDae().setNumMesReferencia(MesEnum.getMesEnum(Calendar.getInstance()).getValue());
		parcelaDaeDTO.getDae().setNumAnoReferencia(DataUtil.obterAno());
		//Os valores são fixos porque estão sendo feitos no mapeamento na tabela memoria_calculo_dae_parcela
		parcelaDaeDTO.getDae().setNumParcelaReferencia(1); 
		parcelaDaeDTO.getDae().setNumTotalParcelaReferencia(1);
		parcelaDaeDTO.setSituacaoDae(situacaoDae);

		RequerimentoPessoa rp = requerimentoPessoaService.obterPorRequerimentoETipoPessoa(memoriaCalculoDaeParcela.getIdeMemoriaCalculoDae().getIdeCumprimentoReposicaoFlorestal().getRequerimento(), new TipoPessoaRequerimento(1));
	
		parcelaDaeDTO.setPessoa(rp.getPessoa());
		parcelaDaeDTO.setEnderecoPessoa(enderecoFacade.filtrarEnderecoByPessoa(rp.getPessoa()));
		
		
		daeUtil.gerarDaeSefaz(parcelaDaeDTO);
		
		daeDAO.salvarOuAtualizar(parcelaDaeDTO.getDae());
		
		HistSituacaoDae histSituacaoDae = new HistSituacaoDae();
		histSituacaoDae.setIdeDae(parcelaDaeDTO.getDae());
		histSituacaoDae.setIdeSituacaoDae(situacaoDae);
		histSituacaoDae.setIdeUsuario(ContextoUtil.getContexto().getUsuarioLogado()); 
		histSituacaoDae.setDtAlteracao(new Date());
		//Persistir atualização no histórico de situação do DAE
		histSituacaoDaeService.salvarHistSituacaoDae(histSituacaoDae);
		
		return parcelaDaeDTO.getDae();
	}

	public Double obterValorPago(List<ParcelaDaeDTO> listaParcelaDaeDTO) {
		Double valor = 0.0;
		
		if (!Util.isNullOuVazio(listaParcelaDaeDTO)) {
			for (ParcelaDaeDTO parcelaDaeDTO : listaParcelaDaeDTO) {
				if(!Util.isNullOuVazio(parcelaDaeDTO.getSituacaoDae())) {
					if (SituacaoDaeEnum.PAGO.getId().equals(parcelaDaeDTO.getSituacaoDae().getIdeSitucaoDae())) {
						valor += parcelaDaeDTO.getValorDae();
					}
				}
			}
		}
		return valor;
	}
	
}
