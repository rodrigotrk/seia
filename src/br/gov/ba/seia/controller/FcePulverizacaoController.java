package br.gov.ba.seia.controller;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.controller.abstracts.BaseFceFinalidadeUsoAguaController;
import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.FceAtividadeArea;
import br.gov.ba.seia.entity.FcePulverizacao;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.TipoFinalidadeUsoAgua;
import br.gov.ba.seia.entity.TipologiaAtividade;
import br.gov.ba.seia.entity.TipologiaPulverizacao;
import br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum;
import br.gov.ba.seia.enumerator.TipoFinalidadeUsoAguaEnum;
import br.gov.ba.seia.enumerator.TipologiaEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.interfaces.FceNavegacaoInterface;
import br.gov.ba.seia.service.FcePulverizacaoService;
import br.gov.ba.seia.service.TipologiaPulverizacaoService;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

/**
 * 29/01/14
 * @author eduardo.fernandes
 */
@Named("fcePulverizacaoController")
@ViewScoped
public class FcePulverizacaoController extends BaseFceFinalidadeUsoAguaController implements FceNavegacaoInterface {

	@Inject
	private FceAtividadeAreaController fceAtividadeAreaController;

	@EJB
	private TipologiaPulverizacaoService tipologiaPulverizacaoService;
	@EJB
	private FcePulverizacaoService fcePulverizacaoService;

	private int activeTab;
	private static DocumentoObrigatorio DOC_PULVERIZACAO = new DocumentoObrigatorio(DocumentoObrigatorioEnum.FCE_PULVERIZACAO.getId());

	// ABA PULVERIZACAO
	private FceAtividadeArea fceAtividadeArea;
	private FcePulverizacao fcePulverizacao;
	private List<TipologiaPulverizacao> listaTipoPulverizacao;
	private TipologiaAtividade tipologiaAtividade;

	// ABA ADICIONAIS
	private static DocumentoObrigatorio DOC_PULVERIZACAO_UPLOAD = new DocumentoObrigatorio(DocumentoObrigatorioEnum.FCE_PULVERIZACAO_ADICIONAIS.getId());

	@Override
	public void init(){
		verificarEdicao();
		carregarAba();
		somarAreasDoNR();
		if(!super.isFceSalvo()){
			iniciarFce(DOC_PULVERIZACAO);
			fcePulverizacao = new FcePulverizacao(super.fce);
		}
		else {
			carregarFcePulverizacao();
			carregarListaFceAtividadeArea();
			carregarDocumentoAdicional();
		}
	}

	@Override
	public void verificarEdicao(){
		try {
			carregarFceDoRequerente(DOC_PULVERIZACAO);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " o FCE - Pulverização.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	// Carregar listas da tela
	@Override
	public void carregarAba(){
		carregarListaTipologiaPulverizacao();
		carregarListaTipologiaAtividade();
	}

	private void carregarListaTipologiaPulverizacao(){
		try {
			if(Util.isNullOuVazio(listaTipoPulverizacao)){
				listaTipoPulverizacao = new ArrayList<TipologiaPulverizacao>();
			}
			listaTipoPulverizacao = tipologiaPulverizacaoService.listarTipologiaPulverizacaoByIndAtivo();
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar")+" a lista de Tipologia Pulverização.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	private void carregarListaTipologiaAtividade(){
		fceAtividadeAreaController.carregarListaTipologiaAtividade(criarListaTipologiaEnumParaBusca());
	}

	private List<TipologiaEnum> criarListaTipologiaEnumParaBusca(){
		List<TipologiaEnum> listaTemp = new ArrayList<TipologiaEnum>();
		listaTemp.add(TipologiaEnum.AGRICULTURA_IRRIGADA_OUT);
		listaTemp.add(TipologiaEnum.SILVICULTURA);
		return listaTemp;
	}
	// FIM do carregar listas da tela

	// isEdicao
	private void carregarFcePulverizacao(){
		try {
			fcePulverizacao = fcePulverizacaoService.buscarFcePulverizacaoByIdeFce(super.fce);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " o FCE - Pulverização.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	private void carregarListaFceAtividadeArea(){
		fceAtividadeAreaController.carregarListaFceAtividadeArea(super.fce);
		if(isFcePulverizacaoIndOutros()){
			fceAtividadeAreaController.simularOutrosNaLista(true);
		}
		if(fceAtividadeAreaController.isCulturaAdicionada()){
			fceAtividadeAreaController.tratarListaAtividadesAdicionadas("Pulverização");
		}
	}

	private void carregarDocumentoAdicional(){
		try {
			carregarDocumentoAdicionalByDocumentoObrigatorio(DocumentoObrigatorioEnum.FCE_PULVERIZACAO_ADICIONAIS);
		}
		catch(Exception e){
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " o Documento Adicional do FCE - Pulverização.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	// FIM - isEdicao

	private void somarAreasDoNR(){
		BigDecimal areaTotalPulverizacaoDoNR = new BigDecimal(0);
		super.carregarListaOutorgaLocGeoByTipoFinalidadeUsoAgua(new TipoFinalidadeUsoAgua(TipoFinalidadeUsoAguaEnum.PULVERIZACAO_AGRICOLA.getId()));
		for(OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica : super.listaOutorgaLocalizacaoGeografica){
			if(!Util.isNull(outorgaLocalizacaoGeografica.getNumAreaPulverizada())) {
				areaTotalPulverizacaoDoNR = areaTotalPulverizacaoDoNR.add(outorgaLocalizacaoGeografica.getNumAreaPulverizada());
			}
		}
		fceAtividadeAreaController.calcularAreaDaCultura(areaTotalPulverizacaoDoNR);
	}

	// Acoes do usuario
	@Override
	public void controlarAbas(TabChangeEvent event) {
		if ("abaPulverizacao".equals(event.getTab().getId())) {
			activeTab = 0;
		} else if ("abaAdicionais".equals(event.getTab().getId())) {
			activeTab = 1;
		}
	}

	@Override
	public void avancarAba(){
		if(validarAba()){
			salvarAbaPulverizacao(true);
			activeTab ++;
		}
	}

	@Override
	public void voltarAba(){
		activeTab --;
	}

	public void changeMetodoTerrestre(ValueChangeEvent event){
		if(!Util.isNullOuVazio(event)){
			if (!(Boolean) event.getNewValue()) {
				fcePulverizacao.setNumVolumeTerrestre(new BigDecimal(0));
			}
		}
	}

	public void changeMetodoAereo(ValueChangeEvent event){
		if(!Util.isNullOuVazio(event)){
			if (!(Boolean) event.getNewValue()) {
				fcePulverizacao.setNumVolumeAerea(new BigDecimal(0));
			}
		}
	}

	/**
	 * Adiciona a cultura selecionada na listaTipologiaAtividadeAdicionadas
	 * @author eduardo.fernandes
	 */
	public void adicionarCultura(){
		fceAtividadeAreaController.adicionarCultura(tipologiaAtividade, false);

	}

	/**
	 * Confirma a fceAtividadeArea (cultura selecionada) e a adiciona na listaFceAtividadeArea
	 */
	public void confirmarCultura(){
		fceAtividadeAreaController.confirmarCultura(fceAtividadeArea, "Pulverização");
	}

	/**
	 * Permite edição do campo Área
	 */
	public void editarCultura(){
		fceAtividadeAreaController.editarCultura(fceAtividadeArea);
	}

	/**
	 * Exclui as culturas que o usuário já havia selecionado.
	 */
	public void excluirCultura(){
		fceAtividadeAreaController.excluirCultura(fceAtividadeArea);
		if(Util.isNullOuVazio(fceAtividadeAreaController.getListaFceAtividadeArea())){
			fcePulverizacao.setNumAreaPulverizada(new BigDecimal(0));
		}
	}

	private void salvarAbaPulverizacao(boolean foiBtnAvancar){
		boolean jaSalvo = !Util.isNullOuVazio(fcePulverizacao.getIdeFcePulverizacao());
		try {
			super.salvarFce();
			salvarFcePulverizacao();
			salvarFceAtividadeArea();
			if(foiBtnAvancar){
				exibirMensagens(jaSalvo);
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_salvar") + " o FCE - Pulverização.");
		}
	}

	@Override
	public void finalizar(){
		if(validarAba()){
			try {
				fcePulverizacaoService.finalizar(this);
			} catch (Exception e) {
				JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_salvar") + " o FCE - Pulverização.");
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		} else {
			activeTab = 0;
		}
	}
	
	public void prepararParaFinalizar() throws Exception{
		salvarAbaPulverizacao(false);
		if(validarAbaAdicionais()){
			salvarAbaAdicionais();
			super.concluirFce();
			exibirMensagens(isFceSalvo());
			RequestContext.getCurrentInstance().execute("pulverizacao.hide()");
			if(!fceAtividadeAreaController.existeOutros()){
				RequestContext.getCurrentInstance().execute("rel_pulverizacao.show()");
			} else {
				JsfUtil.addWarnMessage(Util.getString("fce_pulverizacao_finalizar_outros"));
			}
			fceAtividadeAreaController.limpar();
			limpar();
		}
	}

	private void exibirMensagens(boolean edicao){
		if(!edicao){
			super.exibirMensagem001();
		}
		else {
			super.exibirMensagem002();
		}
	}

	private void salvarFceAtividadeArea() throws Exception{
		if(super.isFceSalvo()){
			fceAtividadeAreaController.salvarFceAtividadeArea(super.fce);
		}
	}

	private void salvarFcePulverizacao() throws Exception{
		if(super.isFceSalvo()){
			fcePulverizacao.setIndOutros(fceAtividadeAreaController.existeOutros());
			fcePulverizacaoService.salvarFcePulverizacao(fcePulverizacao);
		}
	}

	private void salvarAbaAdicionais(){
		if(validarAbaAdicionais()){
			try {
				salvarDocumentoAdicional(requerimento, DOC_PULVERIZACAO_UPLOAD);
			} catch (Exception e) {
				JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_salvar_documento_adicional"));
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e,Util.SEIA_EXCEPTION);

			}
		}
	}
	// FIM Acoes do usuario

	// Métodos de validação
	@Override
	public boolean validarAba(){
		boolean valido = true;
		boolean confirmados = true;
		if(Util.isNullOuVazio(fcePulverizacao.getTipologiaPulverizacao())){
			JsfUtil.addErrorMessage("A tipologia da pulverização " + Util.getString("msg_generica_preenchimento_obrigatorio"));
			valido = false;
		}
		if(isIndTerrestreNullorFalse() && isIndAereaNullorFalse()){
			JsfUtil.addErrorMessage("O método de pulverização " + Util.getString("msg_generica_preenchimento_obrigatorio"));
			valido = false;
		}
		if(!isIndTerrestreNullorFalse()){
			if(Util.isNullOuVazio(fcePulverizacao.getNumVolumeTerrestre())){
				JsfUtil.addErrorMessage("A área terrestre " + Util.getString("msg_generica_null_ou_vazio"));
				valido = false;
			}
		}
		valido = validacaoObrigatoriedade(valido);

		if(!confirmados){
			valido = confirmados;
		}
		return valido;
	}

	private boolean validacaoObrigatoriedade(boolean valido) {
		if(!isIndAereaNullorFalse()){
			if(Util.isNullOuVazio(fcePulverizacao.getNumVolumeAerea())){
				JsfUtil.addErrorMessage("A área aérea " + Util.getString("msg_generica_null_ou_vazio"));
				valido = false;
			}
		}
		if(!fceAtividadeAreaController.isCulturaAdicionada()){
			JsfUtil.addErrorMessage(Util.getString("msg_generica_selecione_um") + " uma cultura pulverizada.");
			valido = false;
		}
		else {
			if(validarCulturaConfirmada()){
				if(!super.isFceTecnico() && !fceAtividadeAreaController.existeOutros() && fceAtividadeAreaController.isAreaTotalCalculada() && !fceAtividadeAreaController.isAreaTotalDoNRequalsSomatorioArea(fceAtividadeAreaController.getStringAreaTotalCulturaDoNR(), fceAtividadeAreaController.getSomatorioAreaTotalCultura())){
					JsfUtil.addErrorMessage("A área total a ser pulverizada tem que ser igual a "+ fceAtividadeAreaController.getStringAreaTotalCulturaDoNR() +" (ha), conforme informado no requerimento. Favor corrigir.");
					valido = false;
				}
			}
			if(Util.isNullOuVazio(fcePulverizacao.getNumAreaPulverizada())){
				JsfUtil.addErrorMessage("A área a ser pulverizada por dia " + Util.getString("msg_generica_null_ou_vazio"));
				valido = false;
			}
		}
		if(Util.isNullOuVazio(fcePulverizacao.getNumVolumeDerivar())){
			JsfUtil.addErrorMessage("O volume a derivar " + Util.getString("msg_generica_null_ou_vazio"));
			valido = false;
		}
		return valido;
	}

	/**
	 * @author eduardo (eduardo.fernandes@avansys.com.br)
	 * @return
	 * @since 16/03/2016
	 */
	public boolean validarCulturaConfirmada() {
		return fceAtividadeAreaController.verificarConfirmado(" Pulverizada.");
	}

	private boolean validarAbaAdicionais(){
		boolean valido = true;
		if(!isFcePulverizacaoSalva()){
			valido = validarAba();
		}
		if(!isArquivoUpado()){
			valido = false;
			JsfUtil.addErrorMessage(Util.getString("fce_outorga_obrigatorio_upload"));
		}
		return valido;
	}
	// FIM - Métodos de validação

	// Controladores e Rendered's
	public boolean isIndTerrestreNullorFalse(){
		return Util.isNullOuVazio(fcePulverizacao.getIndPulverizacaoTerrestre()) || !fcePulverizacao.getIndPulverizacaoTerrestre();
	}

	public boolean isIndAereaNullorFalse(){
		return Util.isNullOuVazio(fcePulverizacao.getIndPulverizacaoAerea()) || !fcePulverizacao.getIndPulverizacaoAerea();
	}

	public boolean isFcePulverizacaoSalva(){
		return !Util.isNullOuVazio(fcePulverizacao.getIdeFcePulverizacao());
	}

	private boolean isFcePulverizacaoIndOutros(){
		return !Util.isNullOuVazio(fcePulverizacao) && fcePulverizacao.getIndOutros();
	}

	public boolean isTipologiaAtividadeOutros(TipologiaAtividade tipologiaAtividade){
		return fceAtividadeAreaController.isTipologiaAtividadeOutros(tipologiaAtividade);
	}
	// FIM Controladores e Rendered's

	// STREAMs
	public StreamedContent getDadosAdicionais() {
		try {
			return getDadosAdicionais("Informacoes_Adicionais_FCE_Pulverizacao.doc", "Informações Adicionais - FCE Pulverização Agrícola.doc");
		} catch (FileNotFoundException e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_documento_adicional"));
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public StreamedContent  getImprimirRelatorio() {
		try {
			return getImprimirRelatorio(super.fce, DOC_PULVERIZACAO);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_imprimir_relatorio") + " do FCE - Pulverização.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	@Override
	public void limpar(){
		activeTab = 0;
		fceAtividadeAreaController.limpar();
		super.limparFce();
		super.limparDocumentoUpado();
		if(!Util.isNullOuVazio(listaTipoPulverizacao)){
			listaTipoPulverizacao.clear();
		}
		tipologiaAtividade = null;
		fcePulverizacao = null;
		fceAtividadeArea = null;
	}

	// getters && setters
	/**
	 * Lista de Culturas Pulverizadas que podem ser adicionadas
	 */
	public List<TipologiaPulverizacao> getListaTipoPulverizacao() {
		return listaTipoPulverizacao;
	}

	public FcePulverizacao getFcePulverizacao() {
		return fcePulverizacao;
	}

	public void setFcePulverizacao(FcePulverizacao fcePulverizacao) {
		this.fcePulverizacao = fcePulverizacao;
	}

	public TipologiaAtividade getTipologiaAtividade() {
		return tipologiaAtividade;
	}

	public void setTipologiaAtividade(TipologiaAtividade tipologiaAtividade) {
		this.tipologiaAtividade = tipologiaAtividade;
	}

	public int getActiveTab() {
		return activeTab;
	}

	public void setActiveTab(int activeTab) {
		this.activeTab = activeTab;
	}

	public FceAtividadeArea getFceAtividadeArea() {
		return fceAtividadeArea;
	}

	public void setFceAtividadeArea(FceAtividadeArea fceAtividadeArea) {
		this.fceAtividadeArea = fceAtividadeArea;
	}

	@Override
	public void abrirDialog() {
		RequestContext.getCurrentInstance().addPartialUpdateTarget("pnlFcePulverizacao");
		RequestContext.getCurrentInstance().addPartialUpdateTarget("dlgImprimirRelatorioPulverizacao");
		RequestContext.getCurrentInstance().execute("pulverizacao.show();");
	}

	@Override
	protected void prepararDuplicacao() {
		try {
			fcePulverizacao.setIdeFcePulverizacao(null);
			fcePulverizacao.setIdeFce(null);
			
			List<FceAtividadeArea> listFceAtividadeArea = new ArrayList<FceAtividadeArea>();
			
			for(FceAtividadeArea fceAtividadeArea : fceAtividadeAreaController.getListaFceAtividadeArea()){
				FceAtividadeArea fceAtividadeAreaClone = fceAtividadeArea.clone();
				fceAtividadeAreaClone.setIdeFceAtividadeArea(null);
				fceAtividadeAreaClone.setIdeFce(null);
				listFceAtividadeArea.add(fceAtividadeAreaClone);
			}
			
			fceAtividadeAreaController.getListaFceAtividadeArea().clear();
			fceAtividadeAreaController.getListaFceAtividadeArea().addAll(listFceAtividadeArea);
			
		} catch (CloneNotSupportedException e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}

	@Override
	protected void duplicarFce() {
		try {
			super.salvarFce();
			fcePulverizacao.setIdeFce(super.fce);
			salvarFcePulverizacao();
			salvarFceAtividadeArea();
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_duplicar") + " o FCE - Pulverização.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	@Override
	protected void carregarFceTecnico() {
		activeTab = 0;
		carregarAba();
		somarAreasDoNR();
		carregarFcePulverizacao();
		carregarListaFceAtividadeArea();
		carregarDocumentoAdicional();		
	}
}