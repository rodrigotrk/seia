package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Level;

import br.gov.ba.seia.entity.ArquivoProcesso;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.FceAquiculturaLicenca;
import br.gov.ba.seia.entity.FceLocalizacaoGeografica;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.ProcessoAto;
import br.gov.ba.seia.entity.ProcessoAtoConcedido;
import br.gov.ba.seia.entity.TipoAreaConcedida;
import br.gov.ba.seia.enumerator.MotivoNotificacaoEnum;
import br.gov.ba.seia.enumerator.TipoAreaConcedidaEnum;
import br.gov.ba.seia.facade.FceLicenciamentoAquiculturaServiceFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;
import br.gov.ba.seia.util.fce.FceGeoBahiaUtil;

/**
 * Controller responsável pela <i>Aba Dados Gerais</i> do <b>FCE - Licenciamento para Aquicultura</b>.
 * <pre><b> #6934 </b></pre>
 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
 * @since 27/05/2015
 */
@Named("fceLicenciamentoAquiculturaDadosGeraisController")
@ViewScoped
public class FceLicenciamentoAquiculturaDadosGeraisController{

	@Inject
	private FceLicenciamentoAquiculturaController licenciamentoAquiculturaController;

	@EJB
	private FceLicenciamentoAquiculturaServiceFacade serviceFacade;

	private LocalizacaoGeografica localizacaoGeografica;
	
	private boolean indConcedidoEmpreendimento;
	private List<ArquivoProcesso> listaPoligonalDaNotificacaoEmpreendimento;
	private List<ProcessoAtoConcedido> listProcessoAtoConcedidoEmpreendimento;
	private ArquivoProcesso apConcedidoEmpreendimento;
	
	private boolean indConcedidoGalpao;
	private List<ArquivoProcesso> listaPoligonalDaNotificacaoGalpao;
	private List<ProcessoAtoConcedido> listProcessoAtoConcedidoGalpao;
	private ArquivoProcesso apConcedidoGalpao;

	/**
	 * Poliongal do Galpão de Insumos
	 */
	private FceLocalizacaoGeografica fceLocalizacaoGeografica;

	@PostConstruct
	public void init(){
		if(!isEmpreendimentoComShape()) armazenarLocGeoDoEmpreendimento();
		verificarEdicao();
		if(!isEdicao()) inicarFceLocalizacaoGeografica();
		if(getFce().isFceTecnico()) carregarPoligonaisDasNotificacoes();
		
	}

	/**
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 09/06/2015
	 */
	private void verificarEdicao() {
		try {
			if(!Util.isNullOuVazio(getFce())){
				carregarFceLocalizacaoGeografica();
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " as informações da Aba Dados Gerais.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	private void carregarFceLocalizacaoGeografica() throws Exception {
		fceLocalizacaoGeografica = serviceFacade.buscarFceLocalizacaoGeograficaByFce(getFce());
	}

	/**
	 * Método que vai inicializar a Poliongal do Galpão de Insumos.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 10/07/2015
	 */
	private void inicarFceLocalizacaoGeografica() {
		fceLocalizacaoGeografica = new FceLocalizacaoGeografica(licenciamentoAquiculturaController.getFce());
	}
	
	private Boolean isEdicao() {
		return !Util.isNull(fceLocalizacaoGeografica);
	}
	
	public String getVisualizarPoligonalEmpreendimento(){
		return FceGeoBahiaUtil.criarURLToVisualizarShapeInFce(licenciamentoAquiculturaController.getEmpreendimento());
	}

	public String getVisualizarPoligonalGalpao(){
		return FceGeoBahiaUtil.criarURLToVisualizarShapeInFce(fceLocalizacaoGeografica.getIdeLocalizacaoGeografica());
	}
	
	public String getVisualizarPoligonalNotificacao(LocalizacaoGeografica locGeo){
		return FceGeoBahiaUtil.criarURLToVisualizarShapeInFce(locGeo);
	}
	
	private void armazenarLocGeoDoEmpreendimento(){
		try {
			if(licenciamentoAquiculturaController.isEmpreendimentoCarregado()){
				localizacaoGeografica = licenciamentoAquiculturaController.getEmpreendimento().getIdeLocalizacaoGeografica().clone();
			}
		} catch (CloneNotSupportedException e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " as informações do Empreendimento.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/*
	 * INI flags
	 */
	public boolean isEmpreendimentoComShape() {
		return licenciamentoAquiculturaController.isEmpreendimentoCarregado() && licenciamentoAquiculturaController.isTheGeomPersistidoEmEmpreendimento();
	}

	private boolean isFceLocalizacaoGeograficaComLocGeo(){
		return isEdicao() && !Util.isNullOuVazio(fceLocalizacaoGeografica.getIdeLocalizacaoGeografica());
	}

	public boolean isGalpaoComShape() {
		return isFceLocalizacaoGeograficaComLocGeo()
				&& licenciamentoAquiculturaController.isTheGeomPersistidoEmGalpao(fceLocalizacaoGeografica);
	}

	/*
	 * FIM flags
	 */
	public void excluirGalpao(){
		try {
			serviceFacade.excluirFceLocalizacaoGeografica(fceLocalizacaoGeografica);
			indConcedidoGalpao = false;
			licenciamentoAquiculturaController.exibirMensagem005();
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_excluir") + " a poligonal do galpão de insumos.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	protected void finalizarAba() throws Exception{
		try {
			if(licenciamentoAquiculturaController.isFceTecnico()) preparaParaSalvarProcessoAtoConcedidoEmpreendimento();
			if(licenciamentoAquiculturaController.isFceTecnico()) preparaParaSalvarProcessoAtoConcedidoGalpao();
			licenciamentoAquiculturaController.salvarFceAquiculturaLicenca();
			serviceFacade.finalizarAbaDadosGerais(this);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_salvar") + " o FCE - Aquicultura");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public void limpar(){
		fceLocalizacaoGeografica = null;
		localizacaoGeografica = null;
	}

	protected boolean validarAba(){
		
		boolean valido = true;
		
		if(!isEmpreendimentoComShape()){
			licenciamentoAquiculturaController.getEmpreendimento().setIdeLocalizacaoGeografica(localizacaoGeografica);
			JsfUtil.addErrorMessage("A "+ Util.getString("fce_lic_aqui_poligonal_externa_area_empreendimento") + " " + Util.getString("msg_generica_preenchimento_obrigatorio"));
			valido = false;
		}
		
		if(licenciamentoAquiculturaController.isFceTecnico() && !validaIndConcedidoEmpreendimento()) {
			valido = false;
			JsfUtil.addErrorMessage("Pelo menos uma poligonal do empreendimento deve ser concedida.");
		}
		
		if(!isGalpaoComShape()){
			JsfUtil.addErrorMessage("A "+ Util.getString("fce_lic_aqui_poligonal_galpao_insumos") + " " + Util.getString("msg_generica_preenchimento_obrigatorio"));
			valido = false;
		}

		if(licenciamentoAquiculturaController.isFceTecnico() && !validaIndConcedidoGalpao()) {
			valido = false;
			JsfUtil.addErrorMessage("Pelo menos uma poligonal do galpão deve ser concedida.");
		}
		
		return valido;
	}

	public void avancarAba(){
		try {
			if(validarAba()){
				finalizarAba();
				licenciamentoAquiculturaController.avancarAba();
			}
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_salvar") + " a Aba Dados Gerais.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}

	}

	public FceAquiculturaLicenca getFceAquiculturaLicenca(){
		return licenciamentoAquiculturaController.getFceAquiculturaLicenca();
	}

	public Empreendimento getEmpreendimento(){
		return licenciamentoAquiculturaController.getEmpreendimento();
	}

	public boolean isDesabilitado(){
		return licenciamentoAquiculturaController.isDesabilitarTudo();
	}
	
	public void prepararAnaliseTecnica(){
		try {
			fceLocalizacaoGeografica = serviceFacade.buscarFceLocalizacaoGeograficaByFce(getFce());
			
			LocalizacaoGeografica locGeoOriginal = fceLocalizacaoGeografica.getIdeLocalizacaoGeografica(); 
			LocalizacaoGeografica locGeoDuplicada = licenciamentoAquiculturaController.duplicarLocalizacaoGeografica(locGeoOriginal);
			
			locGeoDuplicada.setIdeClassificacaoSecao(locGeoOriginal.getIdeClassificacaoSecao());
			locGeoDuplicada.setIdeSistemaCoordenada(locGeoOriginal.getIdeSistemaCoordenada());
			
			fceLocalizacaoGeografica.setIdeLocalizacaoGeografica(locGeoDuplicada);
			fceLocalizacaoGeografica.setIdeFce(null);
			fceLocalizacaoGeografica.setIdeFceLocalizacaoGeograficaPK(null);
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}

	public void duplicarFce() {
		try {
			finalizarAba();
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_duplicar") + " o FCE - Licenciamento para Aquicultura.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	/********************************
	 * 								*
	 * XXX: Processo Ato Concedido	*
	 * 								*
	 ********************************/
	
	private void carregarPoligonaisDasNotificacoes() {
		listaPoligonalDaNotificacaoEmpreendimento = new ArrayList<ArquivoProcesso>();
		listaPoligonalDaNotificacaoGalpao = new ArrayList<ArquivoProcesso>();

		List<ArquivoProcesso> list = (List<ArquivoProcesso>) serviceFacade.listarArquivoProcessoPorProcesso(
				getFceAquiculturaLicenca().getIdeFce().getIdeAnaliseTecnica().getIdeProcesso());
		
		for (ArquivoProcesso ap : list) {
			if(!Util.isNullOuVazio(ap.getMotivoNotificacao())) {
				
				if(ap.getMotivoNotificacao().getIdeMotivoNotificacao().equals(MotivoNotificacaoEnum.SHAPE_LICENCIAMENTO_AQUICULTURA.getId())) {
					listaPoligonalDaNotificacaoEmpreendimento.add(ap);
				} else if(ap.getMotivoNotificacao().getIdeMotivoNotificacao().equals(MotivoNotificacaoEnum.SHAPE_GALPAO_ARMAZENAMENTO_AQUICULTURA.getId())) {
					listaPoligonalDaNotificacaoGalpao.add(ap);
				}
			}
		}
	}
	
	/**
	 * EMPREENDIMENTO
	 */
	
	public void changeConcedidoEmpreendimento(ValueChangeEvent eve) {
		if((Boolean) eve.getNewValue()) {
			indConcedidoEmpreendimento = true;
			for (ArquivoProcesso ap : listaPoligonalDaNotificacaoEmpreendimento) ap.setIndConcedido(false);
		} else {
			indConcedidoEmpreendimento = false;
		}
	}

	public void changeConcedidoNotificacoesEmpreendimento(AjaxBehaviorEvent e) {
		
		apConcedidoEmpreendimento = (ArquivoProcesso) e.getComponent().getAttributes().get("attributeAP");
		
		if(apConcedidoEmpreendimento != null && apConcedidoEmpreendimento.isIndConcedido()) {
			indConcedidoEmpreendimento = false;
			
			for (ArquivoProcesso ap : listaPoligonalDaNotificacaoEmpreendimento) {
				if(!ap.getIdeArquivoProcesso().equals(apConcedidoEmpreendimento.getIdeArquivoProcesso())) {
					ap.setIndConcedido(false);
				}
			}
		}
	}
	
	public boolean isRenderedListaPoligonalDaNotificacaoEmpreendimento() {
		return !Util.isNullOuVazio(listaPoligonalDaNotificacaoEmpreendimento);
	}

	private boolean validaIndConcedidoEmpreendimento() {
		
		if(indConcedidoEmpreendimento) return true;
		
		for (ArquivoProcesso ap : listaPoligonalDaNotificacaoEmpreendimento) {
			if(ap.isIndConcedido()) return true;
		}
		
		return false;
	}

	private void preparaParaSalvarProcessoAtoConcedidoEmpreendimento() {
		LocalizacaoGeografica locGeo = null;
		listProcessoAtoConcedidoEmpreendimento = new ArrayList<ProcessoAtoConcedido>();
		
		if(indConcedidoEmpreendimento) {
			locGeo = getEmpreendimento().getIdeLocalizacaoGeografica();
		} else {
			for (ArquivoProcesso ap : listaPoligonalDaNotificacaoEmpreendimento) {
				if(ap.isIndConcedido()) locGeo = ap.getIdeLocalizacaoGeografica();
			}
		}
		
		for (ProcessoAto pa : serviceFacade.listarProcessoAtoPorFce(licenciamentoAquiculturaController.getFce())) {
			listProcessoAtoConcedidoEmpreendimento.add(
					new ProcessoAtoConcedido(null, locGeo, pa, null, null, new TipoAreaConcedida(TipoAreaConcedidaEnum.EMPREENDIMENTO)));
		}
	}
	
	/**
	 * GALPÃO
	 */
	
	public void changeConcedidoGalpao(ValueChangeEvent eve) {
		if((Boolean) eve.getNewValue()) {
			indConcedidoGalpao = true;
			for (ArquivoProcesso ap : listaPoligonalDaNotificacaoGalpao) ap.setIndConcedido(false);
		} else {
			indConcedidoGalpao= false;
		}
	}
	
	public void changeConcedidoNotificacoesGalpao(AjaxBehaviorEvent e) {
		
		apConcedidoGalpao= (ArquivoProcesso) e.getComponent().getAttributes().get("attributeAP");
		
		if(apConcedidoGalpao!= null && apConcedidoGalpao.isIndConcedido()) {
			indConcedidoGalpao= false;
			
			for (ArquivoProcesso ap : listaPoligonalDaNotificacaoGalpao) {
				if(!ap.getIdeArquivoProcesso().equals(apConcedidoGalpao.getIdeArquivoProcesso())) {
					ap.setIndConcedido(false);
				}
			}
		}
	}
	
	public boolean isRenderedListaPoligonalDaNotificacaoGalpao() {
		return !Util.isNullOuVazio(listaPoligonalDaNotificacaoGalpao);
	}

	private boolean validaIndConcedidoGalpao() {
		
		if(indConcedidoGalpao) return true;
		
		for (ArquivoProcesso ap : listaPoligonalDaNotificacaoGalpao) {
			if(ap.isIndConcedido()) return true;
		}
		
		return false;
	}

	private void preparaParaSalvarProcessoAtoConcedidoGalpao() {
		LocalizacaoGeografica locGeo = null;
		listProcessoAtoConcedidoGalpao = new ArrayList<ProcessoAtoConcedido>();
		
		if(indConcedidoGalpao) {
			locGeo = fceLocalizacaoGeografica.getIdeLocalizacaoGeografica();
		} else {
			for (ArquivoProcesso ap : listaPoligonalDaNotificacaoGalpao) {
				if(ap.isIndConcedido()) locGeo = ap.getIdeLocalizacaoGeografica();
			}
		}
		
		for (ProcessoAto pa : serviceFacade.listarProcessoAtoPorFce(licenciamentoAquiculturaController.getFce())) {
			listProcessoAtoConcedidoGalpao.add(
					new ProcessoAtoConcedido(null, locGeo, pa, null, null, new TipoAreaConcedida(TipoAreaConcedidaEnum.GALPAO_DE_INSUMOS)));
		}
	}
	
	/************************
	 * 						*
	 * XXX: Gets and Sets	*
	 * 						*
	 ************************/
	
	private Fce getFce() {
		return licenciamentoAquiculturaController.getFce();
	}

	public FceLocalizacaoGeografica getFceLocalizacaoGeografica() {
		return fceLocalizacaoGeografica;
	}

	public void setFceLocalizacaoGeografica(FceLocalizacaoGeografica fceLocalizacaoGeografica) {
		this.fceLocalizacaoGeografica = fceLocalizacaoGeografica;
	}

	public boolean isIndConcedidoEmpreendimento() {
		return indConcedidoEmpreendimento;
	}

	public void setIndConcedidoEmpreendimento(boolean indConcedidoEmpreendimento) {
		this.indConcedidoEmpreendimento = indConcedidoEmpreendimento;
	}

	public LocalizacaoGeografica getLocalizacaoGeografica() {
		return localizacaoGeografica;
	}

	public void setLocalizacaoGeografica(LocalizacaoGeografica localizacaoGeografica) {
		this.localizacaoGeografica = localizacaoGeografica;
	}

	public FceLicenciamentoAquiculturaController getLicenciamentoAquiculturaController() {
		return licenciamentoAquiculturaController;
	}

	public void setLicenciamentoAquiculturaController(FceLicenciamentoAquiculturaController licenciamentoAquiculturaController) {
		this.licenciamentoAquiculturaController = licenciamentoAquiculturaController;
	}

	public FceLicenciamentoAquiculturaServiceFacade getServiceFacade() {
		return serviceFacade;
	}

	public void setServiceFacade(FceLicenciamentoAquiculturaServiceFacade serviceFacade) {
		this.serviceFacade = serviceFacade;
	}

	public List<ArquivoProcesso> getListaPoligonalDaNotificacaoEmpreendimento() {
		return listaPoligonalDaNotificacaoEmpreendimento;
	}

	public void setListaPoligonalDaNotificacaoEmpreendimento(List<ArquivoProcesso> listaPoligonalDaNotificacaoEmpreendimento) {
		this.listaPoligonalDaNotificacaoEmpreendimento = listaPoligonalDaNotificacaoEmpreendimento;
	}

	public List<ProcessoAtoConcedido> getListProcessoAtoConcedidoEmpreendimento() {
		return listProcessoAtoConcedidoEmpreendimento;
	}

	public void setListProcessoAtoConcedidoEmpreendimento(List<ProcessoAtoConcedido> listProcessoAtoConcedidoEmpreendimento) {
		this.listProcessoAtoConcedidoEmpreendimento = listProcessoAtoConcedidoEmpreendimento;
	}

	public ArquivoProcesso getApConcedidoEmpreendimento() {
		return apConcedidoEmpreendimento;
	}

	public void setApConcedidoEmpreendimento(ArquivoProcesso apConcedidoEmpreendimento) {
		this.apConcedidoEmpreendimento = apConcedidoEmpreendimento;
	}

	public boolean isIndConcedidoGalpao() {
		return indConcedidoGalpao;
	}

	public void setIndConcedidoGalpao(boolean indConcedidoGalpao) {
		this.indConcedidoGalpao = indConcedidoGalpao;
	}

	public List<ArquivoProcesso> getListaPoligonalDaNotificacaoGalpao() {
		return listaPoligonalDaNotificacaoGalpao;
	}

	public void setListaPoligonalDaNotificacaoGalpao(List<ArquivoProcesso> listaPoligonalDaNotificacaoGalpao) {
		this.listaPoligonalDaNotificacaoGalpao = listaPoligonalDaNotificacaoGalpao;
	}

	public List<ProcessoAtoConcedido> getListProcessoAtoConcedidoGalpao() {
		return listProcessoAtoConcedidoGalpao;
	}

	public void setListProcessoAtoConcedidoGalpao(List<ProcessoAtoConcedido> listProcessoAtoConcedidoGalpao) {
		this.listProcessoAtoConcedidoGalpao = listProcessoAtoConcedidoGalpao;
	}

	public ArquivoProcesso getApConcedidoGalpao() {
		return apConcedidoGalpao;
	}

	public void setApConcedidoGalpao(ArquivoProcesso apConcedidoGalpao) {
		this.apConcedidoGalpao = apConcedidoGalpao;
	}
}