package br.gov.ba.seia.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import br.gov.ba.seia.util.Util;


public class CepConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String valor) {
		
		if (!Util.isNullOuVazio(valor)) {

			StringBuilder str = new StringBuilder();

			if (!Util.isEmptyString(valor)) {

				String[] splitTraco = valor.split("-");
				String[] splitPonto = splitTraco[0].split("\\.");
				str.append(splitPonto[0]).append(splitPonto[1]).append(splitTraco[1]);
			}
			try{
				return new Integer(str.toString().trim());
			}catch (NumberFormatException e) {
				return null;
			}
		}
		else {

			return null;
		}
	}


	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {

		if (!Util.isNullOuVazio(arg2)) {

			StringBuilder str = new StringBuilder();

			if (!Util.isNull(arg2)) {

				String valor = arg2.toString().trim();
				if (!Util.isNullOuVazio(valor) && valor.length() < 8) {
					valor = Util.lpad(valor, '0', 8);
				}else
					valor = valor.toString();				
					str.append(valor.substring(0,2)).append(".").append(valor.substring(2,5)).append("-").append(valor.substring(5,8));
			}

			return str.toString();
		}
		else {
			return null;
		}
	}
}