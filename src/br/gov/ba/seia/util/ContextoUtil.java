package br.gov.ba.seia.util;

import javax.faces.context.FacesContext;

import br.gov.ba.seia.controller.ContextoController;

public class ContextoUtil {
	public static ContextoController getContexto(){
		ContextoController contextoBean = getContextoController();
		return contextoBean;
	}
	
	
	@SuppressWarnings("deprecation")
	private static ContextoController getContextoController() {
		try {
			FacesContext faces = FacesContext.getCurrentInstance();  
			ContextoController bean = (ContextoController) faces.getApplication().getVariableResolver().resolveVariable(faces, "contextoController");  
			bean.getPessoa();
			return bean;
		}
		catch(Exception e) {
			return null;
		}
	}
}
