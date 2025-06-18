package br.gov.ba.seia.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import br.gov.ba.seia.entity.Mes;

public class MesConverter extends SimpleEntityConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext ctx, UIComponent component, String value) {
		if (value != null && !value.equals("")) {
            return this.getAttributesFrom(component).get(value);
        }
        return new Mes();
	}
	
}
