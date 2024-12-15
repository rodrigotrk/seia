package br.gov.ba.seia.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import br.gov.ba.seia.entity.Area;
import br.gov.ba.seia.util.Util;

public class AreaAutoCompleteConverter implements Converter  {


	@Override
    public Object getAsObject(FacesContext context, UIComponent component, String newValue) {

		if (!Util.isNull(newValue) && !newValue.isEmpty() && !newValue.equals("null")) { 
			return new Area(Integer.parseInt(newValue));
		} else {
			return null;
		}
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
    	Area area = (Area) value;
		if (!Util.isNullOuVazio(area)) { 
			return  String.valueOf(area);
		}
		else {
			return null;
		}
    }
}