package br.gov.ba.seia.converters;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import br.gov.ba.seia.util.Util;

public class IntegerConverter implements Converter  {

	@Override
    public Object getAsObject(FacesContext context, UIComponent component, String newValue) {

		if (!Util.isNullOuVazio(newValue)) {

			boolean lNumeroInvalido = false;
			Integer numLimit = 9;
			
			if (newValue.length() <= 15 && component.getId().toString().equalsIgnoreCase("CCIRINCRA")) {
				for (int lIndice = 0; lIndice < newValue.length(); lIndice++) {
					if (!Character.isDigit(newValue.charAt(lIndice))) {
						numLimit = 15;
						lNumeroInvalido = true;
						break;
					}
				}
			}
			else if (newValue.length() <= 9) {
				for (int lIndice = 0; lIndice < newValue.length(); lIndice++) {

					if (!Character.isDigit(newValue.charAt(lIndice))) {

						lNumeroInvalido = true;
						break;
					}
				}
			}
			else {
				lNumeroInvalido = true;
			}

			if (lNumeroInvalido) {
				FacesMessage lMensagem = new FacesMessage();
				if(!component.getId().toString().equalsIgnoreCase("fracaoIdealImovel")){
					lMensagem.setSummary("Erro de conversão: Favor informar um valor inteiro com no máximo "+numLimit+" casas decimais.");
				} else {
					lMensagem.setSummary("Erro de conversão: Favor informar um valor inteiro");
				}

				lMensagem.setSeverity(FacesMessage.SEVERITY_ERROR);
				throw new ConverterException(lMensagem);
			}
			else {		      
				return Integer.valueOf(newValue);
			}
		}
		else return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {

		if (!Util.isNullOuVazio(value)) return String.valueOf(value);
		else return null;
    }
}