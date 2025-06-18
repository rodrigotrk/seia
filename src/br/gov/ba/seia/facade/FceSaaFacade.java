package br.gov.ba.seia.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import br.gov.ba.seia.controller.FceSaaController;
import br.gov.ba.seia.entity.EtaTipoComposicao;
import br.gov.ba.seia.entity.FaixaDiametroAdutora;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.FceCaptacaoSubterranea;
import br.gov.ba.seia.entity.FceCaptacaoSuperficial;
import br.gov.ba.seia.entity.FceOutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.FceSaa;
import br.gov.ba.seia.entity.FceSaaLocalizacaoGeograficaDadosConcedidos;
import br.gov.ba.seia.entity.FceSaaLocalizacaoGeograficaElevatoriaBruta;
import br.gov.ba.seia.entity.FceSaaLocalizacaoGeograficaElevatoriaTratada;
import br.gov.ba.seia.entity.FceSaaLocalizacaoGeograficaEta;
import br.gov.ba.seia.entity.FceSaaLocalizacaoGeograficaReservatorio;
import br.gov.ba.seia.entity.FceSaaTipoComposicao;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.TipoMaterialUtilizado;
import br.gov.ba.seia.entity.TipoReservatorio;
import br.gov.ba.seia.service.FaixaDiametroAdutoraService;
import br.gov.ba.seia.service.FceCaptacaoSubterraneaService;
import br.gov.ba.seia.service.FceOutorgaLocalizacaoGeograficaService;
import br.gov.ba.seia.service.FceSaaService;
import br.gov.ba.seia.service.TipoComposicaoService;
import br.gov.ba.seia.service.TipoMaterialUtilizadoService;
import br.gov.ba.seia.service.TipoReservatorioService;
import br.gov.ba.seia.service.ValidacaoGeoSeiaService;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceSaaFacade {

	@EJB
	private FaixaDiametroAdutoraService faixaDiametroService;
	
	@EJB
	private TipoMaterialUtilizadoService tipoMaterialService;
	
	@EJB
	private TipoComposicaoService tipoComposicaoService;
	
	@EJB
	private TipoReservatorioService tipoReservatorioService;
	
	@EJB
	private ValidacaoGeoSeiaService validacaoGeoSeiaService;
	
	@EJB
	private FceSaaService fceSaaService;
	
	@EJB
	private FceServiceFacade fceServiceFacade;
	
	@EJB
	private FceOutorgaServiceFacade fceOutorgaServiceFacade;
	
	@EJB
	private FceCaptacaoSubterraneaService fceCaptacaoSubterraneaService;
	
	@EJB
	private FceOutorgaLocalizacaoGeograficaService fceOutorgaLocalizacaoGeograficaService;
	
	@EJB
	private FceCaptacaoSuperficialServiceFacade fceCaptacaoSuperficialServiceFacade;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FaixaDiametroAdutora> carregarFaixasDiametro(Integer tipoFase01, Integer tipoFase02, Integer tipoFase03 ) throws Exception{
		
		return faixaDiametroService.carregarFaixasDiametro(tipoFase01, tipoFase02, tipoFase03);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoMaterialUtilizado> carregarTipoMaterialUtilizado() throws Exception{
		return tipoMaterialService.listaTipoMaterialUtilizado();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceSaaTipoComposicao> carregarTipoComposicoes() throws Exception {
		
		return tipoComposicaoService.listaTipoComposicao();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<EtaTipoComposicao> listaTipoComposicaoByIdeLocalizacaoEta(FceSaaLocalizacaoGeograficaEta localizacaoEta) throws Exception {
		return tipoComposicaoService.listaTipoComposicaoByIdeLocalizacaoEta(localizacaoEta);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirTipoComposicaoByIdeLocalizacaoEta(FceSaaLocalizacaoGeograficaEta localizacaoEta) throws Exception {
		tipoComposicaoService.excluirTipoComposicaoByIdeLocalizacaoEta(localizacaoEta);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoReservatorio> carregarTipoReservatorio() throws Exception {
		
		return tipoReservatorioService.listaTipoReservatorio();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public String buscarGeometriaShape(Integer ideLocalizacaoGeografica) throws Exception {
		return validacaoGeoSeiaService.buscarGeometriaShape(ideLocalizacaoGeografica);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceSaa(FceSaa fceSaa) throws Exception{
		fceSaaService.salvarFceSaa(fceSaa);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceSaaLocalizacaoBruta(FceSaaLocalizacaoGeograficaElevatoriaBruta localizacaoBruta) throws Exception {
		fceSaaService.salvarFceSaaLocalizacaoBruta(localizacaoBruta);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceSaaLocalizacaoDadosConcedidos(FceSaaLocalizacaoGeograficaDadosConcedidos localizacao) throws Exception {
		fceSaaService.salvarFceSaaLocalizacaoDadosConcedidos(localizacao);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFceSaaLocalizacaoBruta(FceSaaLocalizacaoGeograficaElevatoriaBruta localizacaoBruta) throws Exception {
		fceSaaService.excluirFceSaaLocalizacaoBruta(localizacaoBruta);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFceSaaLocalizacaoReservatorio(FceSaaLocalizacaoGeograficaReservatorio localizacaoReservatorio) throws Exception {
		fceSaaService.excluirFceSaaLocalizacaoReservatorio(localizacaoReservatorio);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFceSaaLocalizacaoTratada(FceSaaLocalizacaoGeograficaElevatoriaTratada localizacaoTratada) throws Exception {
		fceSaaService.excluirFceSaaLocalizacaoTratada(localizacaoTratada);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFceSaaLocalizacaoEta(FceSaaLocalizacaoGeograficaEta localizacaoEta) throws Exception {
		fceSaaService.excluirFceSaaLocalizacaoEta(localizacaoEta);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceSaaLocalizacaoEta(FceSaaLocalizacaoGeograficaEta localizacaoEta) throws Exception {
		fceSaaService.salvarFceSaaLocalizacaoEta(localizacaoEta);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceSaaLocalizacaoTratada(FceSaaLocalizacaoGeograficaElevatoriaTratada localizacaoTratada) throws Exception {
		fceSaaService.salvarFceSaaLocalizacaoTratada(localizacaoTratada);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceSaaLocalizacaoReservatorio(FceSaaLocalizacaoGeograficaReservatorio localizacaoReservatorio) throws Exception {
		fceSaaService.salvarFceSaaLocalizacaoReservatorio(localizacaoReservatorio);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void finalizar(FceSaaController ctrl) throws Exception {
		ctrl.prepararParaFinalizar();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FceSaa buscarFceSaaByIdeFce(Fce fce) throws Exception{
		return fceSaaService.buscarFceSaaByIdeFce(fce);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceSaaLocalizacaoGeograficaElevatoriaBruta> listarLocalizacaoBrutaByIdeFceSaa(FceSaa fceSaa) throws Exception {
		return fceSaaService.listarLocalizacaoBrutaByIdeFceSaa(fceSaa);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceSaaLocalizacaoGeograficaElevatoriaTratada> buscarLocalizacaoTratadaByIdeFceSaa(FceSaa fceSaa) throws Exception {
		return fceSaaService.buscarLocalizacaoTratadaByIdeFceSaa(fceSaa);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceSaaLocalizacaoGeograficaEta> listarLocalizacaoEtaByIdeFceSaa(FceSaa fceSaa) throws Exception {
		return fceSaaService.listarLocalizacaoEtaByIdeFceSaa(fceSaa);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FceSaaLocalizacaoGeograficaDadosConcedidos buscarLocalizacaoDadosConcedidosByIdeFceSaaAndTipoCaptacao(FceSaa fceSaa,Integer ideTipoCaptacao) throws Exception {
		return fceSaaService.buscarLocalizacaoDadosConcedidosByIdeFceSaaAndTipoCaptacao(fceSaa,ideTipoCaptacao);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceSaaLocalizacaoGeograficaReservatorio> listarLocalizacaoReservatorioByIdeFceSaa(FceSaa fceSaa) throws Exception {
		return fceSaaService.listarLocalizacaoReservatorioByIdeFceSaa(fceSaa);
	}
	
	public void salvarOuAtualizarFce(Fce fce) throws Exception{
		fceServiceFacade.salvarFce(fce);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public String getBacia(LocalizacaoGeografica localizacaoGeografica){
		return fceOutorgaServiceFacade.getBacia(localizacaoGeografica);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<OutorgaLocalizacaoGeografica> carregarCaptacoesSubterraneas(Requerimento requerimento) throws Exception {
		
		List<OutorgaLocalizacaoGeografica> listaOutorgaLocalizacaoGeografica = new ArrayList<OutorgaLocalizacaoGeografica>();
		
		List<FceCaptacaoSubterranea> listaFceCaptacaoSubterranea = fceCaptacaoSubterraneaService.listarFceCaptacaoSubterraneaTecnicoByIdeRequerimento(requerimento);
		
		for (FceCaptacaoSubterranea fceCaptacaoSubterranea : listaFceCaptacaoSubterranea) {
			OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica = new OutorgaLocalizacaoGeografica(fceCaptacaoSubterranea.getIdeFceOutorgaLocalizacaoGeografica().getIdeLocalizacaoGeografica());
			listaOutorgaLocalizacaoGeografica.add(outorgaLocalizacaoGeografica);
		}
		
		return listaOutorgaLocalizacaoGeografica;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<OutorgaLocalizacaoGeografica> carregarCaptacoesSuperficiais(Requerimento requerimento) throws Exception {
		List<OutorgaLocalizacaoGeografica> listaOutorgaLocalizacaoGeografica = new ArrayList<OutorgaLocalizacaoGeografica>();
		
		List<FceOutorgaLocalizacaoGeografica> listaFceOutorgaLocalizacaoGeografica = fceOutorgaLocalizacaoGeograficaService.listarFceOutorgaLocGeoAnaliseTecnicaByIdeRequerimento(requerimento);
		
		for (FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeografica : listaFceOutorgaLocalizacaoGeografica) {
			FceCaptacaoSuperficial fceCaptacaoSuperficial = fceCaptacaoSuperficialServiceFacade.buscarFceCaptacaoSuperficialBy(fceOutorgaLocalizacaoGeografica);
			if(!Util.isNullOuVazio(fceCaptacaoSuperficial)){
				OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica = new OutorgaLocalizacaoGeografica(fceOutorgaLocalizacaoGeografica.getIdeLocalizacaoGeografica());
				listaOutorgaLocalizacaoGeografica.add(outorgaLocalizacaoGeografica);
			}
		}
		
		return listaOutorgaLocalizacaoGeografica;
	}
	
}
