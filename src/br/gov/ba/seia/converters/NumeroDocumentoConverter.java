package br.gov.ba.seia.converters;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

public class NumeroDocumentoConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String value) {
		
		if(isCpf(value)){
			return cpfAsObject(value);
		}
		else {
			return cnpjAsObject(value);
		}
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object value) {
		String vlo  =(String)value;
		
		if(isCpf(vlo)){
			return cpfAsObject(vlo);
		}
		else {
			return cnpjAsObject(vlo);
		}
	}
	
	private Boolean isCpf(String value){
		if (value.length() == 14){
			return true;
		}
		else {
			return false;
		}
	}
	
	
	private String cnpjAsObject(String value) {
		String cnpj = value;
	      
        if (value!= null && !value.equals(""))
	               cnpj = value.replaceAll("\\.", "").replaceAll("\\-", "").replaceAll("/", ""); 	 
	      return cnpj;
	}
	
	private String cpfAsObject(String value){ 
		StringBuilder builder = new StringBuilder(value);
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
	
}
