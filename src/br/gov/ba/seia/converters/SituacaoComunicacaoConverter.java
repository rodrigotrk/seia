package br.gov.ba.seia.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.gov.ba.seia.util.Util;
@FacesConverter(value = "situacaoComunicacaoConverter")
public class SituacaoComunicacaoConverter implements Converter {
	
	

	@Override
    public Object getAsObject(FacesContext context, UIComponent component, String newValue) {

		if ("I".equalsIgnoreCase(newValue)) {
			return false;
		}
		else if("A".equalsIgnoreCase(newValue)){
			return true;
		}
		else return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {


		if (Boolean.TRUE.equals(value)) {
			return "A";
		}else if(Boolean.FALSE.equals(value)) {
			return "I";
		}else return null;
    }

}
