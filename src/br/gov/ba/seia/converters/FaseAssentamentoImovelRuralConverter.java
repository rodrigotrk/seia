package br.gov.ba.seia.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import br.gov.ba.seia.entity.FaseAssentamentoImovelRural;
import br.gov.ba.seia.util.Util;

public class FaseAssentamentoImovelRuralConverter implements Converter  {

	private static final String SELECIONE = "Selecione...";

	@Override
    public Object getAsObject(FacesContext context, UIComponent component, String newValue) {

		if (!Util.isNullOuVazio(newValue) && !SELECIONE.equals(newValue)) return new FaseAssentamentoImovelRural(Integer.parseInt(newValue));
		else return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {

		if (!Util.isNullOuVazio(value)) return String.valueOf(value);
		else return null;
    }

}
