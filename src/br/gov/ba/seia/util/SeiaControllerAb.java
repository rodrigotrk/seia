package br.gov.ba.seia.util;

import java.util.HashMap;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.selectmanycheckbox.SelectManyCheckbox;
import org.primefaces.component.selectoneradio.SelectOneRadio;
import org.primefaces.context.RequestContext;

import br.gov.ba.seia.interfaces.PropriedadesSistemaIf;

public abstract class SeiaControllerAb {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public SeiaControllerAb() {

		if (getNomeURL() != null){

			HashMap lColNomesPageBeans = (HashMap)getAtributoSession(PropriedadesSistemaIf.ATRIBUTO_SESSAO_COL_NOMES_PAGE_BEANS);

    		if (lColNomesPageBeans == null) {

    			lColNomesPageBeans = new HashMap();
    		}

    		lColNomesPageBeans.put(getNomeURL(), this);

    		addAtributoSessao(PropriedadesSistemaIf.ATRIBUTO_SESSAO_COL_NOMES_PAGE_BEANS, lColNomesPageBeans);
        }
	}

	/**
	* Limpa os dados dos componentes de edição e de seus filhos,
	* recursivamente. Checa se o componente é instância de EditableValueHolder
	* e 'reseta' suas propriedades.
	*/
	public void limparComponentesFormulario(UIComponent pComponente) {

		if (pComponente instanceof EditableValueHolder) {
			EditableValueHolder lComponenteEditavel = (EditableValueHolder) pComponente;
			lComponenteEditavel.setSubmittedValue(null);
			lComponenteEditavel.setValue(null);
			lComponenteEditavel.setLocalValueSet(false);
			lComponenteEditavel.setValid(true);
		}

		if (!Util.isNull(pComponente) &&  pComponente.getChildCount() > 0) {

			for (UIComponent lComponenteFilho : pComponente.getChildren()) {
				limparComponentesFormulario(lComponenteFilho);
			}
		}
	}

	public void desabilitarCampos(UIComponent pComponente) {

		if (pComponente instanceof InputText) {
			InputText lComponenteEditavel = (InputText) pComponente;
			lComponenteEditavel.setDisabled(true);
		}

		if (pComponente instanceof SelectOneRadio) {
			SelectOneRadio lComponenteEditavel = (SelectOneRadio) pComponente;
			lComponenteEditavel.setDisabled(true);
		}
		
		if (pComponente instanceof SelectManyCheckbox) {
			SelectManyCheckbox lComponenteEditavel = (SelectManyCheckbox) pComponente;
			lComponenteEditavel.setDisabled(true);
		}

		if (pComponente.getChildCount() > 0) {

			for (UIComponent lComponenteFilho : pComponente.getChildren()) {
				desabilitarCampos(lComponenteFilho);
			}
		}
	}

    protected void addAtributoSessao(String pNomeAtributo, Object pValor) {
    	
    	getSession().setAttribute(pNomeAtributo, pValor); 
    }

    protected HttpSession getSession() {
    	
    	return getRequest().getSession();
    }

    protected HttpServletRequest getRequest() {

    	return (HttpServletRequest) getContextoExterno().getRequest();
    }

    protected Object getAtributoSession(String pNomeAtributo) {

        return getAtributosSession().get(pNomeAtributo); 
    }

    @SuppressWarnings("rawtypes")
	protected Map getAtributosSession() {

        return getContextoExterno().getSessionMap();
    }

    protected String getParametroRequest(String pNomeParametro) {

        return (String) getParametrosRequest().get(pNomeParametro); 
    }

    @SuppressWarnings("rawtypes")
	protected Map getParametrosRequest() {

        return getContextoExterno().getRequestParameterMap();
    }

    protected ExternalContext getContextoExterno() {

        return getCurrentInstante().getExternalContext();
    }

    public void addMensagemErro(String pSummary, String pDetail) {

    	getCurrentInstante().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, pSummary, pDetail));
    }

    public void addMensagemInformativa(String pSummary, String pDetail) {

    	getCurrentInstante().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, pSummary, pDetail));
    }

    protected FacesContext getCurrentInstante() {

        return FacesContext.getCurrentInstance();
    }

    protected RequestContext getRequestContext() {

    	return RequestContext.getCurrentInstance();
    }

    public String getNomeURL() {
		
		return null;
	}
	/**
	 * Método chamado automaticamente antes da fase restaurar visão
	 * pelo IniciarListener.
	 * Pode ser implementado nas subclasses.
	 */
	public void iniciar() {
	}
}