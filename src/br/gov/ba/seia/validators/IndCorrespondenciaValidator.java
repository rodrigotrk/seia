package br.gov.ba.seia.validators;

import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

public class IndCorrespondenciaValidator implements Validator {

	
	private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("/Bundle");
	
	@Override
	public void validate(FacesContext arg0, UIComponent arg1, Object arg2)
			throws ValidatorException {
     
		Boolean vlo = (Boolean) arg2;
		if(!vlo) {
			FacesMessage message = new FacesMessage();
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			message.setSummary(BUNDLE.getString("empreendimento_lbl_check_ind_correspondencia"));
			throw new ValidatorException(message);	
		}
	}

}