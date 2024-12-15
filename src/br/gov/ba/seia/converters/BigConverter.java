package br.gov.ba.seia.converters;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import br.gov.ba.seia.util.Util;


public class BigConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String valor) {

		if (!Util.isNullOuVazio(valor)) return BigDecimal.valueOf(Double.valueOf(valor.replace(".", "").replace(",", ".")));
		return null;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		Locale ptBr = new Locale("pt", "BR"); 
		if (!Util.isNullOuVazio(arg2) && arg2 instanceof BigDecimal) return NumberFormat.getInstance(ptBr).format(arg2);
		else return null;
	}
}