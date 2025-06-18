package br.gov.ba.seia.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.util.Util;

public class TipologiaConverter implements Converter  {

	private static final String SELECIONE = "Selecione...";
	private final String TODOS = "Todos"; //ResourceBundle.getBundle("/Bundle").getString("geral_lbl_todos");
	
	@Override
    public Object getAsObject(FacesContext context, UIComponent component, String newValue) {

		if (!Util.isNullOuVazio(newValue) && !(SELECIONE.equals(newValue) || TODOS.equals(newValue))) {
			
			return new Tipologia(Integer.parseInt(newValue));
		}
		return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {

		if (!Util.isNullOuVazio(value)) return String.valueOf(value);
		else return null;
    }
}