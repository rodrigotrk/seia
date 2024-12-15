package br.gov.ba.seia.converters;

import java.math.BigDecimal;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import br.gov.ba.seia.util.Util;


public class BigConverterIntToDecimal implements Converter {

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String valor) {

		if (!Util.isNullOuVazio(valor)) return BigDecimal.valueOf(Double.valueOf(valor.replace(".", "").replace(",", ".")));
		return null;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		BigDecimal bd = (BigDecimal) arg2;
		if (!Util.isNullOuVazio(arg2) && arg2 instanceof BigDecimal) return bd.toString().replace(".", ",");
		else return null;
	}
}