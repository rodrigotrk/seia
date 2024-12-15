package br.gov.ba.seia.validators;

import java.util.ResourceBundle;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import br.gov.ba.seia.util.ValidarCoordenada;


@FacesValidator("latitudeValidator")
public class LatitudeValidator implements Validator {
	
	private final ResourceBundle BUNDLE = ResourceBundle.getBundle("/Bundle");
	private final Integer MAX_LATITUDE = Integer.parseInt(BUNDLE.getString("max_latitude"));
	private final Integer MIN_LATITUDE = Integer.parseInt(BUNDLE.getString("min_latitude"));
	
	
	@Override
	public void validate(FacesContext arg0, UIComponent arg1, Object arg2) throws ValidatorException {
		
		String idDoCampo =  arg1.getClientId().replace(":", "\\\\:");
		String valorDoCampo = ((String) arg2).replace(",", ".");
		
		ValidarCoordenada.validarCampo(idDoCampo, valorDoCampo, MIN_LATITUDE, MAX_LATITUDE, ValidarCoordenada.LATITUDE);
	}
}
