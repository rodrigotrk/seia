package br.gov.ba.seia.validators;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import br.gov.ba.seia.entity.Telefone;
import br.gov.ba.seia.util.Util;

public class TelefoneValidator implements Validator {

	@SuppressWarnings("unchecked")
	@Override
	public void validate(FacesContext context, UIComponent arg1, Object arg2) throws ValidatorException {
		List<Telefone> listTelefone = (List<Telefone>)context.getApplication().getELResolver().getValue(context.getELContext(), null, "listaTelefone");
		if (Util.isObjectInList(arg2, listTelefone)) {
			FacesMessage message = new FacesMessage();
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			message.setSummary("Telefone já existe");
			throw new ValidatorException(message);
		}
 	}

}
