package br.gov.ba.seia.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import br.gov.ba.seia.entity.Estado;
import br.gov.ba.seia.util.Util;

public class EstadoValidator implements Validator {

	@Override
	public void validate(FacesContext arg0, UIComponent arg1, Object arg2) throws ValidatorException {
		Estado estado = (Estado)arg2;
		if (!Util.isNullOuVazio(estado) && estado.getIdeEstado().equals(0)) {
			FacesMessage message = new FacesMessage();
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			message.setSummary("O campo Estado deve ser selecionado.");
			throw new ValidatorException(message);
		}
	}
}
