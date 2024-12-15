package br.gov.ba.seia.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import br.gov.ba.seia.entity.UnidadeMedida;
import br.gov.ba.seia.util.Util;

public class UnidadeMedidaConverter implements Converter  {

	private static final String SELECIONE = "Selecione...";

	@Override
    public Object getAsObject(FacesContext context, UIComponent component, String newValue) {
		if (!Util.isNullOuVazio(newValue) && !SELECIONE.equals(newValue)){
			String[] str = newValue.split(";");
			String id = str[0];
			String nome = str[1];
			return new UnidadeMedida(Integer.parseInt(id), null, nome);
			
		}
		else {
			return null;
		}
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (!Util.isNullOuVazio(value)){
			UnidadeMedida unidadeMedida = (UnidadeMedida)value;
			String codigo = unidadeMedida.getIdeUnidadeMedida() + ";" + unidadeMedida.getNomUnidadadeMedida();
			return codigo;
		}else{
			return null;
		}
    }
}