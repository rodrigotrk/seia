package br.gov.ba.seia.converters;

import java.util.ResourceBundle;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.util.Util;

public class EmpreendimentoConverter implements Converter {
	
	private final String SELECIONE = ResourceBundle.getBundle("/Bundle").getString("geral_lbl_selecione");

	@Override
    public Object getAsObject(FacesContext context, UIComponent component, String newValue) {

		if (!Util.isNullOuVazio(newValue) && !SELECIONE.equals(newValue) && !newValue.equals("Todos")) {
			return new Empreendimento(Integer.parseInt(newValue));
		}
		else return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {

		if (!Util.isNullOuVazio(value)) return String.valueOf(value);
		else return null;
    }
}