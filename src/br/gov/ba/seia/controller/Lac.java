package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;

import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.Certificado;
import br.gov.ba.seia.entity.LacLegislacao;
import br.gov.ba.seia.entity.Legislacao;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.enumerator.AtoAmbientalEnum;
import br.gov.ba.seia.util.CertificadoUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.SeiaControllerAb;
import br.gov.ba.seia.util.Util;

public abstract class Lac extends SeiaControllerAb {

	protected int activeTab;

	protected Collection<LacLegislacao> legislacoes;
	
	@Inject
	private CertificadoUtil certificadoUtil;

	protected Certificado gerarCertificado(AtoAmbiental atoAmbiental, Requerimento requerimento) {
		try {
			return certificadoUtil.gerarCertificado(atoAmbiental, requerimento);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			return null;
		}
	}

	public void controlarAbas(TabChangeEvent tabChangeEvent) {
	}

	public void exibirTermos() {
	}

	public void render() {
	}

	public boolean isTodosTermosAceites() {
		boolean valido = true;
		for (LacLegislacao legislacao : this.legislacoes) {
			if (Util.isNull(legislacao.getIndAceitacao()) || !legislacao.getIndAceitacao()) {
				valido = false;
				break;
			}
		}
		return valido;
	}
	
	public boolean isTermosAceites() {
		boolean valido = true;
		for (LacLegislacao legislacao : this.legislacoes) {
			if (Util.isNull(legislacao.getIndAceitacao()) || !legislacao.getIndAceitacao()) {
				valido = false;
				JsfUtil.addErrorMessage("Todos os termos devem ser aceites.");
				break;
			}
		}
		return valido;
	}

	public void resetarTermos() {
		if (!Util.isNull(legislacoes)) {
			for (LacLegislacao legislacao : legislacoes) {
				legislacao.setIndAceitacao(false);
			}
		}
	}

	public void aceitarTermo(LacLegislacao legislacao) {
		if (Util.isNull(legislacao.getIndAceitacao()) || !legislacao.getIndAceitacao()) {
			legislacao.setIndAceitacao(true);
		} else {
			legislacao.setIndAceitacao(false);
		}
	}
	
	public void aceitarTermo(LacLegislacao legislacao,boolean checked) {
		legislacao.setIndAceitacao(checked);
	}
	
	public void negarTermos(String modal){
		this.resetarTermos();
		JsfUtil.addWarnMessage("O empreendimento não está regular perante a legislação ambiental. Aceite os termos exibidos anteriomente para permitir tal regularização.");
		RequestContext.getCurrentInstance().execute(modal+".hide();");
	}

	public void carregarLegislacoes(Collection<Legislacao> legislacoes) {
		try {
			Collection<LacLegislacao> listaTemporaria = new ArrayList<LacLegislacao>();
			for (Legislacao legislacao : legislacoes) {
				LacLegislacao lacLegislacao = new LacLegislacao();
				lacLegislacao.setLegislacao(legislacao);
				listaTemporaria.add(lacLegislacao);
			}
			if(Util.isNull(this.legislacoes)){
				this.legislacoes = new ArrayList<LacLegislacao>();
			}
			
			for (LacLegislacao lacLegislacao : listaTemporaria) {
				if(!this.legislacoes.contains(lacLegislacao)){
					this.legislacoes.add(lacLegislacao);
				}
			}
			
		} catch (Exception exception) {
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	protected abstract String getCondicionantesFormularioLAC();

	protected AtoAmbiental getAtoAmbientalLac() {
		AtoAmbientalEnum atoAmbientalLac = AtoAmbientalEnum.LAC;
		AtoAmbiental atoAmbiental = new AtoAmbiental(atoAmbientalLac.getId(), atoAmbientalLac.getSigla());
		return atoAmbiental;
	}

	public Collection<LacLegislacao> getLegislacoes() {
		return legislacoes;
	}

	public void setLegislacoes(Collection<LacLegislacao> legislacoes) {
		this.legislacoes = legislacoes;
	}

	public int getActiveTab() {
		return activeTab;
	}

	public void setActiveTab(int activeTab) {
		this.activeTab = activeTab;
	}
	

}
