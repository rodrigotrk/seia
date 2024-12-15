package br.gov.ba.seia.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import br.gov.ba.seia.entity.Area;
import br.gov.ba.seia.util.Util;

public class AreaTipologiaConverter implements Converter  {

	private static final String SELECIONE = "Selecione...";

	@Override
    public Object getAsObject(FacesContext context, UIComponent component, String newValue) {
		if (!Util.isNullOuVazio(newValue) && !SELECIONE.equals(newValue)) {
			String[] str = newValue.split(";");
			String id = str[0];
			String nome = str[1];
			return new Area(Integer.parseInt(id), nome, null, false);
		} else { 
			return null;
		}
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (!Util.isNullOuVazio(value)) { 
			Area area = (Area)value;
			String codigo = area.getIdeArea() + ";" + area.getNomArea();
			return codigo;
		} else {
			return null;
		}
    }
}