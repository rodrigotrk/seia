package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.entity.DestinoResiduo;
import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.FcePesquisaMineral;
import br.gov.ba.seia.entity.FcePesquisaMineralDestinoResiduo;
import br.gov.ba.seia.entity.FcePesquisaMineralMetodoRecuperacao;
import br.gov.ba.seia.entity.FcePesquisaMineralOrigemEnergia;
import br.gov.ba.seia.entity.FcePesquisaMineralOrigemEnergiaPK;
import br.gov.ba.seia.entity.FcePesquisaMineralProspeccao;
import br.gov.ba.seia.entity.FcePesquisaMineralSubstanciaMineralTipologia;
import br.gov.ba.seia.entity.FcePesquisaMineralTipoResiduo;
import br.gov.ba.seia.entity.FceProspeccao;
import br.gov.ba.seia.entity.Geofisica;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.MetodoProspeccao;
import br.gov.ba.seia.entity.MetodoRecuperacaoIntervencao;
import br.gov.ba.seia.entity.OutorgaMineracao;
import br.gov.ba.seia.entity.ProcessoDnpm;
import br.gov.ba.seia.entity.SubstanciaMineral;
import br.gov.ba.seia.entity.TipoOrigemEnergia;
import br.gov.ba.seia.entity.TipoResiduoGerado;
import br.gov.ba.seia.enumerator.ClassificacaoSecaoEnum;
import br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum;
import br.gov.ba.seia.enumerator.MetodoProspeccaoEnum;
import br.gov.ba.seia.enumerator.MetodoRecuperacaoIntervencaoEnum;
import br.gov.ba.seia.facade.FceAutorizacaoMineracaoFacade;
import br.gov.ba.seia.facade.FceMineracaoFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

/**
 * Controller responsável pelo <b>FCE - Autorização Mineral</b>
 *
 * @author alexandre.queiroz
 * @since 09/06/2016
 * @see <a href="http://10.105.12.26/redmine/issues/7703">#7703</a>
 */

@Named("fceAutorizacaoMineracaoController")
@ViewScoped
public class FceAutorizacaoMineracaoController extends BaseFceMineracaoController {

	@EJB
	private FceAutorizacaoMineracaoFacade facade;

	/* Geral */
	private static DocumentoObrigatorio DOC_PESQUISA_MINERACAO = new DocumentoObrigatorio(DocumentoObrigatorioEnum.FCE_AUTORIZACAO_MINERACAO.getId());
	private FcePesquisaMineral idefcePesquisaMineral;

	private static final String dialogProcessoDnpm = "dialogProcessoDnpm";
	private static final String dialogProspeccao = "dialogProspeccao";

	private static final String abaDadosGerais = "abaDadosGerais";
	private static final String abaAtividadesPesquisa = "abaAtividadesPesquisa";
	private static final String abaAspectosAmbientais = "abaAspectosAmbientais";
	
	private static final Integer ABA_DADOS_GERAIS = 0;
	private static final Integer ABA_ATIVIDADES_PESQUISA = 1;
	private static final Integer ABA_ASPECTOS_AMBIENTAIS = 2;
	
	/* Dados Gerais */
	private FcePesquisaMineralSubstanciaMineralTipologia fcePesquisaMineralSubstanciaMineral;
	private List<FcePesquisaMineralSubstanciaMineralTipologia> listaFcePesquisaMineralSubstanciaMineral;
	private List<FcePesquisaMineralOrigemEnergia> listaFcePesquisaMineralOrigemEnergia;
	private List<ProcessoDnpm> listaProcessoDnmp;

	/* Atividades Pesquisa */
	private Geofisica geofisica;
	private MetodoProspeccao metodoProspeccao;
	private FceProspeccao fceProspeccao;
	private FcePesquisaMineralProspeccao fcePesquisaMineralProspeccao;
	private List<FcePesquisaMineralMetodoRecuperacao> listaFcePesquisaMineralMetodoRecuperacao;

	/*Aspectos ambietais*/
	private List<Geofisica> listaGeofisca;
	private List<MetodoProspeccao> listaMetodoProspeccao;
	private List<FceProspeccao> listafceProspeccao;
	private List<FcePesquisaMineralProspeccao> listaFcePesquisaMineralProspeccao;
	private String tituloDialogLocGeo;
	private List<FcePesquisaMineralProspeccao> listaFcePesquisaMineralProspeccaoExclusao;
	
	@Override
	public void init() {

		super.limpar();			
		
		verificarEdicao();
		
		super.init();
		
		if (super.isFceSalvo()) {
			carregarFcePesquisaMineral();
		} else {
			super.iniciarFce(DOC_PESQUISA_MINERACAO);
			idefcePesquisaMineral = new FcePesquisaMineral(super.fce);
			idefcePesquisaMineral.setIndSupressao(necessidadeDeSupressao());
			
			iniciarSuprimentoAgua();
		}
	}
	
	private void iniciarSuprimentoAgua() {
		try {
			if(!facade.isEnquadramentoDeOutorgaComAA(super.requerimento)){
				montarListaOutorgaMineracao();				
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}

	/** 
	 * Abas
	 **/
	@Override
	public void avancarAba() {
		if (validarAba()) {
			activeTab++;
		}
	}

	@Override
	public void carregarAba() {
		carregarAbaDadosGerais();
		carregarAbaAtividadesPesquisa();
		carregarAbaAspectosAmbientais();
	}

	/**
	 * Carregamentos
	 * */
	@Override
	protected void carregarAbaDadosGerais() {
		try {
			super.montarArvoreTipologiaMineracao(null);
			
			listaProcessoDnmp = new ArrayList<ProcessoDnpm>();
			listaFcePesquisaMineralOrigemEnergia = new ArrayList<FcePesquisaMineralOrigemEnergia>();
			listaFcePesquisaMineralSubstanciaMineral = new ArrayList<FcePesquisaMineralSubstanciaMineralTipologia>();
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar")	+ " o FCE - Licenciamento para Mineração.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	private void carregarAbaAtividadesPesquisa() {

		try {
			listafceProspeccao = new ArrayList<FceProspeccao>();
			listaFcePesquisaMineralProspeccao = new ArrayList<FcePesquisaMineralProspeccao>();
			listaMetodoProspeccao = facade.listarMetodosProspeccao();
			super.carregarListaMetodoRecuperacaoIntervencao(!super.isFceTecnico());
			super.carregarListaTipoOrigemEnergia();
			listaGeofisca = facade.listarGeofisica();
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar")	+ " o FCE - Licenciamento para Mineração.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@Override
	protected void carregarAbaAspectosAmbientais() {
		try {
			super.carregarListaTipoResiduoGerado();
			super.carregarListaDestinoResiduo();
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar")	+ " o FCE - Licenciamento para Mineração.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	private void carregarFcePesquisaMineral() {
		try {
			idefcePesquisaMineral = facade.getFcePesquisaMineralBy(super.fce);
			if(!Util.isNullOuVazio(idefcePesquisaMineral)){
				carregarlistaFcePesquisaMineralSubstanciaMineral();
				carregarListaFcePesquisaMineralOrigemEnergia();
				carregarListaProcessoDnpm();
			}
			carregarMetodosProspeccaoMineral();
			carregarFcePesquisaMineralLocalizacaoGeografica();
			carregarAreaIntervencao();
			carregarGeofiscas();
			carregarClasseResiduoGerado();
			carregarTipoResiduo();
			super.carregarListaOutorgaMineracao(idefcePesquisaMineral);

		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar")+ " o FCE - Licenciamento para Mineração.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	private void carregarListaProcessoDnpm() throws Exception {
		listaProcessoDnmp = facade.listarProcessoDNPMby(idefcePesquisaMineral);
	}

	public void montarListaOutorgaMineracao(){
		try {
			super.montarListaOutorgaMineracao(idefcePesquisaMineral);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	/**
	 * Atividades Pesquisa
	 */
	private void carregarMetodosProspeccaoMineral(){
		try {
			listaFcePesquisaMineralProspeccao = facade.listarFcePesquisaMineralProspeccaoBy(idefcePesquisaMineral);
			for (FcePesquisaMineralProspeccao fcePesquisaMineralProspeccao : listaFcePesquisaMineralProspeccao) {
				for(MetodoProspeccao metodoProspeccao: listaMetodoProspeccao ){
					if(fcePesquisaMineralProspeccao.getIdeMetodoProspeccao().getIdeMetodoProspeccao().equals(metodoProspeccao.getIdeMetodoProspeccao())){
						metodoProspeccao.setChecked(true);
					}
				}
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}

	public void iniciarProcessoDnpm(){
		super.processoDnpm = new ProcessoDnpm(idefcePesquisaMineral);
	}

	@Override
	protected void incluirProcessoDnpm() {
		super.processoDnpm = new ProcessoDnpm(idefcePesquisaMineral);
		Html.esconder(dialogProcessoDnpm);
	}
	
	private void carregarFcePesquisaMineralLocalizacaoGeografica(){
		try{
			for(FcePesquisaMineralProspeccao fcePesquisaMineralProspeccao: listaFcePesquisaMineralProspeccao ){
				fcePesquisaMineralProspeccao.setListaFceProspeccao(facade.listarFceProspeccaoBy(fcePesquisaMineralProspeccao));
			}
		}catch(Exception e){
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}

	private void carregarGeofiscas(){
		try {
			for(Geofisica geofisicaObj: facade.listarGeofisicaBy(idefcePesquisaMineral)){
				for(Geofisica geo: listaGeofisca){
					if(geo.getIdeGeofisica().equals(geofisicaObj.getIdeGeofisica())){
						geo.setChecked(true);
					}
				}
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}

	private void carregarClasseResiduoGerado(){
		try {
			for(FcePesquisaMineralDestinoResiduo fcePesquisaMineralDestinoResiduo: facade.listarFcePesquisaMineralDestinoResiduoBy(idefcePesquisaMineral)){
				for (DestinoResiduo destinoResiduo : listaDestinoResiduo) {
					if(fcePesquisaMineralDestinoResiduo.getIdeFcePesquisaMineralDestinoResiduoPK().getIdeDestinoResiduo().equals(destinoResiduo.getIdeDestinoResiduo())){
						destinoResiduo.setChecked(true);
					}
				}
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}

	private void carregarTipoResiduo(){
		try {
			for(FcePesquisaMineralTipoResiduo fcePesquisaMineralTipoResiduo: facade.listarFcePesquisaMineralTipoResiduoBy(idefcePesquisaMineral)){
				for (TipoResiduoGerado tipoResiduoGerado : listaTipoResiduoGerado) {
					if(fcePesquisaMineralTipoResiduo.getIdeFcePesquisaMineralTipoResiduoPK().getIdeTipoResiduoGerado().equals(tipoResiduoGerado.getIdeTipoResiduoGerado())){
						tipoResiduoGerado.setChecked(true);
					}
				}
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}

	private void carregarAreaIntervencao(){
		try {
			listaFcePesquisaMineralMetodoRecuperacao = facade.listarMetodoRecuperacaoIntervencaoByFcePesquisaMineral(idefcePesquisaMineral);
			for(FcePesquisaMineralMetodoRecuperacao fcePesquisaMineralMetodoRecuperacao: listaFcePesquisaMineralMetodoRecuperacao ){
				for(MetodoRecuperacaoIntervencao metodoRecuperacaoIntervencao: listaMetodoRecuperacaoIntervencao) {
					if(fcePesquisaMineralMetodoRecuperacao.getIdeFcePesquisaMineralMetodoRecuperacaoPK().getIdeMetodoRecuperacaoIntervencao().equals(metodoRecuperacaoIntervencao.getIdeMetodoRecuperacaoIntervencao())){
						metodoRecuperacaoIntervencao.setChecked(true);
					}
				}
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}

	private void carregarlistaFcePesquisaMineralSubstanciaMineral() throws Exception{
		listaFcePesquisaMineralSubstanciaMineral = facade.listarFcePesquisaMineralSubstanciaMineralBy(idefcePesquisaMineral);
	}

	private void carregarListaFcePesquisaMineralOrigemEnergia(){
		try {
			for(FcePesquisaMineralOrigemEnergia fcePesquisaMineralOrigemEnergia: facade.listarFcePesquisaMineralOrigemEnergiaBy(idefcePesquisaMineral) ){
				for(TipoOrigemEnergia tipoOrigemEnergia: listaTipoOrigemEnergia){
					if(fcePesquisaMineralOrigemEnergia.getIdeTipoOrigemEnergia().getIdeTipoOrigemEnergia().equals(tipoOrigemEnergia.getIdeTipoOrigemEnergia())){
						tipoOrigemEnergia.setChecked(true);
					}
				}
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}

	@Override
	public void finalizar() {
		try{
			facade.finalizar(this);
		}catch(Exception e){
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public void prepararParaFinalizar() throws Exception {
		if (new Integer(activeTab).equals(ABA_DADOS_GERAIS)) {
			if(validarAbaDadosGerais()){
				facade.salvar(this);
				activeTab++;
			}
		} else if (new Integer(activeTab).equals(ABA_ATIVIDADES_PESQUISA)) {
			if(validarAbaAtividadesPesquisa()){
				facade.salvar(this);
				activeTab++;
			}
		} else if (new Integer(activeTab).equals(ABA_ASPECTOS_AMBIENTAIS)) {
			
			if(!validarAbaDadosGerais() || !validarAbaAtividadesPesquisa()){
				Html.atualizar("pnlAutMineracao");
			}
			
			if(validarAbaDadosGerais() && validarAbaAtividadesPesquisa() && validarAbaAspectosAmbientais()){
				
				facade.salvar(this);
				
				if(existeOutros()){
					super.exibirInformacao001();
					
				}else{
					super.concluirFce();
					Html.exibir("relAutMineracao");
				}
				
				Html.esconder("mineracaoAutorizacao");
				limpar();
				super.limparFce();

			}
		}
	}

	@Override
	public void controlarAbas(TabChangeEvent event) {
		if(abaDadosGerais.equals(event.getTab().getId())){
			activeTab = 0;
		}
		else if(abaAtividadesPesquisa.equals(event.getTab().getId())){
				activeTab = 1;
		}
		else if(abaAspectosAmbientais.equals(event.getTab().getId())){
				activeTab = 2;
		}
	}

	public void voltarAbas(){
		if (new Integer(activeTab).equals(ABA_ATIVIDADES_PESQUISA)) {
			activeTab--;
		} else if (new Integer(activeTab).equals(ABA_ASPECTOS_AMBIENTAIS)) {
			activeTab--;
		}
	}

	private boolean existeOutros() {
		boolean existeOutros = false;
		
		for(TipoOrigemEnergia tipoOrigemEnergia : super.listaTipoOrigemEnergia){
			if (tipoOrigemEnergia.isOutros() && tipoOrigemEnergia.isChecked()) {
				existeOutros = true;
			}
		}

		for(Geofisica geofisica: listaGeofisca){
			if(geofisica.isOutros() && geofisica.isChecked()){
				existeOutros = true;
			}
		}

		for(MetodoRecuperacaoIntervencao metodoRecuperacaoIntervencao: listaMetodoRecuperacaoIntervencao){
			if(metodoRecuperacaoIntervencao.isOutros() && metodoRecuperacaoIntervencao.isChecked()){
				existeOutros = true;
			}  
		}
		
		
		for(DestinoResiduo destinoResiduo: listaDestinoResiduo){
			if(destinoResiduo.isChecked() && destinoResiduo.isOutros()){
				existeOutros = true;
			}
		}
		

		return existeOutros;
	}

	public void getlistaFcePesquisaMineralOrigemEnergia() {
		if(super.listaTipoOrigemEnergia!=null){
			for(TipoOrigemEnergia tipoOrigemEnergia: super.listaTipoOrigemEnergia){
				if(tipoOrigemEnergia.isChecked()){
					listaFcePesquisaMineralOrigemEnergia.add(new FcePesquisaMineralOrigemEnergia(new FcePesquisaMineralOrigemEnergiaPK(idefcePesquisaMineral.getIdeFcePesquisaMineral(), tipoOrigemEnergia.getIdeTipoOrigemEnergia())));
				}
			}
		}
	}

	public void getlistaFcePesquisaMineralSubstanciaMineral(){
		for(FcePesquisaMineralSubstanciaMineralTipologia fcePesquisaMineralSubstanciaMineral: listaFcePesquisaMineralSubstanciaMineral){
			fcePesquisaMineralSubstanciaMineral.getIdeFcePesquisaMineralSubstanciaMineralPK().setIdeFcePesquisaMineral(idefcePesquisaMineral.getIdeFcePesquisaMineral());
		}
	}

	public int getSomentePonto() {
		return ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_PONTO.getId().intValue();
	}

	@Override
	public void verificarEdicao() {
		try {
			super.carregarFceDoRequerente(DOC_PESQUISA_MINERACAO);
		}catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar")	+ " o FCE - Licenciamento para Mineração.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@Override
	public void limpar() {
		
		idefcePesquisaMineral = null;
		
		/* Dados Gerais */
		fcePesquisaMineralSubstanciaMineral= null;
		 listaFcePesquisaMineralSubstanciaMineral= null ;
		listaFcePesquisaMineralOrigemEnergia = null;
		listaProcessoDnmp = null;

		/* Atividades Pesquisa */
		geofisica = null;
		metodoProspeccao= null;
		fceProspeccao = null;
		fcePesquisaMineralProspeccao= null;
		listaFcePesquisaMineralMetodoRecuperacao= null;
		listaMetodoRecuperacaoIntervencao= null;

		/*Aspectos ambietais*/
		listaGeofisca= null;
		listaMetodoProspeccao= null;
		listafceProspeccao= null;
		listaFcePesquisaMineralProspeccao= null;
		tituloDialogLocGeo= null;

		super.limpar();
	}

	@Override
	public boolean validarAba() {
		if (new Integer(activeTab).equals(ABA_DADOS_GERAIS)) {
			return validarAbaDadosGerais();
		} else if (new Integer(activeTab).equals(ABA_ATIVIDADES_PESQUISA)) {
			return validarAbaAtividadesPesquisa();
		} else if (new Integer(activeTab).equals(ABA_ASPECTOS_AMBIENTAIS)) {
			return super.validarAbaAspectosAmbientais();
		}
		return false;
	}

	private boolean validarAbaDadosGerais() {
		boolean isValido = true;
		
		if (Util.isNullOuVazio(listaFcePesquisaMineralSubstanciaMineral)) {
			activeTab = ABA_DADOS_GERAIS;
			JsfUtil.addErrorMessage(Util.getString("msg_generica_selecione_um") + " uma Substância mineral a ser pesquisada.");			
			isValido=  false;
		}

		if (Util.isNullOuVazio(idefcePesquisaMineral.getIndPesqIntervRecursoHidrico())) {
			activeTab = ABA_DADOS_GERAIS;
			super.exibirMensagem003("Pesquisa ou Intervenção em recurso hídrico");			
			isValido=  false;
		}

		if (!Util.isNullOuVazio(idefcePesquisaMineral.getIndPesqIntervRecursoHidrico())&& (idefcePesquisaMineral.getIndPesqIntervRecursoHidrico())) {
			if (!idefcePesquisaMineral.getIndEsferaEstadual() && !idefcePesquisaMineral.getIndEsferaFederal()) {
				activeTab = ABA_DADOS_GERAIS;
				JsfUtil.addErrorMessage(Util.getString("msg_generica_selecione_um") + " uma intervenção em corpo hídrico.");					
				isValido=  false;
			}
		}
			
		if(!super.isProcessoDnpmAdicionado()){
			isValido=  false;
		}
		
		if (Util.isNullOuVazio(listaProcessoDnmp)) {
			activeTab = ABA_DADOS_GERAIS;
			super.exibirMensagem003("Informações sobre a área junto ao DNPM");				
			isValido=  false;
		}

		if(!Util.isNullOuVazio(super.listaTipoOrigemEnergia)){
			boolean marcouOrigemEnergia = false;

			for(TipoOrigemEnergia tipoOrigemEnergia : super.listaTipoOrigemEnergia){
				if (tipoOrigemEnergia.isChecked()) {
					marcouOrigemEnergia = true;
				}
			}

			if (!marcouOrigemEnergia) {
				activeTab = ABA_DADOS_GERAIS;
				JsfUtil.addErrorMessage(Util.getString("msg_generica_selecione_um") + " um Suprimento de energia.");
				isValido=  false;
			}
		}
		
		return isValido;
	}

	private boolean validarAbaAtividadesPesquisa() {

		boolean isValido = true;
		boolean possuiProspeccao = false;
		boolean possuiGeofisica = false;
		boolean escolheuGeofisca = false;
		boolean possuiRecuperacaoIntervencao = false;
		
		for(MetodoProspeccao metodoProspeccao : listaMetodoProspeccao){
			if(metodoProspeccao.isChecked() && !metodoProspeccao.getIdeMetodoProspeccao().equals(MetodoProspeccaoEnum.GEOFISICA.getId())){
				possuiProspeccao = true;
			}else if(metodoProspeccao.isChecked() && metodoProspeccao.getIdeMetodoProspeccao().equals(MetodoProspeccaoEnum.GEOFISICA.getId())){
				possuiGeofisica = true;
			}
		}

		if(possuiGeofisica){
			for(Geofisica geofisica: listaGeofisca){
				if(geofisica.isChecked() ){
					escolheuGeofisca = true;
				}
			}
			if(!escolheuGeofisca){
				activeTab =ABA_ATIVIDADES_PESQUISA;
				JsfUtil.addErrorMessage(Util.getString("msg_generica_selecione_um") + " um método de prospecção mineral Geofísca" + ".");					
				isValido = false;
			}
		}

		if(!possuiProspeccao && !possuiGeofisica){
			activeTab =ABA_ATIVIDADES_PESQUISA;
			JsfUtil.addErrorMessage(Util.getString("msg_generica_selecione_um") + " um Método de prospecção mineral" + ".");				
			isValido =false;
		}

		for(MetodoProspeccao metodoProspeccao : listaMetodoProspeccao){
			if(metodoProspeccao.isChecked() && !metodoProspeccao.getIdeMetodoProspeccao().equals(MetodoProspeccaoEnum.GEOFISICA.getId())){
				if(listaFcePesquisaMineralProspeccao.isEmpty()){
					activeTab =ABA_ATIVIDADES_PESQUISA;
					JsfUtil.addErrorMessage(Util.getString("msg_generica_selecione_um") + " um " + metodoProspeccao.getNomMetodoProspeccao() + ".");
					isValido = false;
				}
				else{
					for(FcePesquisaMineralProspeccao fceProspeccao: listaFcePesquisaMineralProspeccao){
						if(fceProspeccao.getIdeMetodoProspeccao().getIdeMetodoProspeccao().equals(metodoProspeccao.getIdeMetodoProspeccao())){
							if(!isContemMetodoProspeccao(metodoProspeccao)){ 
								activeTab =ABA_ATIVIDADES_PESQUISA;
								JsfUtil.addErrorMessage("É obrigatória a inserção de ao menos uma localização geográfica para o método de prospecção mineral " + metodoProspeccao.getNomMetodoProspeccao()+".");									
								isValido = false;
							}
						}
					}
				}

			}
		}

		for(MetodoRecuperacaoIntervencao metodoRecuperacaoIntervencao: listaMetodoRecuperacaoIntervencao){
			if(metodoRecuperacaoIntervencao.isChecked()){
				possuiRecuperacaoIntervencao = true;
			}
		}

		if(!possuiRecuperacaoIntervencao){
			activeTab =ABA_ATIVIDADES_PESQUISA;
			JsfUtil.addErrorMessage(Util.getString("msg_generica_selecione_um") + " um " + "método de recuperacão da área de intervenção" + ".");				
			isValido = false;
		}

		return isValido;
	}


	@Override
	public boolean validarAbaAspectosAmbientais() {
		boolean isValido = true;
		boolean possuiTipoResiduoGerado = false;
		boolean possuiDestinoRediduo = false;

		for(TipoResiduoGerado tipoResiduoGerado: listaTipoResiduoGerado){
			if(tipoResiduoGerado.isChecked()){
				possuiTipoResiduoGerado = true;
			}
		}

		for(DestinoResiduo destinoResiduo: listaDestinoResiduo){
			if(destinoResiduo.isChecked()){
				possuiDestinoRediduo = true;
			}
		}

		if(!possuiTipoResiduoGerado){
			JsfUtil.addErrorMessage("É obrigatória a seleção de ao menos uma classe");
			isValido = false;
		}

		if(!possuiDestinoRediduo){
			JsfUtil.addErrorMessage("É obrigatória a seleção de ao menos um destino dos resíduos.");
			isValido = false;
		}

		return isValido;
	}

	private boolean isContemMetodoProspeccao(MetodoProspeccao metodoProspeccao){
		for (FcePesquisaMineralProspeccao fcePesquisaMineralProspeccaoObj : listaFcePesquisaMineralProspeccao) {
			if(fcePesquisaMineralProspeccaoObj.getIdeMetodoProspeccao().getIdeMetodoProspeccao().equals(metodoProspeccao.getIdeMetodoProspeccao())&&
					(!Util.isNullOuVazio(fcePesquisaMineralProspeccaoObj.getListaFceProspeccao()))){
				return true;
			}
		}
		return false;
	}

	@Override
	public void abrirDialog() {
		super.activeTab = 0;
		Html.exibir("mineracaoAutorizacao");
		Html.atualizar("dRelAutMineracao");
		Html.atualizar("mineracaoAutorizacao");
	}

	@Override
	protected FceMineracaoFacade getMineracaoFacade() {
		return facade;
	}

	@Override
	protected List<ProcessoDnpm> getListaProcessoDnpm() {
		return listaProcessoDnmp;
	}

	@Override
	protected boolean necessidadeDeSupressao() {
		return super.existeProcessoAsv() || isAtoAmbientalAA();
	}

	private boolean isAtoAmbientalAA() {
		try{
			return facade.isEnquadramentoAA(super.requerimento);
		}
		catch(Exception e){
			JsfUtil.addErrorMessage(Util.getString(""));
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@Override
	protected void prepararDuplicacao() {
		idefcePesquisaMineral.setIdeFce(super.fce);
		idefcePesquisaMineral.setIdeFcePesquisaMineral(null);
		for(FcePesquisaMineralSubstanciaMineralTipologia subMineral :listaFcePesquisaMineralSubstanciaMineral){
			subMineral.setIdeFcePesquisaMineral(idefcePesquisaMineral);
		}
		for(ProcessoDnpm processoDnpm : getListaProcessoDnpm()){
			processoDnpm.setIdeProcessoDnpm(null);
			processoDnpm.setIdeFcePesquisaMineral(idefcePesquisaMineral);
		}
		if(super.isExisteProcessoOutorga()) {
			for(OutorgaMineracao outorgaMineracao : super.listaOutorgaMineracao){
				outorgaMineracao.setIdeOutorgaMineracao(null);
				outorgaMineracao.setFcePesquisaMineral(idefcePesquisaMineral);
			}
		}
		for(FcePesquisaMineralProspeccao fcePesquisaMineralProspeccao : listaFcePesquisaMineralProspeccao) {
			fcePesquisaMineralProspeccao.setIdeFcePesquisaMineralProspeccao(null);
			fcePesquisaMineralProspeccao.setIdeFcePesquisaMineral(idefcePesquisaMineral);
		}
		for(FceProspeccao prospeccao : listafceProspeccao) {
			prospeccao.setIdeFceProspeccao(null);
			prospeccao.setIdeFcePesquisaMineralProspeccao(fcePesquisaMineralProspeccao);
		}
	}

	@Override
	protected void duplicarFce() {
		try {
			for(FceProspeccao prospeccao : listafceProspeccao) {
				LocalizacaoGeografica localizacaoGeografica = prospeccao.getIdeLocalizacaoGeografica();
				prospeccao.setIdeLocalizacaoGeografica(facade.duplicarLocalizacaoGeografica(localizacaoGeografica));
			}
			activeTab = 0;
			while(activeTab <= 2){
				facade.salvar(this);
				activeTab++;
			}

		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_duplicar") + " Autorização para Mineração.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);

		}
	}

	@Override
	protected void carregarFceTecnico() {
		super.init();
		carregarFcePesquisaMineral();
	}


	/*
	 * Atividades Pesquisa
	 * */
	public void selecionarProspeccao(MetodoProspeccao metodoProspeccao){
		if(!isFcePesquisaMineralContemMetodoProspeccao(metodoProspeccao)){
			FcePesquisaMineralProspeccao fcePesquisaMineralProspeccao = new FcePesquisaMineralProspeccao();
			fcePesquisaMineralProspeccao.setIdeMetodoProspeccao(metodoProspeccao);
			listaFcePesquisaMineralProspeccao.add(fcePesquisaMineralProspeccao);
		}
	}

	private boolean isFcePesquisaMineralContemMetodoProspeccao(MetodoProspeccao metodoProspeccao){
		for(FcePesquisaMineralProspeccao fcePesquisaMineralProspeccaoObj: listaFcePesquisaMineralProspeccao){
			if(fcePesquisaMineralProspeccaoObj.getIdeMetodoProspeccao().getIdeMetodoProspeccao().equals(metodoProspeccao.getIdeMetodoProspeccao())){
				return true;
			}
		}
		return false;
	}


	public void iniciarMetodoProspeccao(MetodoProspeccaoEnum metodoProspeccaoEnum ){
		fceProspeccao=new FceProspeccao();
		for (FcePesquisaMineralProspeccao fcePesquisaMineralProspeccao : listaFcePesquisaMineralProspeccao) {
			if(fcePesquisaMineralProspeccao.getIdeMetodoProspeccao().getIdeMetodoProspeccao().equals(metodoProspeccaoEnum.getId())){
				fceProspeccao.setIdeFcePesquisaMineralProspeccao(fcePesquisaMineralProspeccao);
			}
		}

		getTituloDialog(metodoProspeccaoEnum);

		Html.atualizar(dialogProspeccao);
		Html.exibir(dialogProspeccao);
	}

	public void editarLocGeo(){
		tituloDialogLocGeo = "Editar";

		Html.atualizar(dialogProspeccao);
		Html.exibir(dialogProspeccao);
		
	}
	
	public void inserirPesquisaMineralProspeccao(){
		
		boolean isInsercao = false;
		
		if(validarMetodoProspeccao()){
			for (FcePesquisaMineralProspeccao fcePesquisaMineralProspeccao : listaFcePesquisaMineralProspeccao) {
				if(fcePesquisaMineralProspeccao.getIdeMetodoProspeccao().getIdeMetodoProspeccao().equals(fceProspeccao.getIdeFcePesquisaMineralProspeccao().getIdeMetodoProspeccao().getIdeMetodoProspeccao())){
					if(!fcePesquisaMineralProspeccao.getListaFceProspeccao().contains(fceProspeccao)){
						fcePesquisaMineralProspeccao.getListaFceProspeccao().add(fceProspeccao);
						isInsercao = true;
					}
					break;
				}
			}
			fceProspeccao = new FceProspeccao();
			
			Html.esconder(dialogProspeccao);
			
			if(isInsercao){
				super.exibirMensagem001();
			}else{
				super.exibirMensagem016("Prospecção mineral");
			}
		}
	}

	private boolean validarMetodoProspeccao(){
		boolean isValido = true;
		
		if(Util.isNullOuVazio(fceProspeccao.getSeqProspeccao())){
			super.exibirMensagem003("Sequência ou identificação");
			isValido =  false;
		}else if(Util.isNullOuVazio(fceProspeccao.getIdeLocalizacaoGeografica())){
			super.exibirMensagem003("Localização Geográfica");
			isValido =  false;
		}
		
		return isValido;
	}

	public void removerFceProspeccao(){
		FceProspeccao fceProspeccaoParaRemover = new FceProspeccao();

		for(FcePesquisaMineralProspeccao fcePesquisaMineralProspeccao: listaFcePesquisaMineralProspeccao){
			for(FceProspeccao fceProspeccao: fcePesquisaMineralProspeccao.getListaFceProspeccao()){
				if(fceProspeccao.getIdeFceProspeccao()!=null && this.fceProspeccao.getIdeFceProspeccao()!=null){
					if(fceProspeccao.getIdeFceProspeccao().equals(this.fceProspeccao.getIdeFceProspeccao())){
						fceProspeccaoParaRemover = fceProspeccao;
					}
				}else{
					if(fceProspeccao.getIdeLocalizacaoGeografica().equals(this.fceProspeccao.getIdeLocalizacaoGeografica())){
						fceProspeccaoParaRemover = fceProspeccao;
					}
				}
			}
			fcePesquisaMineralProspeccao.getListaFceProspeccao().remove(fceProspeccaoParaRemover);

		}

		Html.esconder("cformExcluirFceProspeccao");
		super.exibirMensagem005();
		
	}


	private List<FceProspeccao> carregarEdicao(List<FceProspeccao> listaFceProspeccao, MetodoProspeccao metodoProspeccao){
		
		for (FceProspeccao fceProspeccao : listaFceProspeccao) {
			fceProspeccao.getIdeFcePesquisaMineralProspeccao().setIdeMetodoProspeccao(metodoProspeccao);
		}
		return listaFceProspeccao;
	}
	
	public List<FceProspeccao> getFceProspeccao(MetodoProspeccaoEnum metodoProspeccaoEnum){
		for(FcePesquisaMineralProspeccao fceProspeccao: listaFcePesquisaMineralProspeccao){
			if(fceProspeccao.getIdeMetodoProspeccao().getIdeMetodoProspeccao().equals(metodoProspeccaoEnum.getId())){
				return carregarEdicao(fceProspeccao.getListaFceProspeccao(), fceProspeccao.getIdeMetodoProspeccao());
			}
		}
		return null;
	}

	public boolean getRenderFceProspeccao(MetodoProspeccaoEnum metodoProspeccaoEnum){
		if(!Util.isNullOuVazio(listaFcePesquisaMineralProspeccao)){
			for(FcePesquisaMineralProspeccao fcePesquisaMineralProspeccao : listaFcePesquisaMineralProspeccao){
				if(fcePesquisaMineralProspeccao.getIdeMetodoProspeccao().getIdeMetodoProspeccao().equals(metodoProspeccaoEnum.getId()) &&
						(!fcePesquisaMineralProspeccao.getListaFceProspeccao().isEmpty())){
					return true;
				}
			}
		}
		return false;
	}
	
	private void getTituloDialog(MetodoProspeccaoEnum metodoProspeccaoEnum){
		if(metodoProspeccaoEnum.getId().equals(MetodoProspeccaoEnum.SONDAGENS.getId())){
			tituloDialogLocGeo = "Incluir Furos de Sondagem";
		}else if (metodoProspeccaoEnum.getId().equals(MetodoProspeccaoEnum.POCOS.getId())){
			tituloDialogLocGeo = "Incluir Poços";
		}else if (metodoProspeccaoEnum.getId().equals(MetodoProspeccaoEnum.TRINCHEIRAS.getId())){
			tituloDialogLocGeo = "Incluir Trincheira";
		}else if (metodoProspeccaoEnum.getId().equals(MetodoProspeccaoEnum.GALERIAS.getId())){
			tituloDialogLocGeo = "Incluir Galerias";
		}else if (metodoProspeccaoEnum.getId().equals(MetodoProspeccaoEnum.AMOSTRAGEM.getId())){
			tituloDialogLocGeo = "Incluir Amostragem";
		}
	}
	
	@Override
	public void excluirProcessoDnpm() {
		try {
			super.excluirProcessoDnpm();
			super.exibirMensagem005();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}

	@Override
	public void excluirSubstanciaMineral() {
		listaFcePesquisaMineralSubstanciaMineral.remove(fcePesquisaMineralSubstanciaMineral);
		super.exibirMensagem005();
	}

	public void excluirLocGeo(){
		try {
			facade.excluirLocalizacaoGeografica(fceProspeccao.getIdeLocalizacaoGeografica());
			fceProspeccao.setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
			Html.atualizar("pnlLocGeo");
			Html.esconder("cformExcluirLocGeo");
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}

	public void changeIntervencaoCorpoHidrico(ValueChangeEvent event) {
		if (!(Boolean) event.getNewValue()) {
			idefcePesquisaMineral.setIndEsferaEstadual(null);
			idefcePesquisaMineral.setIndEsferaFederal(null);
		}
	}

	public boolean render(MetodoProspeccaoEnum metodoProspeccaoEnum) {
		for (MetodoProspeccao metodoProspeccao : listaMetodoProspeccao) {
			if (metodoProspeccao.getIdeMetodoProspeccao().equals(metodoProspeccaoEnum.getId()) && metodoProspeccao.isChecked()) {
				return true;
			}
		}
		return false;
	}

	public boolean isLocGeoFceProspeccaoSalva() {
		return !Util.isNull(fceProspeccao) && !Util.isNull(fceProspeccao.getIdeLocalizacaoGeografica()) && super.isLocalizacaoGeograficaSalva(fceProspeccao.getIdeLocalizacaoGeografica());
	}

	public StreamedContent getImprimirRelatorio() {
		try{
			return super.getImprimirRelatorio(super.fce, DOC_PESQUISA_MINERACAO);
		}
		catch(Exception e){
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_imprimir_relatorio") + " do FCE - Autorização para Mineração.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Messagens
	 * */
	public void exibirAlertaOutrosDestinoResiduos(DestinoResiduo destinoResiduo){
		if(destinoResiduo.isOutros() && destinoResiduo.isChecked()){
			super.exibirInformacao001();
		}
	}

	public void exibirAlertaOutrosSubstanciaMineral(SubstanciaMineral substanciaMineral){
		if(substanciaMineral.isOutros()){
			super.exibirInformacao001();
		}
	}

	public void exibirAlertaOutrosSuprimentoEnergia(TipoOrigemEnergia tipoOrigemEnergia){
		if(tipoOrigemEnergia.isOutros() && tipoOrigemEnergia.isChecked()){
			super.exibirInformacao001();
		}
	}

	public void exibirAlertaOutrosGalerias(Geofisica geofisica){
		if( geofisica.isOutros() && geofisica.isChecked() ){
			super.exibirInformacao001();
		}
	}

	public void exibirAlertaRecuperacaoIntervenvao(MetodoRecuperacaoIntervencao metodoRecuperacaoIntervencao){
		if(metodoRecuperacaoIntervencao.getIdeMetodoRecuperacaoIntervencao().equals(MetodoRecuperacaoIntervencaoEnum.OUTROS_METODOS.getId()) && metodoRecuperacaoIntervencao.isChecked()){
			super.exibirInformacao001();
		}
	}

	@Override
	protected void fecharDialogProcessoDNPM() {
		Html.esconder(dialogProcessoDnpm);
	}
	
	/**
	 * Gets e Sets
	 * */
	public List<MetodoProspeccao> getListaMetodoProspeccao() {
		return listaMetodoProspeccao;
	}

	public void setListaMetodoProspeccao(List<MetodoProspeccao> listaMetodoProspeccao) {
		this.listaMetodoProspeccao = listaMetodoProspeccao;
	}

	public List<FcePesquisaMineralProspeccao> getListaFcePesquisaMineralProspeccao() {
		return listaFcePesquisaMineralProspeccao;
	}

	public void setListaFcePesquisaMineralProspeccao(List<FcePesquisaMineralProspeccao> listaFcePesquisaMineralProspeccao) {
		this.listaFcePesquisaMineralProspeccao = listaFcePesquisaMineralProspeccao;
	}

	public FcePesquisaMineral getIdeFcePesquisaMineral() {
		return idefcePesquisaMineral;
	}

	public void setIdeFcePesquisaMineral(FcePesquisaMineral fcePesquisaMineral) {
		this.idefcePesquisaMineral = fcePesquisaMineral;
	}

	public void setListaTipoOrigemEnergia(List<TipoOrigemEnergia> listaTipoOrigemEnergia) {
		this.listaTipoOrigemEnergia = listaTipoOrigemEnergia;
	}

	public List<Geofisica> getListaGeofisca() {
		return listaGeofisca;
	}

	public void setListaGeofisca(List<Geofisica> listaGeofisca) {
		this.listaGeofisca = listaGeofisca;
	}

	public FcePesquisaMineralSubstanciaMineralTipologia getFcePesquisaMineralSubstanciaMineral() {
		return fcePesquisaMineralSubstanciaMineral;
	}

	public void setFcePesquisaMineralSubstanciaMineral(FcePesquisaMineralSubstanciaMineralTipologia fcePesquisaMineralSubstanciaMineral) {
		this.fcePesquisaMineralSubstanciaMineral = fcePesquisaMineralSubstanciaMineral;
	}

	public FceProspeccao getFceProspeccao() {
		return fceProspeccao;
	}

	public void setFceProspeccao(FceProspeccao fceProspeccao) {
		this.fceProspeccao = fceProspeccao;
	}

	public MetodoProspeccao getMetodoProspeccao() {
		return metodoProspeccao;
	}

	public void setMetodoProspeccao(MetodoProspeccao metodoProspeccao) {
		this.metodoProspeccao = metodoProspeccao;
	}

	public String getTituloDialogLocGeo() {
		return tituloDialogLocGeo;
	}

	public void setTituloDialogLocGeo(String tituloDialogLocGeo) {
		this.tituloDialogLocGeo = tituloDialogLocGeo;
	}

	public List<FcePesquisaMineralOrigemEnergia> getListaFcePesquisaMineralOrigemEnergia() {
		return listaFcePesquisaMineralOrigemEnergia;
	}

	public void setListaFcePesquisaMineralOrigemEnergia(List<FcePesquisaMineralOrigemEnergia> listaFcePesquisaMineralOrigemEnergia) {
		this.listaFcePesquisaMineralOrigemEnergia = listaFcePesquisaMineralOrigemEnergia;
	}

	public List<FcePesquisaMineralSubstanciaMineralTipologia> getListaFcePesquisaMineralSubstanciaMineral() {
		return listaFcePesquisaMineralSubstanciaMineral;
	}

	public void setListaFcePesquisaMineralSubstanciaMineral(List<FcePesquisaMineralSubstanciaMineralTipologia> listaFcePesquisaMineralSubstanciaMineral) {
		this.listaFcePesquisaMineralSubstanciaMineral = listaFcePesquisaMineralSubstanciaMineral;
	}

	public List<ProcessoDnpm> getListaProcessoDnmp() {
		return listaProcessoDnmp;
	}

	public void setListaProcessoDnmp(List<ProcessoDnpm> listaProcessoDnmp) {
		this.listaProcessoDnmp = listaProcessoDnmp;
	}

	public Geofisica getGeofisica() {
		return geofisica;
	}

	public void setGeofisica(Geofisica geofisica) {
		this.geofisica = geofisica;
	}

	public FcePesquisaMineralProspeccao getFcePesquisaMineralProspeccao() {
		return fcePesquisaMineralProspeccao;
	}

	public void setFcePesquisaMineralProspeccao(FcePesquisaMineralProspeccao fcePesquisaMineralProspeccao) {
		this.fcePesquisaMineralProspeccao = fcePesquisaMineralProspeccao;
	}

	public List<FceProspeccao> getListafceProspeccao() {
		return listafceProspeccao;
	}

	public void setListafceProspeccao(List<FceProspeccao> listafceProspeccao) {
		this.listafceProspeccao = listafceProspeccao;
	}

	@Override
	public void salvarSubstanciaMineralFechar() {
		if(salvarSubstanciaMineralContinuar()){
			Html.esconder("incluirSubstanciaMineral");
		}
	}

	@Override
	public boolean salvarSubstanciaMineralContinuar() {
		if(super.isSubstanciaMineral()){
			if(Util.isNull(listaFcePesquisaMineralSubstanciaMineral)){
				listaFcePesquisaMineralSubstanciaMineral = new ArrayList<FcePesquisaMineralSubstanciaMineralTipologia>();
			} 
			boolean podeAdicionar = true;
			for(FcePesquisaMineralSubstanciaMineralTipologia fcePesquisaMineralSubstanciaMineral : listaFcePesquisaMineralSubstanciaMineral){
				if(super.getSubstanciaMineralSelecionada().equals(fcePesquisaMineralSubstanciaMineral.getSubstanciaMineralTipologia())){
					podeAdicionar = false;
					JsfUtil.addErrorMessage("A " + Util.getString("fce_lic_min_substancia_mineral") + " já foi selecionada.");
					return false;
				}
			}
			if(podeAdicionar){
				listaFcePesquisaMineralSubstanciaMineral.add(new FcePesquisaMineralSubstanciaMineralTipologia(idefcePesquisaMineral, super.getSubstanciaMineralSelecionada()));
				Html.atualizar("tabAutMineracao:minAutDadosGerais:dataTableMinAutSubstancia");
				if(super.getSubstanciaMineralSelecionada().getIdeSubstanciaMineral().isOutros()){
					super.exibirInformacao001();
				}
				super.exibirMensagem001();
				return true;
			}
		}
		return false;
	}

	public List<FcePesquisaMineralProspeccao> getListaFcePesquisaMineralProspeccaoExclusao() {
		return listaFcePesquisaMineralProspeccaoExclusao;
	}

	public void setListaFcePesquisaMineralProspeccaoExclusao(
			List<FcePesquisaMineralProspeccao> listaFcePesquisaMineralProspeccaoExclusao) {
		this.listaFcePesquisaMineralProspeccaoExclusao = listaFcePesquisaMineralProspeccaoExclusao;
	}
}