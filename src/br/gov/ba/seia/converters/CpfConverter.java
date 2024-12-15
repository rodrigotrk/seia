package br.gov.ba.seia.converters;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import br.gov.ba.seia.util.Util;

/**
 * 
 * @author monique.dantas
 *
 */
public class CpfConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		 StringBuilder builder = new StringBuilder(arg2);
		    boolean encontrouCaracterInvalido = false;
		    int i = 0;
		    while ( i < builder.length() && !encontrouCaracterInvalido) {
		      char c = builder.charAt(i);
		      if (Character.isDigit(c))
		        i++;
		      else
		        if (Character.isDefined('.'))
		          builder.deleteCharAt(i);
		        else
		          if (Character.isDefined('-'))
		            builder.deleteCharAt(i);
		          else 
		            encontrouCaracterInvalido = true;
		    }
		    if (encontrouCaracterInvalido) {
		      FacesMessage message = new FacesMessage("Ocorreu um erro de conversão. ","CPF inválido");
		      message.setSeverity(FacesMessage.SEVERITY_ERROR);
		      throw new ConverterException(message);
		    }
		    return builder.toString();
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
	    if (!Util.isNullOuVazio(arg2)) {
	    	String v = arg2.toString();
		    StringBuilder builder = new StringBuilder();
		    int tam = v.length();
		    for(int i = 0; i < tam; i++) {
		      if (i == 3 || i== 6)
		        builder.append(".");
		      else
		        if (i == 9)
		          builder.append("-");
		      if (i < 11)
		        builder.append(v.charAt(i));
		      else
		        break;
		    }
		    return builder.toString();
		} else {
			return null;
		}		 
	  }


}
