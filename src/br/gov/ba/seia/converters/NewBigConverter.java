package br.gov.ba.seia.converters;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Locale;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.gov.ba.seia.util.Util;

@FacesConverter("bigDecimalConverterNaoRetornaVazio")
public class NewBigConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String valor) {
		if (!Util.isNullOuVazio(valor)) {
			return new BigDecimal(valor.replace(".", "").replace(",", "."));
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		Locale.setDefault(new Locale("pt", "BR"));
		DecimalFormat df = Util.getDecimalFormatPtBr();
		return df.format(arg2);
	}
}