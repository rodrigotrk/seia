package br.gov.ba.seia.controller;

import java.util.List;

import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.entity.AnaliseTecnica;
import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.FceAquicultura;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.TipoPeriodoDerivacao;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum;
import br.gov.ba.seia.facade.FceServiceFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.interfaces.FceNavegacaoInterface;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.SessaoUtil;
import br.gov.ba.seia.util.Util;

/**
 * Classe que irá controlar a navegção do FCE - Outorga Aquicultura
 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
 * @since 16/04/2015
 */
@Named("fceOutorgaAquiculturaNavegacaoController")
@ViewScoped
public class FceOutorgaAquiculturaNavegacaoController implements FceNavegacaoInterface {

	@EJB
	private FceServiceFacade fceServiceFacade;
	
	private static final int ABA_REQUERIMENTO = 0;
	private static final int ABA_CAPTACAO = 1;
	private static final int ABA_LANCAMENTO = 2;
	private static final int ABA_RIO = 3;
	private static final int ABA_BARRAGEM = 4;
	private static final int ABA_ADICIONAIS = 5;

	private boolean temCaptacao;
	private boolean temLancamento;
	private boolean temBarragem;
	private boolean temRio;

	private boolean desabilitarTudo;

	private boolean outrosCaptacao;

	private boolean outrosRio;

	private boolean outrosBarragem;

	private boolean analiseTecnica;
	
	private int activeTab;

	private Fce fce;

	private Requerimento requerimento;

	private List<TipoPeriodoDerivacao> listaTipoPeriodoDerivacao;

	private List<Tipologia> listaTipologia;

	private Boolean fceLicenciamentoAquicolaPreenchido;
	
	boolean renderedAbaCaptacao;
	boolean renderedAbaLancamento;
	boolean renderedAbaRio;
	boolean renderedAbaBarragem;
	
	public void iniciarFce(Requerimento requerimento){
		if(Util.isNull(fce)){
			fce = new Fce(new DocumentoObrigatorio(DocumentoObrigatorioEnum.FCE_OUTORGA_AQUICULTURA.getId(),"Formulário de Caracterização do Empreendimento - Outorga para Aquicultura"), requerimento, null);
		}
	}
	
	public void iniciarFceTecnico(Fce ideFce, AnaliseTecnica analiseTecnica){
		if(Util.isNull(fce)){
			fce = new Fce(ideFce.getIdeDocumentoObrigatorio(), requerimento, analiseTecnica);
		}
	}
	
	public void init(){
		limpar();
		abrirDialog();
	}

	/**
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @since 11/02/2016
	 */
	public void abrirDialog() {
		RequestContext.getCurrentInstance().addPartialUpdateTarget("gridOutorgaAquicultura");
		RequestContext.getCurrentInstance().addPartialUpdateTarget("dlgImprimirRelatorioAquicultura");
		RequestContext.getCurrentInstance().execute("outorgaAquicultura.show()");
	}

	/**
	 * Método que controla as abas do FCE de acordo com o clique dado pelo usuário na tela.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 07/11/2014
	 * @param event
	 */
	@Override
	public void controlarAbas(TabChangeEvent event) {
		if ("abaDadosRequerimento".equals(event.getTab().getId())) {
			activeTab = 0;
		}
		else if ("abaCaptacao".equals(event.getTab().getId())) {
			activeTab = 1;
		}
		else if ("abaLancamento".equals(event.getTab().getId())) {
			activeTab = 2;
		}
		else if ("abaRio".equals(event.getTab().getId())) {
			activeTab = 3;
		}
		else if ("abaBarragem".equals(event.getTab().getId())) {
			activeTab = 4;
		}
		else if ("abaDadosAdicionais".equals(event.getTab().getId())) {
			activeTab = 5;
		}
	}

	/**
	 * Método para incrementar o {@link #activeTab}
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 07/11/2014
	 */
	@Override
	public void avancarAba() {
		activeTab++;
		avancarAbasInativas();
	}

	/**
	 * Método para pular as abas que estão inativas
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 06/11/2014
	 */
	private void avancarAbasInativas() {
		if(pularAbaCaptacao()){
			activeTab++;
		}
		if(pularAbaLancamento()){
			activeTab++;
		}
		if(pularAbaRio()){
			activeTab++;
		}
		if(pularAbaBarragem()){
			activeTab++;
		}
	}

	/**
	 * Método para decrementar o {@link #activeTab}
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 07/11/2014
	 */
	@Override
	public void voltarAba() {
		activeTab--;
		voltarAbasInativas();
	}

	/**
	 * Método para pular as abas que estão inativas
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 10/12/2014
	 */
	private void voltarAbasInativas() {
		if(pularAbaBarragem()){
			activeTab--;
		}
		if(pularAbaRio()){
			activeTab--;
		}
		if(pularAbaLancamento()){
			activeTab--;
		}
		if(pularAbaCaptacao()){
			activeTab--;
		}
	}

	/**
	 * Método que verifica se a Aba <i>Viveiro Escavado - Captação</i> está ativa e o {@link Requerimento} não contém ponto de Captação cadastrado.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @return
	 * @since 16/04/2015
	 */
	private boolean pularAbaCaptacao() {
		return isAbaViveiroEscavadoCaptacaoAtiva() && !temCaptacao;
	}

	/**
	 * Método que verifica se a Aba <i>Viveiro Escavado - Lançamento</i> está ativa e o {@link Requerimento} não contém ponto de Lançamento cadastrado.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @return
	 * @since 16/04/2015
	 */
	private boolean pularAbaBarragem() {
		return isAbaTanqueRedeBarragemAtiva() && !temBarragem;
	}

	/**
	 * Método que verifica se a Aba <i>Tanque Rede - Rio</i> está ativa e o {@link Requerimento} não contém intervenção em Rio cadastrada.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @return
	 * @since 16/04/2015
	 */
	private boolean pularAbaRio() {
		return isAbaTanqueRedeRioAtiva() && !temRio;
	}

	/**
	 * Método que verifica se a Aba <i>Tanque Rede - Barragem</i> está ativa e o {@link Requerimento} não contém intervenção em Barragem cadastrada.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @return
	 * @since 16/04/2015
	 */
	private boolean pularAbaLancamento() {
		return isAbaViveiroEscavadoLancamentoAtiva() && !temLancamento;
	}

	public void limpar(){
		limparNavegacao();
		limparRendereds();
		limparAquiculturaBeans();
	}
	
	public void limparNavegacao(){
		listaTipologia = null;
		temCaptacao = false;
		temLancamento = false;
		temRio = false;
		temBarragem = false;
		listaTipoPeriodoDerivacao = null;
		fceLicenciamentoAquicolaPreenchido = null;
		activeTab = 0;
		analiseTecnica = false;
	}

	public void finalizar(ActionEvent evt){
		boolean finalizado = true;
		if(temCaptacao && finalizado){
			FceOutorgaAquiculturaCaptacaoController aquiculturaCaptacaoController = (FceOutorgaAquiculturaCaptacaoController) SessaoUtil.recuperarManagedBean("#{fceOutorgaAquiculturaCaptacaoController}", FceOutorgaAquiculturaCaptacaoController.class);
			aquiculturaCaptacaoController.finalizar();
			if(Util.isNullOuVazio(aquiculturaCaptacaoController.getFceAquiculturaCaptacao())){
				finalizado = false;
			}
		}
		if(temLancamento && finalizado){
			FceOutorgaAquiculturaLancamentoController aquiculturaLancamentoController = (FceOutorgaAquiculturaLancamentoController) SessaoUtil.recuperarManagedBean("#{fceOutorgaAquiculturaLancamentoController}", FceOutorgaAquiculturaLancamentoController.class);
			aquiculturaLancamentoController.finalizar();
			if(Util.isNullOuVazio(aquiculturaLancamentoController.getFceAquiculturaLancamento())){
				finalizado = false;
			}
		}
		if(temRio  && finalizado){
			FceOutorgaAquiculturaRioController aquiculturaRioController = (FceOutorgaAquiculturaRioController) SessaoUtil.recuperarManagedBean("#{fceOutorgaAquiculturaRioController}", FceOutorgaAquiculturaRioController.class);
			aquiculturaRioController.finalizar();
			if(Util.isNullOuVazio(aquiculturaRioController.getFceAquiculturaRio())){
				finalizado = false;
			}
		}
		if(temBarragem && finalizado){
			FceOutorgaAquiculturaBarragemController aquiculturaBarragemController = (FceOutorgaAquiculturaBarragemController) SessaoUtil.recuperarManagedBean("#{fceOutorgaAquiculturaBarragemController}", FceOutorgaAquiculturaBarragemController.class);
			aquiculturaBarragemController.finalizar();
			if(Util.isNullOuVazio(aquiculturaBarragemController.getFceAquiculturaBarragem())){
				finalizado = false;
			}
		}
		if(finalizado){
			if(isOutros()) {
				JsfUtil.addWarnMessage(Util.getString("fce_out_aqui_especie_outros"));
			}
			else {
				RequestContext.getCurrentInstance().execute("rel_aquicultura.show()");
			}
			RequestContext.getCurrentInstance().execute("outorgaAquicultura.hide()");
		}
	}

	private void limparRendereds(){
		renderedAbaCaptacao = false;
		renderedAbaLancamento = false;
		renderedAbaRio = false;
		renderedAbaBarragem = false;
	}
	
	private void limparAquiculturaBeans(){
		SessaoUtil.removerManagedBeanFromViewScoped("fceOutorgaAquiculturaCaptacaoController");
		SessaoUtil.removerManagedBeanFromViewScoped("fceOutorgaAquiculturaLancamentoController");
		SessaoUtil.removerManagedBeanFromViewScoped("fceOutorgaAquiculturaBarragemController");
		SessaoUtil.removerManagedBeanFromViewScoped("fceOutorgaAquiculturaRioController");
	}
	
	public String msgImprimirRelatorio(String nomFce) {
		if(!Util.isNull(fce) && fce.isFceTecnico()) {
			return Util.getString("geral_msg_imprimir_relatorio_dados_concedidos_fce");
		}
		return Util.getString("geral_msg_imprimir_relatorio_fce") + " - " + nomFce + "?";
	}

	/**
	 * Método que imprime o relatório do {@link FceAquicultura}
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 27/11/2014
	 * @return
	 * @throws Exception
	 */
	public StreamedContent getImprimirRelatorio(){
		try {
			if(!Util.isNullOuVazio(fce)&& Util.isNull(fce.getIdeFce())){
				fce = null;
			}
			
			if(temCaptacao){
				FceOutorgaAquiculturaCaptacaoController aquiculturaCaptacaoController = (FceOutorgaAquiculturaCaptacaoController) SessaoUtil.recuperarManagedBean("#{fceOutorgaAquiculturaCaptacaoController}", FceOutorgaAquiculturaCaptacaoController.class);
				return aquiculturaCaptacaoController.getImprimirRelatorio(fce, new DocumentoObrigatorio(DocumentoObrigatorioEnum.FCE_OUTORGA_AQUICULTURA.getId()));
			}
			else if(temLancamento){
				FceOutorgaAquiculturaLancamentoController aquiculturaLancamentoController = (FceOutorgaAquiculturaLancamentoController) SessaoUtil.recuperarManagedBean("#{fceOutorgaAquiculturaLancamentoController}", FceOutorgaAquiculturaLancamentoController.class);
				return aquiculturaLancamentoController.getImprimirRelatorio(fce, new DocumentoObrigatorio(DocumentoObrigatorioEnum.FCE_OUTORGA_AQUICULTURA.getId()));
			}
			else if(temRio){
				FceOutorgaAquiculturaRioController aquiculturaRioController = (FceOutorgaAquiculturaRioController) SessaoUtil.recuperarManagedBean("#{fceOutorgaAquiculturaRioController}", FceOutorgaAquiculturaRioController.class);
				return aquiculturaRioController.getImprimirRelatorio(fce, new DocumentoObrigatorio(DocumentoObrigatorioEnum.FCE_OUTORGA_AQUICULTURA.getId()));
			}
			else if(temBarragem){
				FceOutorgaAquiculturaBarragemController aquiculturaBarragemController = (FceOutorgaAquiculturaBarragemController) SessaoUtil.recuperarManagedBean("#{fceOutorgaAquiculturaBarragemController}", FceOutorgaAquiculturaBarragemController.class);
				return aquiculturaBarragemController.getImprimirRelatorio(fce, new DocumentoObrigatorio(DocumentoObrigatorioEnum.FCE_OUTORGA_AQUICULTURA.getId()));
			}
			else {
				return null;
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_imprimir_relatorio") + " do FCE - Outorga para Aquicultura.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	private boolean isOutros(){
		return outrosCaptacao || outrosBarragem || outrosRio;
	}

	/*
	 * [INI] FLAG's
	 */
	public boolean isAbaDadosRequerimentoAtiva(){
		return activeTab == ABA_REQUERIMENTO;
	}

	public boolean isAbaViveiroEscavadoCaptacaoAtiva(){
		return activeTab == ABA_CAPTACAO;
	}

	public boolean isAbaViveiroEscavadoLancamentoAtiva(){
		return activeTab == ABA_LANCAMENTO;
	}

	public boolean isAbaTanqueRedeRioAtiva(){
		return activeTab == ABA_RIO;
	}

	public boolean isAbaTanqueRedeBarragemAtiva(){
		return activeTab == ABA_BARRAGEM;
	}

	public boolean isAbaDadosAdicionaisAtiva(){
		return activeTab == ABA_ADICIONAIS;
	}

	public boolean isAquiculturaViveiroEscavado() {
		return temCaptacao || temLancamento;
	}

	public boolean isAquiculturaTanqueRede() {
		return temRio || temBarragem;
	}
	/*
	 * [FIM] FLAG's
	 */


	/*
	 * getters and setters
	 */
	public int getActiveTab() {
		return activeTab;
	}

	public void setActiveTab(int activeTab) {
		this.activeTab = activeTab;
	}

	public boolean isTemCaptacao() {
		return temCaptacao;
	}

	public void setTemCaptacao(boolean temCaptacao) {
		renderedAbaCaptacao = true;
		this.temCaptacao = temCaptacao;
	}

	public boolean isTemLancamento() {
		return temLancamento;
	}

	public void setTemLancamento(boolean temLancamento) {
		renderedAbaLancamento = true;
		this.temLancamento = temLancamento;
	}

	public boolean isTemBarragem() {
		return temBarragem;
	}

	public void setTemBarragem(boolean temBarragem) {
		renderedAbaBarragem = true;
		this.temBarragem = temBarragem;
	}

	public boolean isTemRio() {
		return temRio;
	}

	public void setTemRio(boolean temRio) {
		renderedAbaRio = true;
		this.temRio = temRio;
	}

	public Fce getFce(){
		return fce;
	}

	public Requerimento getRequerimento() {
		return requerimento;
	}

	public void setRequerimento(Requerimento requerimento) {
		this.requerimento = requerimento;
	}

	public boolean isDesabilitarTudo() {
		return desabilitarTudo;
	}

	public void setDesabilitarTudo() {
		this.desabilitarTudo = true;
	}

	public List<TipoPeriodoDerivacao> getListaTipoPeriodoDerivacao() {
		return listaTipoPeriodoDerivacao;
	}

	public void setListaTipoPeriodoDerivacao(List<TipoPeriodoDerivacao> listaTipoPeriodoDerivacao) {
		this.listaTipoPeriodoDerivacao = listaTipoPeriodoDerivacao;
	}

	public List<Tipologia> getListaTipologia() {
		return listaTipologia;
	}

	public void setListaTipologia(List<Tipologia> listaTipologia) {
		this.listaTipologia = listaTipologia;
	}

	public boolean isOutrosCaptacao() {
		return outrosCaptacao;
	}

	public void setOutrosCaptacao(boolean outrosCaptacao) {
		this.outrosCaptacao = outrosCaptacao;
	}

	public boolean isOutrosRio() {
		return outrosRio;
	}

	public void setOutrosRio(boolean outrosRio) {
		this.outrosRio = outrosRio;
	}

	public boolean isOutrosBarragem() {
		return outrosBarragem;
	}

	public void setOutrosBarragem(boolean outrosBarragem) {
		this.outrosBarragem = outrosBarragem;
	}

	public Boolean getFceLicenciamentoAquicolaPreenchido() {
		return fceLicenciamentoAquicolaPreenchido;
	}

	public void setFceLicenciamentoAquicolaPreenchido(Boolean fceLicenciamentoAquicolaPreenchido) {
		this.fceLicenciamentoAquicolaPreenchido = fceLicenciamentoAquicolaPreenchido;
	}

	public boolean isRenderedAbaCaptacao() {
		return renderedAbaCaptacao;
	}

	public boolean isRenderedAbaLancamento() {
		return renderedAbaLancamento;
	}

	public boolean isRenderedAbaRio() {
		return renderedAbaRio;
	}

	public boolean isRenderedAbaBarragem() {
		return renderedAbaBarragem;
	}
	
	public boolean isRenderedAbaDadosAdicionais() {
		return isAquiculturaViveiroEscavado() || isAquiculturaTanqueRede();
	}

	public void setFce(Fce fce) {
		this.fce = fce;
	}

	public boolean isAnaliseTecnica() {
		return analiseTecnica;
	}

	public void setAnaliseTecnica(boolean analiseTecnica) {
		this.analiseTecnica = analiseTecnica;
	}
}