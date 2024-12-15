package br.gov.ba.seia.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.entity.ClassificacaoRejeitoDnpm;
import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.FceLicenciamentoMineral;
import br.gov.ba.seia.entity.FceLicenciamentoMineralProducaoProduto;
import br.gov.ba.seia.entity.FceLicenciamentoMineralServidaoMineraria;
import br.gov.ba.seia.entity.FceLicenciamentoMineralSubstanciaMineralTipologia;
import br.gov.ba.seia.entity.FceLicenciamentoMineralTipoApp;
import br.gov.ba.seia.entity.FormaDisposicaoRejeito;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.MetodoLavra;
import br.gov.ba.seia.entity.MetodoRecuperacaoIntervencao;
import br.gov.ba.seia.entity.OutorgaMineracao;
import br.gov.ba.seia.entity.ProcessoDnpm;
import br.gov.ba.seia.entity.ProducaoProduto;
import br.gov.ba.seia.entity.RegimeExploracao;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.ServidaoMineraria;
import br.gov.ba.seia.entity.TecnicaLavra;
import br.gov.ba.seia.entity.TipoApp;
import br.gov.ba.seia.entity.TipoEmissaoAtmosferica;
import br.gov.ba.seia.entity.TipoEstrutura;
import br.gov.ba.seia.entity.TipoSistemaTratamento;
import br.gov.ba.seia.entity.TipoTransporteMinerio;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.enumerator.ClassificacaoSecaoEnum;
import br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum;
import br.gov.ba.seia.enumerator.MetodoLavraEnum;
import br.gov.ba.seia.enumerator.TecnicaLavraEnum;
import br.gov.ba.seia.facade.FceLicenciamentoMineracaoFacade;
import br.gov.ba.seia.facade.FceMineracaoFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

/**
 * Controller responsável pelo <b>FCE - Licenciamento Mineração</b>
 *
 * @author eduardo.fernandes
 * @since 09/06/2016
 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
 */
@Named("fceLicenciamentoMineracaoController")
@ViewScoped
public class FceLicenciamentoMineracaoController extends BaseFceMineracaoController {

	@EJB
	private FceLicenciamentoMineracaoFacade facade;

	private static final DocumentoObrigatorio DOC_LICENCIAMENTO_MINERACAO = new DocumentoObrigatorio(DocumentoObrigatorioEnum.FCE_LICENCIAMENTO_MINERACAO.getId());
	private static final Integer ABA_DADOS_GERAIS = 0;
	private static final Integer ABA_QUADRO_AREAS = 1;
	private static final Integer ABA_CARACTERIZACAO_ATIVIDADES = 2;
	private static final Integer ABA_ASPECTOS_AMBIENTAIS = 3;

	private FceLicenciamentoMineral fceLicenciamentoMineral;
	private boolean licencaPrevia;
	private boolean edicao;

	// ABA Dados Gerais
	private List<FceLicenciamentoMineralSubstanciaMineralTipologia> listaFceLicenciamentoMineralSubstanciaMineral;
	private FceLicenciamentoMineralSubstanciaMineralTipologia fceLicenciamentoMineralSubstanciaMineral;
	private List<RegimeExploracao> listaRegimeExploracao;
	private List<RegimeExploracao> listaRegimeExploracaoSelected;
	private List<Tipologia> listaTipologiasEtapa7;
	
	
	// ABA Caracterização das Atividades
	private List<MetodoLavra> listaMetodoLavra;
	private List<MetodoLavra> listaMetodoLavraSelected;

	private List<TecnicaLavra> listaTecnicaLavraCeuAberto;
	private List<TecnicaLavra> listaTecnicaLavraCeuAbertoSelected;
	private List<TecnicaLavra> listaTecnicaLavraCeuAbertoDragagem;
	private List<TecnicaLavra> listaTecnicaLavraCeuAbertoDragagemSelected;
	private List<TecnicaLavra> listaTecnicaLavraSubterranea;
	private List<TecnicaLavra> listaTecnicaLavraSubterraneaSelected;

	private List<FormaDisposicaoRejeito> listaFormaDisposicaoRejeito;
	private FormaDisposicaoRejeito formaDisposicaoRejeito;

	private List<TipoEstrutura> listaTipoEstrutura;

	private List<ServidaoMineraria> listaServidaoMineraria;
	private ServidaoMineraria servidaoMineraria;
	private List<FceLicenciamentoMineralServidaoMineraria> listaFceLicenciamentoMineralServidaoMineraria;
	private FceLicenciamentoMineralServidaoMineraria fceLicenciamentoMineralServidaoMineraria;

	private List<ClassificacaoRejeitoDnpm> listaClassificacaoRejeitoDnpm;

	private List<TipoTransporteMinerio> listaTipoTransporteMinerio;
	private List<TipoTransporteMinerio> listaTipoTransporteMinerioSelected;

	private List<TipoApp> listaTipoApp;
	private TipoApp tipoApp;

	private List<FceLicenciamentoMineralTipoApp> listaFceLicenciamentoMineralTipoApp;
	private FceLicenciamentoMineralTipoApp fceLicenciamentoMineralTipoApp;

	private String concentradoProduto;
	private List<ProducaoProduto> listaProducaoProduto;
	private List<ProducaoProduto> listaProducaoProdutoCompleta;
	private ProducaoProduto producaoProduto ;
	private List<FceLicenciamentoMineralProducaoProduto> listaFceLicenciamentoMineralProducaoProduto;
	private FceLicenciamentoMineralProducaoProduto fceLicenciamentoMineralProducaoProduto;

	// ABA Aspectos Ambientais
	private List<TipoSistemaTratamento> listaTipoSistemaTratamento;
	private List<TipoSistemaTratamento> listaTipoSistemaTratamentoSelected;

	private List<TipoEmissaoAtmosferica> listaTipoEmissaoAtmosferica;
	private List<TipoEmissaoAtmosferica> listaTipoEmissaoAtmosfericaSelected;

	@Override
	protected FceMineracaoFacade getMineracaoFacade() {
		return facade;
	}

	@Override
	public void init() {
		limpar();
		verificarEdicao();
		if(!super.isFceSalvo()){
			super.iniciarFce(DOC_LICENCIAMENTO_MINERACAO);
			fceLicenciamentoMineral = new FceLicenciamentoMineral(super.fce);
			fceLicenciamentoMineral.setIndSupressao(necessidadeDeSupressao());
		}
		else{
			edicao = true;
			carregarFceLicenciamentoMineral();
		}
		super.init();
	}

	@Override
	protected boolean necessidadeDeSupressao() {
		return super.existeProcessoAsv() || isLicencaAndASV();
	}

	/**
	 * RN 00162 - Supressão de vegetação nativa ​ (...) se o requerimento for
	 * enquadrado com (...) Licença e ASV.
	 *
	 * @author eduardo.fernandes
	 * @since 22/06/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @return
	 * @throws Exception
	 */
	private boolean isLicencaAndASV() {
		try{
			return facade.isEnquadramentoLicencaAndASV(super.requerimento);
		}
		catch(Exception e){
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " as informações do Enquadramento.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);

		}
	}

	@Override
	public void controlarAbas(TabChangeEvent event) {
		if("abaDadosGerais".equals(event.getTab().getId())){
			activeTab = 0;
		}
		else if("abaQuadroDeAreas".equals(event.getTab().getId())){
			activeTab = 1;
		}
		else if("abaCaracterizacaoDasAtividades".equals(event.getTab().getId())){
			activeTab = 2;
		}
		else if("abaAspectosAmbientais".equals(event.getTab().getId())){
			activeTab = 3;
		}

	}

	/**
	 * Método para salvar a aba ativa.
	 *
	 * @author eduardo.fernandes
	 * @throws Exception
	 * @since 15/06/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 */
	private void salvarAba() throws Exception {
		if(isAbaDadosGerais()){
			salvarAbaDadosGerais();
		}
		else if(isAbaQuadroDeAreas()){
			salvarAbaQuadroDeAreas();
		}
		else if(isAbaCaracterizacaoDeAtividades()){
			salvarAbaCaracterizacaoDeAtividades();
		}
		else if(isAbaAspectosAmbientais()){
			salvarAbaAspectosAmbientais();
		}
	}

	/**
	 * Método que salva a aba Dados Gerais.
	 *
	 * @author eduardo.fernandes
	 * @throws Exception
	 * @since 15/06/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 */
	private void salvarAbaDadosGerais() throws Exception {
		facade.salvarAbaDadosGerais(this);
	}


	/**
	 * Método que salva a aba Quadro de Áreas.
	 *
	 * @author eduardo.fernandes
	 * @throws Exception
	 * @since 15/06/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 */
	private void salvarAbaQuadroDeAreas() throws Exception {
		facade.salvarAbaQuadroDeAreas(this);
	}

	/**
	 * Método que salva a aba Caracterização de Atividades.
	 *
	 * @author eduardo.fernandes
	 * @throws Exception
	 * @since 15/06/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 */
	private void salvarAbaCaracterizacaoDeAtividades() throws Exception {
		facade.salvarAbaCaracterizacaoDeAtividades(this);
	}

	/**
	 * Método que salva a aba Aspectos Ambientais.
	 *
	 * @author eduardo.fernandes
	 * @throws Exception
	 * @since 15/06/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 */
	private void salvarAbaAspectosAmbientais() throws Exception {
		facade.salvarAbaAspectosAmbientais(this);
	}

	@Override
	public void avancarAba() {
		try{
			if(validarAba()){
				salvarAba();
				activeTab++;
			}
		}
		catch(Exception e){
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_salvar") + nomeAba());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	private void carregarFceLicenciamentoMineral() {
		try{
			fceLicenciamentoMineral = facade.buscarFceLicenciamentoMineralBy(super.fce);
		}
		catch(Exception e){
			JsfUtil.addErrorMessage(Util.getString("")); // ADICIONAR MSG
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@Override
	public void verificarEdicao() {
		try{
			carregarFceDoRequerente(DOC_LICENCIAMENTO_MINERACAO);
		}
		catch(Exception e){
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " o FCE - Licenciamento para Mineração.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public boolean isIntervencaoCorpoHidrico(){
		return !Util.isNull(fceLicenciamentoMineral.getIndPesqintervRecursoHidrico()) && fceLicenciamentoMineral.getIndPesqintervRecursoHidrico();
	}

	public void changeIntervencaoCorpoHidrico(ValueChangeEvent event){
		if(!(Boolean) event.getNewValue()){
			fceLicenciamentoMineral.setIndEsferaEstadual(null);
			fceLicenciamentoMineral.setIndEsferaFederal(null);
		}
	}

	public boolean isBeneficiamentoProprio() {
		return !Util.isNull(fceLicenciamentoMineral.getIndBeneficiamentoMineracao()) && fceLicenciamentoMineral.getIndBeneficiamentoMineracao();
	}

	public void changeBeneficiamentoProprio(ValueChangeEvent event){
		if(!(Boolean) event.getNewValue()){
			limparFormaDisposicaoDeRejeito();
			if(super.isFceTecnico()) {
				limparListaProducaoProduto();
				limparListaFceLicenciamentoProducaoProduto();
			}
		}
		else {
			if(super.isFceTecnico() && Util.isNullOuVazio(listaProducaoProduto)) {
				carregarListaProducaoProduto();
			}
		}
	}

	private void limparListaFceLicenciamentoProducaoProduto() {
		listaFceLicenciamentoMineralProducaoProduto = null;
	}

	public boolean isPossuiApp() {
		return !Util.isNull(fceLicenciamentoMineral.getIndApp()) && fceLicenciamentoMineral.getIndApp();
	}

	public void changePossuiApp(ValueChangeEvent event){
		if(!(Boolean) event.getNewValue()){
			if(!Util.isNullOuVazio(listaFceLicenciamentoMineralTipoApp)){
				for(FceLicenciamentoMineralTipoApp fceLicenciamentoMineralTipoApp : listaFceLicenciamentoMineralTipoApp){
					listaTipoApp.add(fceLicenciamentoMineralTipoApp.getTipoApp());
				}
				Collections.sort(listaTipoApp);
				listaFceLicenciamentoMineralTipoApp = null;
			}
		}
	}

	public void adicionarApp(){
		if(!isAppAdicionada()){
			listaFceLicenciamentoMineralTipoApp = new ArrayList<FceLicenciamentoMineralTipoApp>();
		}
		listaTipoApp.remove(tipoApp);
		listaFceLicenciamentoMineralTipoApp.add(new FceLicenciamentoMineralTipoApp(fceLicenciamentoMineral, tipoApp));
		super.exibirMensagem001();
	}

	public boolean isExisteAppParaAdicionar() {
		return isPossuiApp() && !Util.isNullOuVazio(listaTipoApp);
	}

	public boolean isAppAdicionada() {
		return !Util.isNullOuVazio(listaFceLicenciamentoMineralTipoApp);
	}

	public void excluirApp(){
		listaFceLicenciamentoMineralTipoApp.remove(fceLicenciamentoMineralTipoApp);
		listaTipoApp.add(fceLicenciamentoMineralTipoApp.getTipoApp());
		Collections.sort(listaTipoApp);
		super.exibirMensagem005();
	}

	public void adicionarServidaoMineraria(){
		if(!isServidaoMinerariaAdicionada()){
			listaFceLicenciamentoMineralServidaoMineraria = new ArrayList<FceLicenciamentoMineralServidaoMineraria>();
		}
		listaServidaoMineraria.remove(servidaoMineraria);
		listaFceLicenciamentoMineralServidaoMineraria.add(new FceLicenciamentoMineralServidaoMineraria(fceLicenciamentoMineral, servidaoMineraria));
		super.exibirMensagem001();
	}

	public boolean isServidaoMinerariaAdicionada() {
		return !Util.isNullOuVazio(listaFceLicenciamentoMineralServidaoMineraria);
	}

	public boolean isExisteServidaoMinerariaParaAdicionar() {
		return !Util.isNullOuVazio(listaServidaoMineraria);
	}

	public void confirmarServidaoMineraria(ActionEvent event) {
		fceLicenciamentoMineralServidaoMineraria = (FceLicenciamentoMineralServidaoMineraria) event.getComponent().getAttributes().get("fceLicMinServidaoMineraria");
		if(isServidaoMinerariaValida(fceLicenciamentoMineralServidaoMineraria)){
			fceLicenciamentoMineralServidaoMineraria.setConfirmado(true);
			if(!fceLicenciamentoMineralServidaoMineraria.isEdicao()){
				super.exibirMensagem001();
			}
			else{
				super.exibirMensagem002();
			}
		}
	}

	private boolean isServidaoMinerariaValida(FceLicenciamentoMineralServidaoMineraria fceLicenciamentoMineralServidaoMineraria) {
		if(Util.isNullOuVazio(fceLicenciamentoMineralServidaoMineraria.getAreaServidaoMineraria())){
			JsfUtil.addErrorMessage("A " + Util.getString("fce_lic_min_area_comprimento") + " " + Util.getString("msg_generica_preenchimento_obrigatorio"));
			return false;
		}
		return true;

	}

	public void editarServidaoMineraria(ActionEvent event) {
		fceLicenciamentoMineralServidaoMineraria = (FceLicenciamentoMineralServidaoMineraria) event.getComponent().getAttributes().get("fceLicMinServidaoMineraria");
		fceLicenciamentoMineralServidaoMineraria.setConfirmado(false);
		fceLicenciamentoMineralServidaoMineraria.setEdicao();
	}

	public void excluirServidaoMineraria(){
		listaFceLicenciamentoMineralServidaoMineraria.remove(fceLicenciamentoMineralServidaoMineraria);
		listaServidaoMineraria.add(fceLicenciamentoMineralServidaoMineraria.getServidaoMineraria());
		ordernarServidaoMineraria();
		super.exibirMensagem005();
	}

	@Override
	public void carregarAba() {
		carregarAbaDadosGerais();
		carregarAbaQuadroAreas();
		carregarAbaCaracterizacaoAtividade();
		carregarAbaAspectosAmbientais();
	}

	@Override
	protected void carregarAbaDadosGerais() {
		try{
			if(!super.isFceTecnico()){
				montarArvore(super.requerimento);
			} else {
				montarArvore(null);
			}
			if(!facade.isEnquadramentoDeLincencaComOutorga(super.requerimento)) {
				super.montarListaOutorgaMineracao(fceLicenciamentoMineral);
			}
			carregarListalistaRegimeExploracao();
			carregarListaTipoOrigemEnergia();
		}
		catch(Exception e){
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + nomeAba());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			RequestContext.getCurrentInstance().execute("mineracaoLicenciamento.hide()");
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}

	}

	protected void montarArvore(Requerimento requerimento) throws Exception {
		if(!Util.isNull(requerimento)){
			listaTipologiasEtapa7 = facade.listarTipologiaDaEtapa7(requerimento);
			for(Tipologia tEtapa7 : listaTipologiasEtapa7){
				tEtapa7.setValorAtividade(new BigDecimal(facade.obterValorDaAtividade(super.requerimento, tEtapa7)));
			}
		}
		super.montarArvoreTipologiaMineracao(listaTipologiasEtapa7);
		if(edicao){
			listaFceLicenciamentoMineralSubstanciaMineral = facade.listarFceLicenciamentoMineralSubstanciaMineralBy(fceLicenciamentoMineral);
			for(FceLicenciamentoMineralSubstanciaMineralTipologia fceLicenciamentoMineralSubstanciaMineralObj : listaFceLicenciamentoMineralSubstanciaMineral){
				fceLicenciamentoMineralSubstanciaMineralObj.getSubstanciaMineralTipologia().getIdeTipologia().setValorAtividade(new BigDecimal(facade.obterValorDaAtividade(super.requerimento, fceLicenciamentoMineralSubstanciaMineralObj.getSubstanciaMineralTipologia().getIdeTipologia())));
			}
		}
	}

	@Override
	protected void carregarListaTipoOrigemEnergia() throws Exception {
		super.carregarListaTipoOrigemEnergia();
		if(edicao){
			super.listaTipoOrigemEnergiaSelected = facade.listarOrigemEnergiaSalvoBy(fceLicenciamentoMineral);
		}
	}

	private void carregarListalistaRegimeExploracao() throws Exception {
		listaRegimeExploracao = facade.listarRegimeExploracao();
		if(edicao){
			listaRegimeExploracaoSelected = facade.listarRegimeExploracaoSalvoBy(fceLicenciamentoMineral);
		}
	}

	private void carregarAbaQuadroAreas() {
		try{
			verificarLicencaPrevia();
		}
		catch(Exception e){
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + nomeAba());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			RequestContext.getCurrentInstance().execute("mineracaoLicenciamento.hide()");
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	private void carregarAbaCaracterizacaoAtividade() {
		try{
			carregarListaMetodoLavra();
			if(isBeneficiamentoProprio()) {
				if(super.isFceTecnico()) {
					carregarListaProducaoProduto();
				}
				carregarListaDisposicaoRejeito();
			}
			carregarListaServidaoMineraria();
			carregarListaTransporteMinerio();
			carregarListaMetodoRecuperacaoIntervencao();
			carregarListaTipoApp();
		}
		catch(Exception e){
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + nomeAba());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			RequestContext.getCurrentInstance().execute("mineracaoLicenciamento.hide()");
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	private void carregarListaDisposicaoRejeito() throws Exception {
		if(edicao){
			carregarDisposicaoRejeitos();
			listaFormaDisposicaoRejeito = facade.listarFormaDisposicaoRejeitoBy(fceLicenciamentoMineral);
		}
	}

	public void confirmarSubstanciaMineral(ActionEvent event) {
		fceLicenciamentoMineralSubstanciaMineral = (FceLicenciamentoMineralSubstanciaMineralTipologia) event.getComponent().getAttributes().get("substancia");
		if(isSubstanciaMineralValida(fceLicenciamentoMineralSubstanciaMineral) && !fceLicenciamentoMineralSubstanciaMineral.isOutros()){
			fceLicenciamentoMineralSubstanciaMineral.setConfirmado(true);
			if(!fceLicenciamentoMineralSubstanciaMineral.isEdicao()){
				super.exibirMensagem001();
			}
			else{
				super.exibirMensagem002();
			}
		}
	}

	private boolean isSubstanciaMineralValida(FceLicenciamentoMineralSubstanciaMineralTipologia fceLicenciamentoMineralSubstanciaMineral) {
		if(Util.isNullOuVazio(fceLicenciamentoMineralSubstanciaMineral.getNumProducaoAnual())){
			JsfUtil.addErrorMessage("A Produção Anual " + Util.getString("msg_generica_preenchimento_obrigatorio"));
			return false;
		}
		return true;
	}

	public void editarSubstanciaMineral(ActionEvent event) {
		fceLicenciamentoMineralSubstanciaMineral = (FceLicenciamentoMineralSubstanciaMineralTipologia) event.getComponent().getAttributes().get("substancia");
		fceLicenciamentoMineralSubstanciaMineral.setConfirmado(false);
		fceLicenciamentoMineralSubstanciaMineral.setEdicao();

	}

	@Override
	public void excluirSubstanciaMineral() {
		try {
			listaFceLicenciamentoMineralSubstanciaMineral.remove(fceLicenciamentoMineralSubstanciaMineral);
			facade.excluirFceLicenciamentoMineralSubstanciaMineral(fceLicenciamentoMineral, fceLicenciamentoMineralSubstanciaMineral.getSubstanciaMineralTipologia());
			super.exibirMensagem005();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_excluir") + " a substância mineral.");
		}
	}

	@Override
	public void incluirProcessoDnpm() {
		if(!Util.isNull(super.processoDnpm) && isPoligonalRequeridaDnpmSalva()){
			obterAreaProcessoDnpm();
		}
		super.processoDnpm = new ProcessoDnpm(fceLicenciamentoMineral);
	}

	/**
	 * Método que obtém a árae da poligonal requerida no processo DNPM.
	 *
	 * @author eduardo.fernandes
	 * @since 29/06/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 */
	private void obterAreaProcessoDnpm() {
		super.processoDnpm.setAreaProcessoDnpm(BigDecimal.valueOf(obterArea(super.processoDnpm.getIdeLocalizacaoGeografica())).setScale(4, RoundingMode.CEILING));
	}

	/**
	 *
	 * Método que obtém a área do <i>shapefile</i> persistido.
	 *
	 * @author eduardo.fernandes
	 * @since 30/06/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param localizacaoGeografica
	 * @return
	 */
	private Double obterArea(LocalizacaoGeografica localizacaoGeografica) {
		try{
			return facade.retornarAreaShape(localizacaoGeografica);
		}
		catch(Exception e){
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " a área do shape informado.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);

		}
	}

	public void excluirPoligonalRequeridaDnpm() {
		super.adicionarLocalizacaoGeograficaParaExclusao(super.processoDnpm.getIdeLocalizacaoGeografica());
		super.processoDnpm.setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
		super.exibirMensagem005();
	}

	@Override
	public List<ProcessoDnpm> getListaProcessoDnpm() {
		return fceLicenciamentoMineral.getListaProcessoDNPM();
	}

	@Override
	public void excluirProcessoDnpm() {
		try{
			excluirPoligonalRequeridaDnpm();
			super.excluirProcessoDnpm();
		}
		catch(Exception e){
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_excluir") + " o Processo DNPM.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public boolean isPoligonalRequeridaDnpmSalva() {
		return super.isLocalizacaoGeograficaSalva(super.processoDnpm.getIdeLocalizacaoGeografica());
	}

	public boolean isPoligonalAreaEfetivaLavraSalva() {
		return super.isLocalizacaoGeograficaSalva(fceLicenciamentoMineral.getIdeLocalizacaoGeograficaLavra()) /*&& !Util.isNullOuVazio(areaDeLavra)*/;
	}

	public void excluirPoligonalLavra() {
		super.adicionarLocalizacaoGeograficaParaExclusao(fceLicenciamentoMineral.getIdeLocalizacaoGeograficaLavra());
		fceLicenciamentoMineral.setIdeLocalizacaoGeograficaLavra(null);
		fceLicenciamentoMineral.setAreaDeLavra(null);
		super.exibirMensagem005();
	}


	public boolean isPoligonalAreaServidaoSalva() {
		return super.isLocalizacaoGeograficaSalva(fceLicenciamentoMineral.getIdeLocalizacaoGeograficaServidao()) /*&& !Util.isNullOuVazio(areaDeServidao)*/;
	}

	public void excluirPoligonalServidao() {
		super.adicionarLocalizacaoGeograficaParaExclusao(fceLicenciamentoMineral.getIdeLocalizacaoGeograficaServidao());
		fceLicenciamentoMineral.setIdeLocalizacaoGeograficaServidao(null);
		fceLicenciamentoMineral.setAreaDeServidao(null);
		super.exibirMensagem005();
	}

	/**
	 * Método que verifica se o Processo DNPM pode ser inserido.
	 *
	 * @author eduardo.fernandes
	 * @since 15/06/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @return
	 */
	@Override
	protected boolean isProcessoDnpmValido() {
		boolean valido = super.isProcessoDnpmValido();
		if(!super.isLocalizacaoGeograficaSalva(super.processoDnpm.getIdeLocalizacaoGeografica())){
			JsfUtil.addErrorMessage("A " + Util.getString("fce_lic_min_poligonal_requerida_dnpm") + " " + Util.getString("msg_generica_preenchimento_obrigatorio"));
			valido = false;
		} else {
			if(!Util.isNullOuVazio(getListaProcessoDnpm())){
				for(ProcessoDnpm processoDnpm : getListaProcessoDnpm()){
					try {
						if(!facade.naoExisteIntersecao(processoDnpm.getIdeLocalizacaoGeografica(), super.processoDnpm.getIdeLocalizacaoGeografica())){
							JsfUtil.addErrorMessage("A(s) poligonal(is) do Processo DNPM não podem se sobrepor.");
							valido = false;
							break;
						}
					} catch (Exception e) {
						Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
						valido = false;
						break;
					}
				}
			}
		}
		return valido;
	}

	public void incluirFormaDisposicaoRejeito(){
		try{
			limparFormaDisposicaoRejeito();
			carregarDisposicaoRejeitos();
		}
		catch(Exception e){
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " a forma de disposição de rejeitos.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	private void carregarDisposicaoRejeitos() throws Exception {
		if(Util.isNullOuVazio(listaTipoEstrutura)){
			carregarListaTipoEstrutura();
		}
		if(Util.isNullOuVazio(listaClassificacaoRejeitoDnpm)){
			carregarListaClassificacaoRejeitoDNPM();
		}
	}

	public void changeTipoEstrutura(ValueChangeEvent event){
		TipoEstrutura estrutura = (TipoEstrutura) event.getNewValue();
		if(!Util.isNull(estrutura) && estrutura.equals(new TipoEstrutura(7, "Outros"))){
			super.exibirInformacao001();
		}
	}


	private boolean isFormaDisposicaoRejeitoValida(){
		boolean valido = true;
		if(Util.isNullOuVazio(formaDisposicaoRejeito.getTipoEstrutura())){
			JsfUtil.addErrorMessage(Util.getString("msg_generica_selecione_um") + " um "+ Util.getString("fce_lic_min_tipo_estrutura") + ".");
			valido = false;
		}
		if(!formaDisposicaoRejeito.isNaoSeAplica() && !formaDisposicaoRejeito.isOutros()){
			if(Util.isNullOuVazio(formaDisposicaoRejeito.getAreaOcupada())){
				JsfUtil.addErrorMessage("A área ocupada " + Util.getString("msg_generica_null_ou_vazio"));
				valido = false;
			}
			if(Util.isNullOuVazio(formaDisposicaoRejeito.getTipoResiduoGerado())){
				JsfUtil.addErrorMessage(Util.getString("msg_generica_selecione_um") + " um " + Util.getString("fce_lic_min_classe_rejeito") + ".");
				valido = false;
			}
			if(!formaDisposicaoRejeito.isCavaExaurida() && Util.isNullOuVazio(formaDisposicaoRejeito.getIndSistemaImpermeabilizacao())){
				JsfUtil.addErrorMessage("O " + Util.getString("fce_lic_min_sistema_impermebealizado") + " " + Util.getString("msg_generica_preenchimento_obrigatorio"));
				valido = false;
			}
			if(!formaDisposicaoRejeito.isEmpilhamentoDrenado() && Util.isNullOuVazio(formaDisposicaoRejeito.getClassificacaoRejeitoDnpm())){
				JsfUtil.addErrorMessage(Util.getString("msg_generica_selecione_um") + " uma " + Util.getString("fce_lic_min_classificacao_portaria") + ".");
				valido = false;
			}
		}
		return valido;
	}

	public void salvarFormaDisposicaoRejeito(){
		if(isFormaDisposicaoRejeitoValida()){
			if(!isFormaDisposicaoRejeitoAdicionada()){
				listaFormaDisposicaoRejeito = new ArrayList<FormaDisposicaoRejeito>();
			}
			if(!listaFormaDisposicaoRejeito.contains(formaDisposicaoRejeito)){
				listaFormaDisposicaoRejeito.add(formaDisposicaoRejeito);
				super.exibirMensagem001();
			}
			else {
				super.exibirMensagem002();
				RequestContext.getCurrentInstance().execute("dialogIncluirFormaDisposicaoRejeito.hide()");
			}
			fecharDialogFormaDisposicaoRejeito();
		}
	}

	private boolean isExisteFormaDisposicaoRejeitoOutros() {
		if(isFormaDisposicaoRejeitoAdicionada()) {
			for(FormaDisposicaoRejeito formaDisposicaoRejeitoObj : listaFormaDisposicaoRejeito){
				if(formaDisposicaoRejeitoObj.isOutros()){
					return true;
				}
			}
		}
		return false;
	}

	public void excluirDisposicaoRejeito(){
		try {
			listaFormaDisposicaoRejeito.remove(formaDisposicaoRejeito);
			facade.excluirFormaDisposicaoRejeito(formaDisposicaoRejeito);
			super.exibirMensagem005();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}

	public boolean isFormaDisposicaoRejeitoAdicionada(){
		return !Util.isNullOuVazio(listaFormaDisposicaoRejeito);
	}

	public boolean isNecessarioInformarTecnicaLavra(){
		return !Util.isNullOuVazio(listaMetodoLavraSelected) &&
				(isMetodoLavraCeuAbertoSelected() || isMetodoLavraSubterraneaSelected());
	}

	private boolean isMetodoLavraCeuAbertoSelected(){
		return listaMetodoLavraSelected.contains(new MetodoLavra(MetodoLavraEnum.CEU_ABERTO));
	}

	private boolean isMetodoLavraSubterraneaSelected(){
		return listaMetodoLavraSelected.contains(new MetodoLavra(MetodoLavraEnum.SUBTERRANEA));
	}

	@SuppressWarnings("unchecked")
	public void changeTecnicaLavraCeuAberto(ValueChangeEvent event){
		List<TecnicaLavra> tlNew = (List<TecnicaLavra>) event.getNewValue();
		List<TecnicaLavra> tlOld = (List<TecnicaLavra>) event.getOldValue();
		if(!Util.isNullOuVazio(tlNew)){
			if(tlNew.contains(new TecnicaLavra(TecnicaLavraEnum.DRAGAGEM)) && isTecnicaLavraDragagemNull()){
				carregarListaTecnicaLavraCeuAbertoDragagem();
			}
			else {
				limparTecnicaLavraDragagemSelected();
			}
			verificarTecnicaLavraOutros(tlNew, tlOld);
		}
	}

	public boolean isTecnicaLavraDragagemSelected(){
		return !Util.isNullOuVazio(listaTecnicaLavraCeuAbertoSelected) && listaTecnicaLavraCeuAbertoSelected.contains(new TecnicaLavra(TecnicaLavraEnum.DRAGAGEM));
	}

	private boolean isTecnicaLavraDragagemNull() {
		return Util.isNullOuVazio(listaTecnicaLavraCeuAbertoDragagem);
	}

	@SuppressWarnings("unchecked")
	public void changeTecnicaLavraSubterranea(ValueChangeEvent event){
		List<TecnicaLavra> tlNew = (List<TecnicaLavra>) event.getNewValue();
		List<TecnicaLavra> tlOld = (List<TecnicaLavra>) event.getOldValue();
		if(!Util.isNullOuVazio(tlNew)){
			verificarTecnicaLavraOutros(tlNew, tlOld);
		}
	}

	private void verificarTecnicaLavraOutros(List<TecnicaLavra> tlNew, List<TecnicaLavra> tlOld) {
		TecnicaLavra outros = new TecnicaLavra(TecnicaLavraEnum.OUTROS);
		if(tlNew.contains(outros)){
			if(Util.isNullOuVazio(tlOld) || !tlOld.contains(outros)){
				super.exibirInformacao001();
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void changeMetodoLavra(ValueChangeEvent event){
		List<MetodoLavra> mlNew = (List<MetodoLavra>) event.getNewValue();
		if(!Util.isNullOuVazio(mlNew)){
			if(mlNew.contains(new MetodoLavra(MetodoLavraEnum.CEU_ABERTO)) && isListaTecnicaLavraCeuAbertoNull()){
				carregarListaTecnicaLavraCeuAberto();
			}
			else if(!mlNew.contains(new MetodoLavra(MetodoLavraEnum.CEU_ABERTO))){
				limparTecnicaLavraCeuAbertoSelected();
			}
			if(mlNew.contains(new MetodoLavra(MetodoLavraEnum.SUBTERRANEA)) && isListaTecnicaLavraSubterraneaNull()){
				carregarListaTecnicaLavraSubterranea();
			}
			else if(!mlNew.contains(new MetodoLavra(MetodoLavraEnum.SUBTERRANEA))){
				limparTecnicaLavraSubterraneaSelected();
			}
		}
		else {
			limparTecnicaLavraCeuAbertoSelected();
			limparTecnicaLavraSubterraneaSelected();
		}
	}

	//
	private boolean isExisteMetodoLavraOutros() {
		return false;
	}

	@SuppressWarnings("unchecked")
	public void changeServidaoMineraria(ValueChangeEvent changeEvent){
		List<ServidaoMineraria> smOld = ((List<ServidaoMineraria>) changeEvent.getOldValue());
		List<ServidaoMineraria> smNew = ((List<ServidaoMineraria>) changeEvent.getNewValue());
		if(!Util.isNullOuVazio(smNew)){
			ServidaoMineraria outros = new ServidaoMineraria(14, "Outras");
			if(smNew.contains(outros)){
				if(Util.isNullOuVazio(smOld) || !smOld.contains(outros)){
					super.exibirInformacao001();
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void changeMetodoRecuperacaoIntervencao(ValueChangeEvent changeEvent){
		List<MetodoRecuperacaoIntervencao> mriOld = ((List<MetodoRecuperacaoIntervencao>) changeEvent.getOldValue());
		List<MetodoRecuperacaoIntervencao> mriNew = ((List<MetodoRecuperacaoIntervencao>) changeEvent.getNewValue());
		if(!Util.isNullOuVazio(mriNew)){
			MetodoRecuperacaoIntervencao outros = new MetodoRecuperacaoIntervencao(3, "Outros");
			if(mriNew.contains(outros)){
				if(Util.isNullOuVazio(mriOld) || !mriOld.contains(outros)){
					super.exibirInformacao001();
				}
			}
		}
	}

	private void carregarListaTecnicaLavraCeuAberto() {
		try{
			listaTecnicaLavraCeuAberto = facade.listarTecnicaLavraByMetodoLavra(new MetodoLavra(MetodoLavraEnum.CEU_ABERTO));
		}
		catch(Exception e){
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " as técnicas de lavra à céu aberto.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	private void carregarListaTecnicaLavraCeuAbertoDragagem() {
		try{
			listaTecnicaLavraCeuAbertoDragagem = facade.listarTecnicaLavraByTecnicaLavra(new TecnicaLavra(TecnicaLavraEnum.DRAGAGEM));
		}
		catch(Exception e){
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " as técnicas de lavra à céu aberto.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public boolean isListaTecnicaLavraCeuAbertoNull() {
		return Util.isNullOuVazio(listaTecnicaLavraCeuAberto);
	}

	private void carregarListaTecnicaLavraSubterranea(){
		try{
			listaTecnicaLavraSubterranea = facade.listarTecnicaLavraByMetodoLavra(new MetodoLavra(MetodoLavraEnum.SUBTERRANEA));
		}
		catch(Exception e){
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " as técnicas de lavra subterrânea.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public boolean isListaTecnicaLavraSubterraneaNull() {
		return Util.isNullOuVazio(listaTecnicaLavraSubterranea);
	}

	private void carregarListaTipoEstrutura() throws Exception {
		listaTipoEstrutura = facade.listarTipoEstrutura();
	}

	private void carregarListaClassificacaoRejeitoDNPM() throws Exception {
		listaClassificacaoRejeitoDnpm = facade.listarClassificacaoRejeito();
	}

	private void carregarListaServidaoMineraria() throws Exception {
		listaServidaoMineraria = facade.listarServidaoMineraria();
		if(edicao){
			listaFceLicenciamentoMineralServidaoMineraria = facade.listarFceLicenciamentoMineralServidaoMinerariaBy(fceLicenciamentoMineral);
			if(!Util.isNullOuVazio(listaFceLicenciamentoMineralServidaoMineraria)){
				for(FceLicenciamentoMineralServidaoMineraria fceLicenciamentoMineralServidaoMinerariaObj : listaFceLicenciamentoMineralServidaoMineraria){
					listaServidaoMineraria.remove(fceLicenciamentoMineralServidaoMinerariaObj.getServidaoMineraria());
				}
			}
		}
		ordernarServidaoMineraria();
	}
	
	private void ordernarServidaoMineraria() {
		Collections.sort(listaServidaoMineraria, new Comparator<ServidaoMineraria>(){
			@Override
			public int compare(ServidaoMineraria o1, ServidaoMineraria o2) {
				return o1.getNomServidaoMineraria().compareTo(o2.getNomServidaoMineraria());
			}
		});
		
		for(ServidaoMineraria sm : listaServidaoMineraria){
	      if("Outras".equalsIgnoreCase(sm.getNomServidaoMineraria())){
	    	  ServidaoMineraria smOutros = sm;
	    	  listaServidaoMineraria.remove(sm);
	    	  listaServidaoMineraria.add(smOutros);
	        return;
	      }
	    }
	}	

	
	private void carregarListaTransporteMinerio() throws Exception {
		listaTipoTransporteMinerio = facade.listarTransporteMinerio();
		if(edicao){
			listaTipoTransporteMinerioSelected = facade.listarTipoTransporteMinerioSalvoBy(fceLicenciamentoMineral);
		}
	}

	protected void carregarListaMetodoRecuperacaoIntervencao() throws Exception {
		super.carregarListaMetodoRecuperacaoIntervencao(true);
		if(edicao){
			listaMetodoRecuperacaoIntervencaoSelected = facade.listarMetodoRecuperacaoIntervencaoSalvoBy(fceLicenciamentoMineral);
		}
	}

	private void carregarListaMetodoLavra() throws Exception {
		listaMetodoLavra = facade.listarMetodoLavra();
		if(edicao){
			listaMetodoLavraSelected = facade.listarMetodoLavraSalvoBy(fceLicenciamentoMineral);
			if(isMetodoLavraCeuAbertoSelected() || isMetodoLavraSubterraneaSelected()){
				List<TecnicaLavra> listaTemp = facade.listarTecnicaLavraSalvoBy(fceLicenciamentoMineral);
				for(TecnicaLavra tl : listaTemp){
					if(!Util.isNull(tl.getMetodoLavra())){
						if(tl.getMetodoLavra().equals(new MetodoLavra(MetodoLavraEnum.CEU_ABERTO))){
							carregarListaTecnicaLavraCeuAberto();
							if(Util.isNullOuVazio(tl.getIdeTecnicaLavraPai())){
								if(Util.isNullOuVazio(listaTecnicaLavraCeuAbertoSelected)){
									listaTecnicaLavraCeuAbertoSelected = new ArrayList<TecnicaLavra>();
								}
								listaTecnicaLavraCeuAbertoSelected.add(tl);
							}
						}
						else if(tl.getMetodoLavra().equals(new MetodoLavra(MetodoLavraEnum.SUBTERRANEA))){
							carregarListaTecnicaLavraSubterranea();
							if(Util.isNullOuVazio(listaTecnicaLavraSubterraneaSelected)){
								listaTecnicaLavraSubterraneaSelected = new ArrayList<TecnicaLavra>();
							}
							listaTecnicaLavraSubterraneaSelected.add(tl);
						}
					}
					else{
						if(!Util.isNullOuVazio(tl.getIdeTecnicaLavraPai())){
							carregarListaTecnicaLavraCeuAbertoDragagem();
							if(Util.isNullOuVazio(listaTecnicaLavraCeuAbertoDragagemSelected)){
								listaTecnicaLavraCeuAbertoDragagemSelected = new ArrayList<TecnicaLavra>();
							}
							listaTecnicaLavraCeuAbertoDragagemSelected.add(tl);
						}
					}
				}
			}
		}
	}

	private void carregarListaTipoApp() throws Exception {
		listaTipoApp = facade.listarApps();
		if(edicao){
			listaFceLicenciamentoMineralTipoApp = facade.listarFceLicenciamentoMineralTipoAppSalvoBy(fceLicenciamentoMineral);
			if(!Util.isNullOuVazio(listaFceLicenciamentoMineralTipoApp)){
				for(FceLicenciamentoMineralTipoApp fceLicenciamentoMineralTipoAppObj : listaFceLicenciamentoMineralTipoApp){
					listaTipoApp.remove(fceLicenciamentoMineralTipoAppObj.getTipoApp());
				}
			}			
		}
		ordernarListaTipoApp();
	}


	private void ordernarListaTipoApp() {
		Collections.sort(listaTipoApp, new Comparator<TipoApp>(){
			@Override
			public int compare(TipoApp o1, TipoApp o2) {
				return o1.getDscTipoApp().compareTo(o2.getDscTipoApp());
			}
		});
		

	}
	

	@Override
	protected void carregarAbaAspectosAmbientais() {
		try{
			carregarListaSistemaTratamento();
			carregarListaEmissaoAtmosferica();
			carregarListaTipoResiduoGerado();
			carregarListaDestinoResiduo();
		}
		catch(Exception e){
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + nomeAba());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			RequestContext.getCurrentInstance().execute("mineracaoLicenciamento.hide()");
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}

	}

	private void carregarListaSistemaTratamento() throws Exception {
		listaTipoSistemaTratamento = facade.listarTipoSistemaTratamento();
		if(edicao){
			listaTipoSistemaTratamentoSelected = facade.listarTipoSistemaTratamentoSalvoBy(fceLicenciamentoMineral);
		}
	}

	private void carregarListaEmissaoAtmosferica() throws Exception {
		listaTipoEmissaoAtmosferica = facade.listarTipoEmissaoAtmosferica();
		if(edicao){
			listaTipoEmissaoAtmosfericaSelected = facade.listaTipoEmissaoAtmosfericaSalvoBy(fceLicenciamentoMineral);
		}
	}

	@Override
	protected void carregarListaTipoResiduoGerado() throws Exception {
		super.carregarListaTipoResiduoGerado();
		if(edicao){
			super.listaTipoResiduoGeradoSelected = facade.listarTipoResiduoGeradoSalvoBy(fceLicenciamentoMineral);
		}
	}

	@Override
	protected void carregarListaDestinoResiduo() throws Exception {
		super.carregarListaDestinoResiduo();
		if(edicao){
			super.listaDestinoResiduoSelected = facade.listarDestinoResiduoSalvoBy(fceLicenciamentoMineral);
		}
	}

	@SuppressWarnings("unchecked")
	public void changeSistemaTratamento(ValueChangeEvent changeEvent) {
		List<TipoSistemaTratamento> tstOld = ((List<TipoSistemaTratamento>) changeEvent.getOldValue());
		List<TipoSistemaTratamento> tstNew = ((List<TipoSistemaTratamento>) changeEvent.getNewValue());
		if(!Util.isNullOuVazio(tstNew)){
			TipoSistemaTratamento outros = new TipoSistemaTratamento(6, "Outros");
			if(tstNew.contains(outros)){
				if(Util.isNullOuVazio(tstOld) || !tstOld.contains(outros)){
					super.exibirInformacao001();
				}
			}
		}
	}

	@Override
	public void finalizar() {
		try{
			facade.finalizar(this);
		}
		catch(Exception e){
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_salvar") + nomeAba());
			RequestContext.getCurrentInstance().execute("mineracaoLicenciamento.hide();");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);

		}
	}
	
	public void prepararParaFinalizar() throws Exception{
		boolean podeFinalizar = true;
		activeTab = 0;
		while(activeTab <= 3){
			if(validarAba()){
				salvarAba();
				activeTab++;
			}
			else{
				podeFinalizar = false;
				RequestContext.getCurrentInstance().addPartialUpdateTarget("tabLicMineracao");
				break;
			}
		}
		if(podeFinalizar){
			super.concluirFce();
			fecharDialog();
			if(!edicao){
				super.exibirMensagem001();
			}
			else{
				super.exibirMensagem002();
			}
			super.excluirLocalizacaoGeografica();
			limpar();
			super.limparFce();
		}
	}

	/**
	 * Método que verifica se existe algum item "Outros" selecionado de qualquer
	 * lista em qualquer uma das abas.
	 *
	 * @author eduardo.fernandes
	 * @since 30/06/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @return
	 */
	private boolean existeAlgumOutros() {
		return super.isExisteOrigemEnergiaOutros() || isExisteMetodoLavraOutros() || isExisteFormaDisposicaoRejeitoOutros() || isExisteMetodoRecuperacaoIntervencaoOutros() || isExisteTipoSistemaTratamentoOutros()
				|| super.isExisteDestinoResiduoOutros() || isExisteProdutoConcentradoOutros();
	}

	private boolean isExisteProdutoConcentradoOutros() {
		if(super.isFceTecnico() && isConcentradoProdutoAdicionado()){
			for(FceLicenciamentoMineralProducaoProduto producaoProdutoObj : listaFceLicenciamentoMineralProducaoProduto){
				if(producaoProdutoObj.isOutros()){
					return true;
				}
			}
		}
		return false;
	}

	private boolean isExisteTipoSistemaTratamentoOutros() {
		return listaTipoSistemaTratamentoSelected.contains(new TipoSistemaTratamento(6, "Outros"));

	}

	private boolean isExisteMetodoRecuperacaoIntervencaoOutros() {
		return listaMetodoRecuperacaoIntervencaoSelected.contains(new MetodoRecuperacaoIntervencao(3, "Outros"));
	}

	public StreamedContent getImprimirRelatorio() {
		try{
			return super.getImprimirRelatorio(super.fce, DOC_LICENCIAMENTO_MINERACAO);
		}
		catch(Exception e){
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_imprimir_relatorio") + " do FCE - Licenciamento para Mineração.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@Override
	public void limpar() {
		super.limpar();
		fceLicenciamentoMineral = null;
		licencaPrevia = false;
		edicao = false;
		limparAbaDadosGerais();
		limparAbaCaracterizacaoAtividades();
		limparAbaAspectosAmbientais();
	}

	/**
	 * Método que limpa a aba de <b>Dados Gerais</b>.
	 *
	 * @author eduardo.fernandes
	 * @since 15/06/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 */
	private void limparAbaDadosGerais() {
		limparListaSubstanciaMineral();
		limparRegimeExploracao();
		// limpar OUTORGA
		super.limparListaOutorgaMineracao();
		super.limparListaTipoOrigemEnergia();
	}

	protected void limparListaSubstanciaMineral() {
		super.limparListaSubstanciaMineral();
		listaFceLicenciamentoMineralSubstanciaMineral = null;
		fceLicenciamentoMineralSubstanciaMineral = null;
	}

	private void limparListaTecnicaLavraSubterranea() {
		listaTecnicaLavraSubterranea = null;
		limparTecnicaLavraSubterraneaSelected();
	}

	private void limparTecnicaLavraSubterraneaSelected() {
		listaTecnicaLavraSubterraneaSelected = null;
	}

	private void limparListaTecnicaLavraCeuAberto() {
		listaTecnicaLavraCeuAberto = null;
		limparTecnicaLavraCeuAbertoSelected();
	}

	private void limparTecnicaLavraCeuAbertoSelected() {
		listaTecnicaLavraCeuAbertoSelected = null;
	}

	private void limparListaTecnicaLavraCeuAbertoDragagem() {
		listaTecnicaLavraCeuAbertoDragagem = null;
		limparTecnicaLavraDragagemSelected();
	}

	private void limparTecnicaLavraDragagemSelected() {
		listaTecnicaLavraCeuAbertoDragagemSelected = null;
	}

	public void limparFormaDisposicaoRejeito() {
		formaDisposicaoRejeito = new FormaDisposicaoRejeito(fceLicenciamentoMineral);
	}

	/**
	 * Método que limpa a aba de <b>Caracterização da(s) Atividade(s)</b>.
	 *
	 * @author eduardo.fernandes
	 * @since 15/06/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 */
	private void limparAbaCaracterizacaoAtividades() {
		limparListaMetodoLavra();
		limparListaTecnicaLavraCeuAberto();
		limparListaTecnicaLavraCeuAbertoDragagem();
		limparListaTecnicaLavraSubterranea();
		limparFormaDisposicaoDeRejeito();
		limparListaServidaoMineraria();
		limparListaTransporteMinerio();
		limparListaMetodoRecuperacaoIntervencao();
		limparListaTipoApp();
	}

	private void limparListaTipoApp() {
		listaTipoApp = null;
		tipoApp = null;
		listaFceLicenciamentoMineralTipoApp = null;
		fceLicenciamentoMineralTipoApp = null;
	}

	private void limparListaServidaoMineraria() {
		listaTipoEstrutura = null;
		servidaoMineraria = null;
		listaServidaoMineraria = null;
		fceLicenciamentoMineralServidaoMineraria = null;
		listaFceLicenciamentoMineralServidaoMineraria = null;
		listaClassificacaoRejeitoDnpm = null;
	}

	private void limparListaTransporteMinerio() {
		listaTipoTransporteMinerio = null;
		listaTipoTransporteMinerioSelected = null;
	}

	private void limparListaMetodoRecuperacaoIntervencao() {
		listaMetodoRecuperacaoIntervencao = null;
		listaMetodoRecuperacaoIntervencaoSelected = null;
	}

	private void limparListaMetodoLavra() {
		listaMetodoLavra = null;
		listaMetodoLavraSelected = null;
	}

	private void limparFormaDisposicaoDeRejeito() {
		formaDisposicaoRejeito = null;
		listaFormaDisposicaoRejeito = null;
	}

	/**
	 * Método que limpa a aba de <b>Aspectos Ambientais</b>.
	 *
	 * @author eduardo.fernandes
	 * @since 15/06/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 */
	private void limparAbaAspectosAmbientais() {
		limparTipoSistemaTratamento();
		limparListaTipoEmissaoAtmosfericaSelected();
		super.limparListaDestinoResiduo();
		super.limparListaTipoResiduoGerado();
	}

	private void limparListaTipoEmissaoAtmosfericaSelected() {
		listaTipoEmissaoAtmosferica = null;
		listaTipoEmissaoAtmosfericaSelected = null;
	}

	private void limparTipoSistemaTratamento() {
		listaTipoSistemaTratamento = null;
		listaTipoSistemaTratamentoSelected = null;
	}

	private void limparRegimeExploracao() {
		listaRegimeExploracao = null;
		listaRegimeExploracaoSelected = null;
	}

	private String nomeAba() {
		String aba = " a aba ";
		if(isAbaDadosGerais()){
			aba = aba.concat(Util.getString("fce_lic_min_aba_dados_gerais"));
		}
		else if(isAbaQuadroDeAreas()){
			aba = aba.concat(Util.getString("fce_lic_min_aba_quadro_de_areas"));
		}
		else if(isAbaCaracterizacaoDeAtividades()){
			aba = aba.concat(Util.getString("fce_lic_min_aba_caracterizacao_da_atividade"));
		}
		else if(isAbaAspectosAmbientais()){
			aba = aba.concat(Util.getString("fce_lic_min_aba_aspectos_ambientais"));
		}
		return aba = aba.concat(".");
	}

	private boolean isAbaDadosGerais() {
		return activeTab == ABA_DADOS_GERAIS;
	}

	private boolean isAbaQuadroDeAreas() {
		return activeTab == ABA_QUADRO_AREAS;
	}

	private boolean isAbaCaracterizacaoDeAtividades() {
		return activeTab == ABA_CARACTERIZACAO_ATIVIDADES;
	}

	private boolean isAbaAspectosAmbientais() {
		return activeTab == ABA_ASPECTOS_AMBIENTAIS;
	}

	@Override
	public boolean validarAba() {
		if(isAbaDadosGerais()){
			return validarAbaDadosGerais();
		}
		else if(isAbaQuadroDeAreas()){
			return validarAbaQuadroDeAreas();
		}
		else if(isAbaCaracterizacaoDeAtividades()){
			return validarAbaCaracterizacaoDeAtividades();
		}
		else if(isAbaAspectosAmbientais()){
			return validarAbaAspectosAmbientais();
		}
		return false;
	}

	/**
	 * Método que verifica se os campos obrigatórios da Aba estão preenchidos.
	 *
	 * @author eduardo.fernandes
	 * @since 15/06/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @return
	 */
	private boolean validarAbaDadosGerais() {
		boolean valido = true;
		if(Util.isNullOuVazio(listaFceLicenciamentoMineralSubstanciaMineral)){
			JsfUtil.addErrorMessage("A " + Util.getString("fce_lic_min_substancia_mineral") + " " + Util.getString("msg_generica_preenchimento_obrigatorio"));
			valido = false;
		} 
		else if(!super.isFceTecnico()){
			List<Tipologia> tipologias = new ArrayList<Tipologia>();
			tipologias.addAll(listaTipologiasEtapa7);
			
			Map<String, Double> somatorioProducaoAnual = new HashMap<String, Double>();
			boolean verificarSomatorios = true;
			for(FceLicenciamentoMineralSubstanciaMineralTipologia substanciaMineral : listaFceLicenciamentoMineralSubstanciaMineral){
				if(!substanciaMineral.isOutros()){
					if(!somatorioProducaoAnual.containsKey(substanciaMineral.getSubstanciaMineralTipologia().getIdeTipologia().getCodTipologia())){
						somatorioProducaoAnual.put(substanciaMineral.getSubstanciaMineralTipologia().getIdeTipologia().getCodTipologia(), substanciaMineral.getNumProducaoAnual());
					}
					else{
						Double producaoAnual = somatorioProducaoAnual.get(substanciaMineral.getSubstanciaMineralTipologia().getIdeTipologia().getCodTipologia());
						producaoAnual += substanciaMineral.getNumProducaoAnual();
						somatorioProducaoAnual.put(substanciaMineral.getSubstanciaMineralTipologia().getIdeTipologia().getCodTipologia(), producaoAnual);
					}
				}
				else {
					verificarSomatorios = false;
				}
				if(listaTipologiasEtapa7.contains(substanciaMineral.getSubstanciaMineralTipologia().getIdeTipologia())){
					tipologias.remove(substanciaMineral.getSubstanciaMineralTipologia().getIdeTipologia());
				}
			}
			// RN 00168
			if(verificarSomatorios){
				Map<String, Double> somatorioDasAtividades = new HashMap<String, Double>();
				for(Tipologia tipologia : listaTipologiasEtapa7){
					somatorioDasAtividades.put(tipologia.getCodTipologia(), tipologia.getValorAtividade().doubleValue());
				}
				for(Map.Entry<String, Double> substanciaMineral : somatorioProducaoAnual.entrySet()){
					if(somatorioDasAtividades.containsKey(substanciaMineral.getKey())){
						Double valAtividadeEtapa7 = somatorioDasAtividades.get(substanciaMineral.getKey());
						Double somaProducaoAnual = substanciaMineral.getValue();
						if(!valAtividadeEtapa7.equals(somaProducaoAnual)){
							JsfUtil.addErrorMessage("A soma das produções informadas não pode ser diferente do porte identificado para cada tipologia do requerimento.");
							Log4jUtil.log(this.getClass().getName(), Level.INFO, "Somatório da ETAPA 7: " + valAtividadeEtapa7);
							valido = false;
							break;
						}
					}

				}
			}
			// RN 00167
			if(!Util.isNullOuVazio(tipologias)){
				JsfUtil.addErrorMessage(Util.getString("msg_generica_selecione_um") + " uma substância mineral para cada tipologia informada no requerimento.");
				valido = false;
			}
		}
		if(Util.isNull(fceLicenciamentoMineral.getIndPesqintervRecursoHidrico())){
			JsfUtil.addErrorMessage("A intervenção em corpo hídrico " + Util.getString("msg_generica_preenchimento_obrigatorio"));
			valido = false;
		}
		else if(fceLicenciamentoMineral.getIndPesqintervRecursoHidrico()){
			if((Util.isNull(fceLicenciamentoMineral.getIndEsferaEstadual()) && Util.isNull(fceLicenciamentoMineral.getIndEsferaFederal())) 
					|| (!fceLicenciamentoMineral.getIndEsferaEstadual() && !fceLicenciamentoMineral.getIndEsferaFederal())){
				JsfUtil.addErrorMessage(Util.getString("msg_generica_selecione_um") + " uma intervenção em corpo hídrico.");
				valido = false;
			}
		}
		if(Util.isNullOuVazio(listaRegimeExploracaoSelected)){
			JsfUtil.addErrorMessage(Util.getString("msg_generica_selecione_um") + " um " + Util.getString("fce_lic_min_regime_exploracao") + ".");
			valido = false;
		}
		if(Util.isNullOuVazio(super.listaTipoOrigemEnergiaSelected)){
			JsfUtil.addErrorMessage(Util.getString("msg_generica_selecione_um") + " um " + Util.getString("fce_lic_min_suprimento_energia") + ".");
			valido = false;
		}
		return valido;
	}

	/**
	 * Método que verifica se os campos obrigatórios da Aba estão preenchidos.
	 *
	 * @author eduardo.fernandes
	 * @since 15/06/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @return
	 */
	private boolean validarAbaQuadroDeAreas() {
		boolean valido = true;
		if(!isProcessoDnpmAdicionado()){
			JsfUtil.addErrorMessage("O processo DNPM " + Util.getString("msg_generica_preenchimento_obrigatorio"));
			valido = false;
		}
		if(!isPoligonalAreaEfetivaLavraSalva()){
			JsfUtil.addErrorMessage("A(s) " + Util.getString("fce_lic_min_poligonais_lavra") + " " + Util.getString("msg_generica_preenchimento_obrigatorio"));
			valido = false;
		}
		else{
			if(isProcessoDnpmAdicionado() && !isPoligonalLavraValida()){
				JsfUtil.addErrorMessage(Util.getString("fce_lic_min_valida_poligonal_lavra"));
				valido = false;
			}
		}
		if(!licencaPrevia && !isPoligonalAreaServidaoSalva()){
			JsfUtil.addErrorMessage("A(s) " + Util.getString("fce_lic_min_poligonais_servidao") + " " + Util.getString("msg_generica_preenchimento_obrigatorio"));
			valido = false;
		}
		return valido;
	}

	/**
	 * <li><b>RN 00170 - Poligonal da área efetiva de lavra</b></li>
	 * <p>
	 * Cadastro obrigatório de um arquivo shapefile contendo uma ou mais
	 * poligonais da área efetiva de lavra. No caso do shape conter mais de uma
	 * poligonal, elas devem estar em sua totalidade no estado da Bahia e não
	 * podem se sobrepor. A área de lavra deve estar dentro de algum dos shapes
	 * da DNPM inseridos.
	 * </p>
	 *
	 * @author eduardo.fernandes
	 * @since 30/06/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param ideLocalizacaoGeograficaLavra
	 * @return
	 * @throws Exception
	 */
	private boolean isPoligonalLavraValida() {
		for(ProcessoDnpm processo : getListaProcessoDnpm()){
			try{
				if(facade.isPoligonalContidaEmOutraPoligonal(processo.getIdeLocalizacaoGeografica(), fceLicenciamentoMineral.getIdeLocalizacaoGeograficaLavra())){
					return true;
				}

			}
			catch(Exception e){
				JsfUtil.addErrorMessage(Util.getString("")); // ADICIONAR
				// MSG DE ERRO
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e, Util.SEIA_EXCEPTION);

			}
		}
		return false;
	}

	private void verificarLicencaPrevia() {
		try{
			licencaPrevia = facade.isEnquadramentoLP(super.requerimento);
		}
		catch(Exception e){
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " as informações do Enquadramento.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);

		}
	}

	/**
	 * Método que verifica se os campos obrigatórios da Aba estão preenchidos.
	 *
	 * @author eduardo.fernandes
	 * @since 15/06/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @return
	 */
	private boolean validarAbaCaracterizacaoDeAtividades() {
		boolean valido = true;
		if(Util.isNullOuVazio(listaMetodoLavraSelected)){
			JsfUtil.addErrorMessage(Util.getString("msg_generica_selecione_um") + " um " + Util.getString("fce_lic_min_metodo_lavra") + ".");
			valido = false;
		}
		else{
			if(isMetodoLavraCeuAbertoSelected()){
				if(Util.isNullOuVazio(listaTecnicaLavraCeuAbertoSelected)){
					JsfUtil.addErrorMessage(Util.getString("msg_generica_selecione_um") + " uma técnica utilizada para desenvolvimento da lavra à céu aberto.");
					valido = false;
				}
				else if(isTecnicaLavraDragagemSelected() && Util.isNullOuVazio(listaTecnicaLavraCeuAbertoDragagemSelected)){
					JsfUtil.addErrorMessage(Util.getString("msg_generica_selecione_um") + " uma técnica utilizada para desenvolvimento da lavra à céu aberto em dragagem.");
					valido = false;
				}
			}
			if(isMetodoLavraSubterraneaSelected() && Util.isNullOuVazio(listaTecnicaLavraSubterraneaSelected)){
				JsfUtil.addErrorMessage(Util.getString("msg_generica_selecione_um") + " uma técnica utilizada para desenvolvimento da lavra subterrânea.");
				valido = false;
			}
		}
		if(Util.isNullOuVazio(fceLicenciamentoMineral.getIndExplosivos())){
			JsfUtil.addErrorMessage("O campo '" + Util.getString("fce_lic_min_utiliza_explosivo") + "' "+ Util.getString("msg_generica_preenchimento_obrigatorio"));
			valido = false;
		}
		if(Util.isNullOuVazio(fceLicenciamentoMineral.getIndBeneficiamentoMineracao())){
			JsfUtil.addErrorMessage("O campo '" + Util.getString("fce_lic_min_beneficiamento_proprio") + "' "+ Util.getString("msg_generica_preenchimento_obrigatorio"));
			valido = false;
		}
		else if(fceLicenciamentoMineral.getIndBeneficiamentoMineracao()) {
			if(super.isFceTecnico()) {
				if(!isConcentradoProdutoAdicionado()) {
					JsfUtil.addErrorMessage("O " + Util.getString("fce_lic_min_concentrado_produto") + " " + Util.getString("msg_generica_preenchimento_obrigatorio"));
					valido = false;
				}
				else {
					for(FceLicenciamentoMineralProducaoProduto producaoProdutoObj : listaFceLicenciamentoMineralProducaoProduto) {
						if(!producaoProdutoObj.isConfirmado()) {
							JsfUtil.addErrorMessage(Util.getString("msg_generica_necessario_confirmar_inf_0040") + " o " + Util.getString("fce_lic_min_concentrado_produto") + ".");
							valido = false;
							break;
						}
					}
				}
			}
			if(!licencaPrevia && !isFormaDisposicaoRejeitoAdicionada()){
				JsfUtil.addErrorMessage("A " + Util.getString("fce_lic_min_disposicao_rejeito") + " " + Util.getString("msg_generica_preenchimento_obrigatorio"));
				valido = false;
			}
		}
		if(!isServidaoMinerariaAdicionada()){
			JsfUtil.addErrorMessage("A " + Util.getString("fce_lic_min_area_servidao") + " " + Util.getString("msg_generica_preenchimento_obrigatorio"));
			valido = false;
		} else {
			for(FceLicenciamentoMineralServidaoMineraria fceLicenciamentoMineralServidaoMinerariaObj : listaFceLicenciamentoMineralServidaoMineraria){
				if(!fceLicenciamentoMineralServidaoMinerariaObj.isConfirmado()){
					JsfUtil.addErrorMessage(Util.getString("msg_generica_necessario_confirmar_inf_0040") + " a " + Util.getString("fce_lic_min_servidao_mineraria") + ".");
					valido = false;
					break;
				}
			}
		}
		if(Util.isNullOuVazio(listaTipoTransporteMinerioSelected)){
			JsfUtil.addErrorMessage(Util.getString("msg_generica_selecione_um") + " uma " + Util.getString("fce_lic_min_logistica_transporte") + ".");
			valido = false;
		}
		if(Util.isNullOuVazio(listaMetodoRecuperacaoIntervencaoSelected)){
			JsfUtil.addErrorMessage(Util.getString("msg_generica_selecione_um") + " uma " + Util.getString("fce_lic_min_recuperacao_intervencao") + ".");
			valido = false;
		}
		if(Util.isNullOuVazio(fceLicenciamentoMineral.getIndApp())){
			JsfUtil.addErrorMessage("APP " + Util.getString("msg_generica_preenchimento_obrigatorio"));
			valido = false;
		}
		else if(fceLicenciamentoMineral.getIndApp() && !isAppAdicionada()){
			JsfUtil.addErrorMessage(Util.getString("msg_generica_selecione_um") + " um APP.");
			valido = false;
		}
		return valido;
	}

	/**
	 * Método que verifica se os campos obrigatórios da Aba estão preenchidos.
	 *
	 * @author eduardo.fernandes
	 * @since 15/06/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @return
	 */
	@Override
	protected boolean validarAbaAspectosAmbientais() {
		boolean valido = true;
		if(Util.isNullOuVazio(listaTipoSistemaTratamentoSelected)){
			JsfUtil.addErrorMessage(Util.getString("msg_generica_selecione_um") + " uma " + Util.getString("fce_lic_min_sistema_tratamento") + ".");
			valido = false;
		}
		if(Util.isNullOuVazio(listaTipoEmissaoAtmosfericaSelected)){
			JsfUtil.addErrorMessage(Util.getString("msg_generica_selecione_um") + " uma " + Util.getString("fce_lic_min_emissoes_atmosfericas") + ".");
			valido = false;
		}
		if(!super.validarAbaAspectosAmbientais()){
			valido = false;
		}
		return valido;
	}

	@Override
	public void abrirDialog() {
		RequestContext.getCurrentInstance().execute("mineracaoLicenciamento.show();");
		atualizarDialog();
	}
	
	protected void fecharDialogFormaDisposicaoRejeito() {
		RequestContext.getCurrentInstance().execute("dialogIncluirFormaDisposicaoRejeito.hide()");
	}
	
	@Override
	protected void fecharDialogProcessoDNPM() {
		RequestContext.getCurrentInstance().execute("dialogIncluirProcessoDNPM.hide()");
	}

	/**
	 * Método que fecha o <i>dialog</i> do FCE - Licenciamento para Mineração
	 *
	 * @author eduardo.fernandes
	 * @since 27/06/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 */
	private void fecharDialog() {
		if(existeAlgumOutros()){
			super.exibirInformacao001();
		}
		else{
			RequestContext.getCurrentInstance().execute("rel_lic_mineracao.show()");
		}
		RequestContext.getCurrentInstance().execute("mineracaoLicenciamento.hide()");
	}

	private void atualizarDialog() {
		RequestContext.getCurrentInstance().addPartialUpdateTarget("mineracaoLicenciamento");
		RequestContext.getCurrentInstance().addPartialUpdateTarget("relLicMineracao");
		RequestContext.getCurrentInstance().addPartialUpdateTarget(":tabLicMineracao");
	}

	@Override
	protected void prepararDuplicacao() {
		fceLicenciamentoMineral.setIdeFce(super.fce);
		fceLicenciamentoMineral.setIdeFceLicenciamentoMineral(null);
		for(ProcessoDnpm processoDnpm : getListaProcessoDnpm()){
			processoDnpm.setIdeProcessoDnpm(null);
			processoDnpm.setIdeFceLicenciamentoMineral(fceLicenciamentoMineral);
		}
		if(super.isExisteProcessoOutorga()) {
			for(OutorgaMineracao outorgaMineracao : super.listaOutorgaMineracao){
				outorgaMineracao.setIdeOutorgaMineracao(null);
				outorgaMineracao.setFceLicenciamentoMineral(fceLicenciamentoMineral);
			}
		}
		if(isFormaDisposicaoRejeitoAdicionada()) {
			for(FormaDisposicaoRejeito disposicao : listaFormaDisposicaoRejeito){
				disposicao.setIdeFormaDisposicaoRejeito(null);
				disposicao.setFceLicenciamentoMineral(fceLicenciamentoMineral);
			}
		}
		for(FceLicenciamentoMineralServidaoMineraria servidao : listaFceLicenciamentoMineralServidaoMineraria){
			servidao.setFceLicenciamentoMineral(fceLicenciamentoMineral);
		}
		for(FceLicenciamentoMineralSubstanciaMineralTipologia subMineral :listaFceLicenciamentoMineralSubstanciaMineral){
			subMineral.setFceLicenciamentoMineral(fceLicenciamentoMineral);
		}
		for(FceLicenciamentoMineralTipoApp tipoAppObj : listaFceLicenciamentoMineralTipoApp) {
			tipoAppObj.setFceLicenciamentoMineral(fceLicenciamentoMineral);
		}
	}

	@Override
	protected void duplicarFce() {
		try{
			LocalizacaoGeografica locGeoLavra = fceLicenciamentoMineral.getIdeLocalizacaoGeograficaLavra();
			fceLicenciamentoMineral.setIdeLocalizacaoGeograficaLavra(facade.duplicarLocalizacaoGeografica(locGeoLavra));
			LocalizacaoGeografica locGeoServidao = fceLicenciamentoMineral.getIdeLocalizacaoGeograficaServidao();
			if(!Util.isNullOuVazio(locGeoServidao)) {
				fceLicenciamentoMineral.setIdeLocalizacaoGeograficaServidao(facade.duplicarLocalizacaoGeografica(locGeoServidao));
			}
			for(ProcessoDnpm processoDnpm : getListaProcessoDnpm()){
				LocalizacaoGeografica locGeo = processoDnpm.getIdeLocalizacaoGeografica();
				processoDnpm.setIdeLocalizacaoGeografica(facade.duplicarLocalizacaoGeografica(locGeo));
			}
			salvarAbas();
		}
		catch(Exception e){
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_duplicar") + " Licenciamento para Mineração.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * ADICIONAR COMENTÁRIO
	 *
	 * @author eduardo.fernandes
	 * @since 26/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/">#</a> ADICIONAR TICKET
	 * @throws Exception
	 */
	private void salvarAbas() throws Exception {
		activeTab = 0;
		while(activeTab <= 3){
			salvarAba();
			activeTab++;
		}
	}

	@Override
	protected void carregarFceTecnico() {
		edicao = true;
		carregarFceLicenciamentoMineral();
		super.init();
	}

	public boolean isSubstanciaMineralAdicionada() {
		return !Util.isNullOuVazio(listaFceLicenciamentoMineralSubstanciaMineral);
	}

	public int getSomenteShape() {
		return ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_SHAPEFILE.getId().intValue();
	}

	public void pesquisarConcentrado(){
		List<ProducaoProduto> listaProducaoProdutoObj = new ArrayList<ProducaoProduto>();
		listaProducaoProdutoObj.addAll(listaProducaoProdutoCompleta);
		limparListaProducaoProduto();
		if(isConcentradoProdutoAdicionado()) {
			removerConcetradoProdutoSelecionado(listaProducaoProdutoObj);
		}
		if(!Util.isNullOuVazio(concentradoProduto)) {
			for(ProducaoProduto producaoProduto : listaProducaoProdutoObj){
				if(producaoProduto.getNomProducaoProduto().toLowerCase().indexOf(concentradoProduto.toLowerCase()) != -1){
					this.listaProducaoProduto.add(producaoProduto);
				}
			}
		} else {
			if(Util.isNullOuVazio(this.listaProducaoProduto)) {
				this.listaProducaoProduto.addAll(listaProducaoProdutoCompleta);
				removerConcetradoProdutoSelecionado(this.listaProducaoProduto);
			}
		}
	}

	/**
	 * ADICIONAR COMENTÁRIO
	 *
	 * @author eduardo.fernandes
	 * @since 26/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/">#</a>  ADICIONAR TICKET
	 * @param listaProducaoProduto
	 */
	private void removerConcetradoProdutoSelecionado(List<ProducaoProduto> listaProducaoProduto) {
		for(FceLicenciamentoMineralProducaoProduto fceLicenciamentoMineralProducaoProdutoObj : listaFceLicenciamentoMineralProducaoProduto) {
			listaProducaoProduto.remove(fceLicenciamentoMineralProducaoProdutoObj.getProducaoProduto());
		}
	}

	public void adicionarConcentrado() {
		if(Util.isNullOuVazio(listaFceLicenciamentoMineralProducaoProduto)) {
			listaFceLicenciamentoMineralProducaoProduto = new ArrayList<FceLicenciamentoMineralProducaoProduto>();
		}
		listaProducaoProduto.remove(producaoProduto);
		listaFceLicenciamentoMineralProducaoProduto.add(new FceLicenciamentoMineralProducaoProduto(fceLicenciamentoMineral, producaoProduto));
		if(producaoProduto.isOutros()){
			super.exibirInformacao001();
		}
		super.exibirMensagem001();
	}

	public void confirmarConcentrado(ActionEvent event) {
		FceLicenciamentoMineralProducaoProduto fceLicenciamentoMineralProducaoProdutoObj = (FceLicenciamentoMineralProducaoProduto) event.getComponent().getAttributes().get("producaoProduto");
		fceLicenciamentoMineralProducaoProdutoObj.setConfirmado(true);
		if(fceLicenciamentoMineralProducaoProdutoObj.isEdicao()) {
			super.exibirMensagem002();
		} 
		else {
			super.exibirMensagem001();
		}
	}

	public void editarConcentrado(ActionEvent event) {
		FceLicenciamentoMineralProducaoProduto fceLicenciamentoMineralProducaoProdutoObj = (FceLicenciamentoMineralProducaoProduto) event.getComponent().getAttributes().get("producaoProduto");
		fceLicenciamentoMineralProducaoProdutoObj.setConfirmado(false);
		fceLicenciamentoMineralProducaoProdutoObj.setEdicao();
	}

	public void excluirConcentrado() {
		listaFceLicenciamentoMineralProducaoProduto.remove(fceLicenciamentoMineralProducaoProduto);
		listaProducaoProduto.add(fceLicenciamentoMineralProducaoProduto.getProducaoProduto());
		Collections.sort(listaProducaoProduto);
		super.exibirMensagem005();
	}

	public boolean isExisteProducaoProdutoParaAdicionar() {
		return !Util.isNullOuVazio(listaProducaoProduto);
	}

	public boolean isConcentradoProdutoAdicionado() {
		return !Util.isNullOuVazio(listaFceLicenciamentoMineralProducaoProduto);
	}
	
	/**
	 * @author eduardo.fernandes
	 * @since 22/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/">#</a> ADICIONAR TICKET
	 */
	private void limparListaProducaoProduto() {
		if(!Util.isNullOuVazio(listaProducaoProduto)) {
			listaProducaoProduto.clear();
		}
	}

	/**
	 *  ADICIONAR COMENTÁRIO
	 *
	 * @author eduardo.fernandes
	 * @since 22/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/">#</a>  ADICIONAR TICKET
	 * @return
	 * @throws Exception
	 */
	private void carregarListaProducaoProduto() {
		try {
			listaProducaoProdutoCompleta = facade.listarProducaoProduto();
			listaProducaoProduto = new ArrayList<ProducaoProduto>();
			listaProducaoProduto.addAll(listaProducaoProdutoCompleta);
			if(super.isFceTecnico() && edicao) {
				listaFceLicenciamentoMineralProducaoProduto = facade.listarFceLicenciamentoMineralProducaoProdutoBy(fceLicenciamentoMineral);
				if(!Util.isNullOuVazio(listaFceLicenciamentoMineralProducaoProduto)) {
					for(FceLicenciamentoMineralProducaoProduto produto : listaFceLicenciamentoMineralProducaoProduto) {
						listaProducaoProduto.remove(produto.getProducaoProduto());
					}
				}
			}

		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " a lista de concentrado/produto.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);

		}
	}

	public FceLicenciamentoMineral getFceLicenciamentoMineral() {
		return fceLicenciamentoMineral;
	}

	public void setFceLicenciamentoMineral(FceLicenciamentoMineral fceLicenciamentoMineral) {
		this.fceLicenciamentoMineral = fceLicenciamentoMineral;
	}

	public List<RegimeExploracao> getListaRegimeExploracao() {
		return listaRegimeExploracao;
	}

	public List<TipoSistemaTratamento> getListaTipoSistemaTratamento() {
		return listaTipoSistemaTratamento;
	}

	public List<TipoEmissaoAtmosferica> getListaTipoEmissaoAtmosferica() {
		return listaTipoEmissaoAtmosferica;
	}

	public List<TipoSistemaTratamento> getListaTipoSistemaTratamentoSelected() {
		return listaTipoSistemaTratamentoSelected;
	}

	public void setListaTipoSistemaTratamentoSelected(List<TipoSistemaTratamento> listaTipoSistemaTratamentoSelected) {
		this.listaTipoSistemaTratamentoSelected = listaTipoSistemaTratamentoSelected;
	}

	public List<TipoEmissaoAtmosferica> getListaTipoEmissaoAtmosfericaSelected() {
		return listaTipoEmissaoAtmosfericaSelected;
	}

	public void setListaTipoEmissaoAtmosfericaSelected(List<TipoEmissaoAtmosferica> listaTipoEmissaoAtmosfericaSelected) {
		this.listaTipoEmissaoAtmosfericaSelected = listaTipoEmissaoAtmosfericaSelected;
	}

	public List<TipoApp> getListaTipoApp() {
		return listaTipoApp;
	}

	public List<MetodoLavra> getListaMetodoLavra() {
		return listaMetodoLavra;
	}

	public List<TecnicaLavra> getListaTecnicaLavraCeuAberto() {
		return listaTecnicaLavraCeuAberto;
	}

	public List<TecnicaLavra> getListaTecnicaLavraSubterranea() {
		return listaTecnicaLavraSubterranea;
	}

	public List<ServidaoMineraria> getListaServidaoMineraria() {
		return listaServidaoMineraria;
	}

	public List<TipoEstrutura> getListaTipoEstrutura() {
		return listaTipoEstrutura;
	}

	public List<ClassificacaoRejeitoDnpm> getListaClassificacaoRejeitoDnpm() {
		return listaClassificacaoRejeitoDnpm;
	}

	public List<TipoTransporteMinerio> getListaTipoTransporteMinerio() {
		return listaTipoTransporteMinerio;
	}

	public List<MetodoLavra> getListaMetodoLavraSelected() {
		return listaMetodoLavraSelected;
	}

	public void setListaMetodoLavraSelected(List<MetodoLavra> listaMetodoLavraSelected) {
		this.listaMetodoLavraSelected = listaMetodoLavraSelected;
	}

	public List<TecnicaLavra> getListaTecnicaLavraCeuAbertoSelected() {
		return listaTecnicaLavraCeuAbertoSelected;
	}

	public void setListaTecnicaLavraCeuAbertoSelected(List<TecnicaLavra> listaTecnicaLavraCeuAbertoSelected) {
		this.listaTecnicaLavraCeuAbertoSelected = listaTecnicaLavraCeuAbertoSelected;
	}

	public List<TecnicaLavra> getListaTecnicaLavraSubterraneaSelected() {
		return listaTecnicaLavraSubterraneaSelected;
	}

	public void setListaTecnicaLavraSubterraneaSelected(List<TecnicaLavra> listaTecnicaLavraSubterraneaSelected) {
		this.listaTecnicaLavraSubterraneaSelected = listaTecnicaLavraSubterraneaSelected;
	}

	public List<TipoTransporteMinerio> getListaTipoTransporteMinerioSelected() {
		return listaTipoTransporteMinerioSelected;
	}

	public void setListaTipoTransporteMinerioSelected(List<TipoTransporteMinerio> listaTipoTransporteMinerioSelected) {
		this.listaTipoTransporteMinerioSelected = listaTipoTransporteMinerioSelected;
	}

	public List<RegimeExploracao> getListaRegimeExploracaoSelected() {
		return listaRegimeExploracaoSelected;
	}

	public void setListaRegimeExploracaoSelected(List<RegimeExploracao> listaRegimeExploracaoSelected) {
		this.listaRegimeExploracaoSelected = listaRegimeExploracaoSelected;
	}

	public List<TecnicaLavra> getListaTecnicaLavraCeuAbertoDragagem() {
		return listaTecnicaLavraCeuAbertoDragagem;
	}

	public List<TecnicaLavra> getListaTecnicaLavraCeuAbertoDragagemSelected() {
		return listaTecnicaLavraCeuAbertoDragagemSelected;
	}

	public void setListaTecnicaLavraCeuAbertoDragagemSelected(List<TecnicaLavra> listaTecnicaLavraCeuAbertoDragagemSelected) {
		this.listaTecnicaLavraCeuAbertoDragagemSelected = listaTecnicaLavraCeuAbertoDragagemSelected;
	}

	public FormaDisposicaoRejeito getFormaDisposicaoRejeito() {
		return formaDisposicaoRejeito;
	}

	public void setFormaDisposicaoRejeito(FormaDisposicaoRejeito formaDisposicaoRejeito) {
		this.formaDisposicaoRejeito = formaDisposicaoRejeito;
	}

	public List<FormaDisposicaoRejeito> getListaFormaDisposicaoRejeito() {
		return listaFormaDisposicaoRejeito;
	}

	public boolean isLicencaPrevia() {
		return licencaPrevia;
	}

	public List<FceLicenciamentoMineralServidaoMineraria> getListaFceLicenciamentoMineralServidaoMineraria() {
		return listaFceLicenciamentoMineralServidaoMineraria;
	}

	public void setListaFceLicenciamentoMineralServidaoMineraria(List<FceLicenciamentoMineralServidaoMineraria> listaFceLicenciamentoMineralServidaoMineraria) {
		this.listaFceLicenciamentoMineralServidaoMineraria = listaFceLicenciamentoMineralServidaoMineraria;
	}

	public FceLicenciamentoMineralServidaoMineraria getFceLicenciamentoMineralServidaoMineraria() {
		return fceLicenciamentoMineralServidaoMineraria;
	}

	public void setFceLicenciamentoMineralServidaoMineraria(FceLicenciamentoMineralServidaoMineraria fceLicenciamentoMineralServidaoMineraria) {
		this.fceLicenciamentoMineralServidaoMineraria = fceLicenciamentoMineralServidaoMineraria;
	}

	public List<FceLicenciamentoMineralSubstanciaMineralTipologia> getListaFceLicenciamentoMineralSubstanciaMineral() {
		return listaFceLicenciamentoMineralSubstanciaMineral;
	}

	public FceLicenciamentoMineralSubstanciaMineralTipologia getFceLicenciamentoMineralSubstanciaMineral() {
		return fceLicenciamentoMineralSubstanciaMineral;
	}

	public void setFceLicenciamentoMineralSubstanciaMineral(FceLicenciamentoMineralSubstanciaMineralTipologia fceLicenciamentoMineralSubstanciaMineral) {
		this.fceLicenciamentoMineralSubstanciaMineral = fceLicenciamentoMineralSubstanciaMineral;
	}

	public List<FceLicenciamentoMineralTipoApp> getListaFceLicenciamentoMineralTipoApp() {
		return listaFceLicenciamentoMineralTipoApp;
	}

	public void setListaFceLicenciamentoMineralTipoApp(List<FceLicenciamentoMineralTipoApp> listaFceLicenciamentoMineralTipoApp) {
		this.listaFceLicenciamentoMineralTipoApp = listaFceLicenciamentoMineralTipoApp;
	}

	public TipoApp getTipoApp() {
		return tipoApp;
	}

	public void setTipoApp(TipoApp tipoApp) {
		this.tipoApp = tipoApp;
	}

	public ServidaoMineraria getServidaoMineraria() {
		return servidaoMineraria;
	}

	public void setServidaoMineraria(ServidaoMineraria servidaoMineraria) {
		this.servidaoMineraria = servidaoMineraria;
	}

	public FceLicenciamentoMineralTipoApp getFceLicenciamentoMineralTipoApp() {
		return fceLicenciamentoMineralTipoApp;
	}

	public void setFceLicenciamentoMineralTipoApp(FceLicenciamentoMineralTipoApp fceLicenciamentoMineralTipoApp) {
		this.fceLicenciamentoMineralTipoApp = fceLicenciamentoMineralTipoApp;
	}

	public String getConcentradoProduto() {
		return concentradoProduto;
	}

	public void setConcentradoProduto(String concentradoProduto) {
		this.concentradoProduto = concentradoProduto;
	}

	public ProducaoProduto getProducaoProduto() {
		return producaoProduto;
	}

	public void setProducaoProduto(ProducaoProduto producaoProduto) {
		this.producaoProduto = producaoProduto;
	}

	public List<ProducaoProduto> getListaProducaoProduto() {
		return listaProducaoProduto;
	}

	public List<FceLicenciamentoMineralProducaoProduto> getListaFceLicenciamentoMineralProducaoProduto() {
		return listaFceLicenciamentoMineralProducaoProduto;
	}

	public FceLicenciamentoMineralProducaoProduto getFceLicenciamentoMineralProducaoProduto() {
		return fceLicenciamentoMineralProducaoProduto;
	}

	public void setFceLicenciamentoMineralProducaoProduto(FceLicenciamentoMineralProducaoProduto fceLicenciamentoMineralProducaoProduto) {
		this.fceLicenciamentoMineralProducaoProduto = fceLicenciamentoMineralProducaoProduto;
	}

	@Override
	public void salvarSubstanciaMineralFechar() {
		if(salvarSubstanciaMineralContinuar()){
			RequestContext.getCurrentInstance().execute("dialogIncluirSubstanciaMineral.hide()");
		}
	}

	@Override
	public boolean salvarSubstanciaMineralContinuar() {
		if(super.isSubstanciaMineral()){
			if(Util.isNull(listaFceLicenciamentoMineralSubstanciaMineral)){
				listaFceLicenciamentoMineralSubstanciaMineral = new ArrayList<FceLicenciamentoMineralSubstanciaMineralTipologia>();
			} 
			boolean podeAdicionar = true;
			for(FceLicenciamentoMineralSubstanciaMineralTipologia fceLicenciamentoMineralSubstanciaMineral : listaFceLicenciamentoMineralSubstanciaMineral){
				if(super.getSubstanciaMineralSelecionada().equals(fceLicenciamentoMineralSubstanciaMineral.getSubstanciaMineralTipologia())){
					podeAdicionar = false;
					JsfUtil.addErrorMessage("A " + Util.getString("fce_lic_min_substancia_mineral") + " já foi selecionada.");
					return false;
				}
			}
			if(podeAdicionar){
				listaFceLicenciamentoMineralSubstanciaMineral.add(new FceLicenciamentoMineralSubstanciaMineralTipologia(fceLicenciamentoMineral, super.getSubstanciaMineralSelecionada()));
				RequestContext.getCurrentInstance().addPartialUpdateTarget("tabLicMineracao:minLicDadosGerais:gridSubstanciaMineral");
				if(super.getSubstanciaMineralSelecionada().getIdeSubstanciaMineral().isOutros()){
					super.exibirInformacao001();
				}
				super.exibirMensagem001();
				return true;
			}
		}
		return false;
	}

}
