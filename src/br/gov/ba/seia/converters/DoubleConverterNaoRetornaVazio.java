package br.gov.ba.seia.converters;

import java.text.NumberFormat;
import java.util.Locale;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.gov.ba.seia.util.Util;

@FacesConverter("doubleConverterNaoRetornaVazio")
public class DoubleConverterNaoRetornaVazio implements Converter {

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String valor) {

		if (!Util.isNullOuVazio(valor)){
			return Double.valueOf(valor.replace(".", "").replace(",", "."));
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {

		NumberFormat numberFormat =  NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
		
		if (!Util.isNullOuVazio(arg2) && arg2 instanceof Double) {
			return Util.formatarNumero(numberFormat.format(arg2));
		}
		else return Util.formatarNumero(numberFormat.format(new Double(0)));
	}
}