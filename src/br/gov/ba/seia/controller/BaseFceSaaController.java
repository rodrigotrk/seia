package br.gov.ba.seia.controller;

import org.primefaces.event.TabChangeEvent;

import br.gov.ba.seia.interfaces.FceNavegacaoInterface;

public class BaseFceSaaController extends FceRequerimentoController implements FceNavegacaoInterface {

	protected int activeTab;
	
	@Override
	public void init() {
		activeTab = 0;
		carregarAba();
	}

	@Override
	public void verificarEdicao() {

		
	}

	@Override
	public void carregarAba() {

	}

	@Override
	public void finalizar() {

		
	}

	@Override
	public void limpar() {

		
	}

	@Override
	public boolean validarAba() {

		return false;
	}

	@Override
	public void abrirDialog() {

		
	}

	@Override
	protected void prepararDuplicacao() {

		
	}

	@Override
	protected void duplicarFce() {

		
	}

	@Override
	protected void carregarFceTecnico() {

		
	}
	
	@Override
	public void voltarAba() {
		activeTab--;
	}

	@Override
	public void controlarAbas(TabChangeEvent event) {

		
	}
	
	@Override
	public void avancarAba() {
		activeTab++;		
	}

	public int getActiveTab() {
		return activeTab;
	}

	public void setActiveTab(int activeTab) {
		this.activeTab = activeTab;
	}

	@Override
	public void prepararParaFinalizar() throws Exception {

		
	}

}
