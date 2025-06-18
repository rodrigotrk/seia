package br.gov.ba.seia.validators;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import org.primefaces.component.calendar.Calendar;

public class DataMinMaxValidator implements Validator {

	@Override
	public void validate(FacesContext arg0, UIComponent arg1, Object data) throws ValidatorException {
		if (data == null) {
            return;
        }		
		
		SimpleDateFormat formatter = new SimpleDateFormat(((Calendar)arg1).getPattern());
		formatter.setLenient(false);
		String dateInString = ((Calendar)arg1).getSubmittedValue().toString();
		String dateMinString = "01/01/1900";

		try {

			Date dateIn = formatter.parse(dateInString);
			
			Date dateMin = formatter.parse(dateMinString);
			
			if(dateIn.after(new Date())) {
				throw new Exception();
			}
			
			if(dateIn.before(dateMin)) {
				throw new Exception();
			}

		} catch (Exception e) {
			FacesMessage message = new FacesMessage();
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			message.setSummary(((Calendar)arg1).getLabel() + " inválida!");				
			throw new ValidatorException(message);
		}       
	}
}
