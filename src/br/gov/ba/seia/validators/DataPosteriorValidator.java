package br.gov.ba.seia.validators;

import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.primefaces.component.calendar.Calendar;

import br.gov.ba.seia.util.Util;

public class DataPosteriorValidator implements Validator {

	@Override
	public void validate(FacesContext arg0, UIComponent arg1, Object data) throws ValidatorException {
		Date dataNascimento = (Date) data;
		Date dataAtual = new Date();
		if (!Util.isNull(dataNascimento) && dataNascimento.after(dataAtual)) {
			if(arg1 instanceof Calendar) {
				FacesMessage message = new FacesMessage();
				message.setSeverity(FacesMessage.SEVERITY_ERROR);
				message.setSummary(((Calendar)arg1).getLabel() + " inválida!");				
				throw new ValidatorException(message);	
			}
		}
	}
	
	
	

}
