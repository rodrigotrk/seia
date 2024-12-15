package br.gov.ba.seia.converters;

import java.util.ResourceBundle;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import br.gov.ba.seia.entity.StatusFluxo;
import br.gov.ba.seia.util.Util;

public class StatusFluxoConverter implements Converter {
	
	private final String SELECIONE = ResourceBundle.getBundle("/Bundle").getString("geral_lbl_selecione");
	private final String TODOS = "Todos"; //ResourceBundle.getBundle("/Bundle").getString("geral_lbl_todos");

	@Override
    public Object getAsObject(FacesContext context, UIComponent component, String newValue) {

		if (!Util.isNullOuVazio(newValue) && (SELECIONE.equals(newValue) || TODOS.equals(newValue))) {
			return null;
		}
		else if(!Util.isNullOuVazio(newValue)){
			return new StatusFluxo(Integer.parseInt(newValue));
		}
		else return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {

		if (!Util.isNullOuVazio(value)) {
			return String.valueOf(value);
		}
		else return null;
    }
    
    
    
}