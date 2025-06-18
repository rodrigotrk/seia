package br.gov.ba.seia.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import br.gov.ba.seia.entity.Ocupacao;

public class OcupacaoComboValidator implements Validator {

	@Override
	public void validate(FacesContext arg0, UIComponent arg1, Object arg2) throws ValidatorException {
		Ocupacao ocupacao = (Ocupacao) arg2;
		
		if(ocupacao == null){
			FacesMessage message = new FacesMessage();
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			message.setSummary("O campo ocupação deve ser selecionado.");
			throw new ValidatorException(message);
			
		}
	}

}
