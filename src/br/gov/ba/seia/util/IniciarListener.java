package br.gov.ba.seia.util;

import java.util.HashMap;
import java.util.Iterator;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.faces.lifecycle.Lifecycle;

import br.gov.ba.seia.interfaces.PropriedadesSistemaIf;

public class IniciarListener implements PhaseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6L;

	public void afterPhase(PhaseEvent pEvento) {

	}

	@SuppressWarnings("rawtypes")
	public void beforePhase(PhaseEvent pEvento) {
		
		if(pEvento.getPhaseId() == PhaseId.RESTORE_VIEW) {
			
	        @SuppressWarnings("static-access")
			FacesContext lContexto = pEvento.getFacesContext().getCurrentInstance();
	        
	        HashMap lColNomesPageBeans = (HashMap) lContexto.getExternalContext().getSessionMap().get(PropriedadesSistemaIf.ATRIBUTO_SESSAO_COL_NOMES_PAGE_BEANS);
	
	        String lURL = lContexto.getExternalContext().getRequestServletPath();
	
	        if (lColNomesPageBeans != null && lURL != null) {
	
	            Object lPageBean = lColNomesPageBeans.get(lURL);
	
	            if (lPageBean != null && lPageBean instanceof SeiaControllerAb) {
	
	            	SeiaControllerAb lManagedBean = (SeiaControllerAb) lPageBean;
	
	           		lManagedBean.iniciar();
	            }
	        }
		}
        
        if (pEvento.getPhaseId() == PhaseId.RENDER_RESPONSE && !Util.isNullOuVazio(SessaoUtil.recuperarObjetoSessao("MENSAGEM_PREENCHIMENTO_QUESTIONARIO_OBRIGATORIO"))) {
        	
        	JsfUtil.addErrorMessage((String) SessaoUtil.recuperarObjetoSessao("MENSAGEM_PREENCHIMENTO_QUESTIONARIO_OBRIGATORIO"));
        	SessaoUtil.removerObjetoSessao("MENSAGEM_PREENCHIMENTO_QUESTIONARIO_OBRIGATORIO");
        }
	}

	public PhaseId getPhaseId() {

		return PhaseId.ANY_PHASE;
	}
}