package br.gov.ba.seia.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.inject.Named;

import br.gov.ba.seia.faces.ViewScoped;

import javax.faces.component.UIComponent;

import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;

import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.util.SeiaControllerAb;
import br.gov.ba.seia.util.Util;


@Named("abasEmpreendimentoController")
@ViewScoped
public class AbasEmpreendimentoController extends SeiaControllerAb implements Serializable {
	
	
	
	private static final long serialVersionUID = 1L;
	private static Integer ABALOCALIZACAO = 1;
	private static Integer ABARESPONSAVEL = 3;
	
	private static final String ABAIMOVEL = "ABAIMOVEL";
	private static final String HABILITAABAS  = "HABILITAABAS";
	
	private static Integer abaMAX = 4;
	private static Integer abaMIN = 0;
	
	private int indexAba;
	
	
	
	private Boolean visualizaProximo;
	private Boolean visualizaAnterior;
	private Boolean visualizaFinalizar;
	private Boolean desabilitaAbas;
	
	private Boolean desabilitaAbaImovel;
	
	Empreendimento empreendimento;
	
	
	
	
	
	@PostConstruct 
	public void init() {
		indexAba = 0;
		
		String lUrl = (String) getAtributoSession("URL_PROCURADOR_ORIGEM");//Retorna para a aba de procurador
		if (!Util.isNullOuVazio(lUrl)) {
			super.addAtributoSessao("URL_PROCURADOR_ORIGEM", null);
			this.indexAba = 4;
		}
		
		
		empreendimento = (Empreendimento) getAtributoSession("EMPREENDIMENTO_SESSAO");
		mudarStatusAbas();
	}
	
	
	
	
	private void mudarStatusAbas() {
		Boolean habilita = (Boolean) getAtributoSession(HABILITAABAS);
		setDesabilitaAbas(habilita);	
	}




	public void incrementaIndexAba() {
		if (!getDesabilitaAbaImovel() || indexAba != ABALOCALIZACAO) {
			this.indexAba++;
			RequestContext.getCurrentInstance().execute("_tabAbas.select(_tabAbas.getActiveIndex()+1)");
		} else {
			this.indexAba += 2;
			RequestContext.getCurrentInstance().execute("_tabAbas.select(_tabAbas.getActiveIndex()+2)");
		}
		
		
	}

	public void decrementaIndexAba() {
		if (!getDesabilitaAbaImovel() || indexAba != ABARESPONSAVEL) {
			this.indexAba--;
			RequestContext.getCurrentInstance().execute("_tabAbas.select(_tabAbas.getActiveIndex()-1)");
		} else {
			this.indexAba -= 2;
			RequestContext.getCurrentInstance().execute("_tabAbas.select(_tabAbas.getActiveIndex()-2)");
		}
		
	}
	

	public void onTabChange(TabChangeEvent event) {
		String activeTab = event.getTab().getId();
		int activeTabIndex = 0;
		for (UIComponent comp : event.getTab().getParent().getChildren()) {
			if (comp.getId().equals(activeTab)) {
				break;
			}
			activeTabIndex++;
		}
		setIndexAba(activeTabIndex);
	}
		
	public Boolean getVisualizaProximo() {
		if (indexAba < abaMAX && getDesabilitaAbas() ) {
			visualizaProximo = true;
		} else {
			visualizaProximo = false;
		}
		return visualizaProximo;
	}
	
	public void setVisualizaProximo(Boolean visualizaProximo) {
		this.visualizaProximo = visualizaProximo;
	}

	public Boolean getVisualizaAnterior() {
		if (indexAba > abaMIN) {
			visualizaAnterior = true;
		} else {
			visualizaAnterior = false;
		}
		return visualizaAnterior;
	}

	public void setVisualizaAnterior(Boolean visualizaAnterior) {
		this.visualizaAnterior = visualizaAnterior;
	}
	
	
	public Boolean getVisualizaFinalizar() {
		if (indexAba == abaMAX) {
			visualizaFinalizar = true;
		} else {
			visualizaFinalizar = false;
		}
		return visualizaFinalizar;
	}

	public void setVisualizaFinalizar(Boolean visualizaFinalizar) {
		this.visualizaFinalizar = visualizaFinalizar;
	}


	public int getIndexAba() {
		return indexAba;
	}


	public void setIndexAba(int indexAba) {
		this.indexAba = indexAba;
	}




	public Boolean getDesabilitaAbas() {
		Boolean b = (Boolean) getAtributoSession(HABILITAABAS);
		if(Util.isNullOuVazio(b)){
			return true;
		}
		else {
			desabilitaAbas = b;
		}
		return desabilitaAbas;
	}

	public void setDesabilitaAbas(Boolean desabilitaAbas) {
		this.desabilitaAbas = desabilitaAbas;
	}

	public Boolean getDesabilitaAbaImovel() {
		Boolean b = (Boolean) getAtributoSession(ABAIMOVEL);
		if (Util.isNullOuVazio(b)){
			return true;
		}
		else {
			desabilitaAbaImovel = b;
		}
		return desabilitaAbaImovel;
	}




	public void setDesabilitaAbaImovel(Boolean desabilitaAbaImovel) {
		this.desabilitaAbaImovel = desabilitaAbaImovel;
	}
	
	
	

}
