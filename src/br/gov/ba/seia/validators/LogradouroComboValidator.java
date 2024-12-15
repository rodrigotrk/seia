package br.gov.ba.seia.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import br.gov.ba.seia.entity.Logradouro;
import br.gov.ba.seia.util.Util;

public class LogradouroComboValidator implements Validator {
	
	@Override
	public void validate(FacesContext arg0, UIComponent arg1, Object arg2) throws ValidatorException {

		Logradouro logradouro = (Logradouro) arg2;

		if (Util.isNullOuVazio(logradouro) || Util.isNullOuVazio(logradouro.getIdeLogradouro())) {
			FacesMessage message = new FacesMessage();
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			message.setSummary("O campo Logradouro deve ser selecionado.");
			throw new ValidatorException(message);
		}
	}
}