package br.gov.ba.seia.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import br.gov.ba.seia.util.Util;

public class EmailValidator implements Validator {

	@Override
	public void validate(FacesContext arg0, UIComponent arg1, Object arg2) {

		if (!Util.isNull(arg2) && !Util.isEmptyString(arg2.toString()) && !Util.validaEmail(arg2.toString())) {

			FacesMessage lFacesMensagem = new FacesMessage();

			lFacesMensagem.setSeverity(FacesMessage.SEVERITY_ERROR);
			lFacesMensagem.setSummary("E-mail inválido!");

			throw new ValidatorException(lFacesMensagem);
		}
	}
}