/**
 *
 */
package br.gov.ba.seia.controller;

import javax.inject.Named;

import org.primefaces.event.TabChangeEvent;

import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.interfaces.FceNavegacaoInterface;

/**
 * Controller responsável pela navegação entre as abas do <b>FCE - Licenciamento para Aquicultura</b>.
 * <pre><b> #6934 </b></pre>
 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
 * @since 01/06/2015
 */
@Named("fceLicenciamentoAquiculturaNavegacaoController")
@ViewScoped
public class FceLicenciamentoAquiculturaNavegacaoController implements FceNavegacaoInterface{

	private static final int ABA_DADOS_GERAIS = 0;
	private static final int ABA_REQUERIMENTO = 1;
	private static final int ABA_VIVEIRO_ESCAVADO = 2;
	private static final int ABA_TANQUE_REDE = 3;
	private static final int ABA_ADICIONAIS = 4;

	private int activeTab;

	boolean renderedAbaDadosGerais;
	boolean renderedAbaDadosRequerimento;
	boolean renderedAbaViveiroEscavado;
	boolean renderedAbaTanqueRede;
	
	/**
	 * Método que controla as abas do FCE de acordo com o clique dado pelo usuário na tela.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 27/05/2015
	 * @param event
	 */
	@Override
	public void controlarAbas(TabChangeEvent event){
		if ("licAquiAbaDadosGerais".equals(event.getTab().getId())) {
			activeTab = ABA_DADOS_GERAIS;
		}
		else if ("licAquiAbaDadosRequerimento".equals(event.getTab().getId())) {
			activeTab = ABA_REQUERIMENTO;
		}
		else if ("licAquiAbaViveiroEscavado".equals(event.getTab().getId())) {
			activeTab = ABA_VIVEIRO_ESCAVADO;
		}
		else if ("licAquiAbaTanqueRede".equals(event.getTab().getId())) {
			activeTab = ABA_TANQUE_REDE;
		}
		else if ("licAquiAbaDadosAdicionais".equals(event.getTab().getId())) {
			activeTab = ABA_ADICIONAIS;
		}
	}

	/**
	 * Método para incrementar o {@link #activeTab}
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 27/05/2015
	 */
	@Override
	public void avancarAba() {
		activeTab++;
	}

	/**
	 * Método para decrementar o {@link #activeTab}
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 27/05/2015
	 */
	@Override
	public void voltarAba() {
		activeTab--;
	}
	
	public void renderizarAbas(){
		setRendered(true);
	}
	
	public void limparRendered(){
		setRendered(false);
		this.renderedAbaViveiroEscavado = false;
		this.renderedAbaTanqueRede = false;
	}

	private void setRendered(boolean isRendered) {
		this.renderedAbaDadosGerais = isRendered;
		this.renderedAbaDadosRequerimento = isRendered;
	}
	
	public void renderizarAbaViveiro(){
		this.renderedAbaViveiroEscavado = true;
	}
	
	public void renderizarAbaTanque(){
		this.renderedAbaTanqueRede = true;
	}

	public void limparNavegacao(){
		activeTab = 0;
	}

	public boolean isAbaDadosGeraisAtiva(){
		return activeTab == ABA_DADOS_GERAIS;
	}

	public boolean isAbaDadosRequerimentoAtiva(){
		return activeTab == ABA_REQUERIMENTO;
	}

	public boolean isAbaViveiroEscavadoAtiva(){
		return activeTab == ABA_VIVEIRO_ESCAVADO;
	}

	public boolean isAbaTanqueRedeAtiva(){
		return activeTab == ABA_TANQUE_REDE;
	}

	public boolean isAbaDadosAdicionaisAtiva(){
		return activeTab == ABA_ADICIONAIS;
	}

	/*
	 * [INI] - getters && setters
	 */
	public int getAbaInicial() {
		return 0;
	}

	public int getActiveTab() {
		return activeTab;
	}

	public void setActiveTab(int activeTab) {
		this.activeTab = activeTab;
	}
	/*
	 * [FIM] - getters && setters
	 */

	public boolean isRenderedAbaDadosGerais() {
		return renderedAbaDadosGerais;
	}

	public boolean isRenderedAbaDadosRequerimento() {
		return renderedAbaDadosRequerimento;
	}

	public boolean isRenderedAbaViveiroEscavado() {
		return renderedAbaViveiroEscavado;
	}

	public boolean isRenderedAbaTanqueRede() {
		return renderedAbaTanqueRede;
	}
}