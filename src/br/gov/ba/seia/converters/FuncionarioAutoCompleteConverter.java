package br.gov.ba.seia.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import br.gov.ba.seia.entity.Funcionario;
import br.gov.ba.seia.util.Util;

public class FuncionarioAutoCompleteConverter implements Converter  {
	
	@Override
    public Object getAsObject(FacesContext context, UIComponent component, String newValue) {

		if (!Util.isNull(newValue) && !newValue.isEmpty() && !newValue.equals("null")) { 
			return new Funcionario(Integer.parseInt(newValue));
		} else {
			return null;
		}
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
    	Funcionario funcionario = (Funcionario) value;
		if (!Util.isNullOuVazio(funcionario)) {
			return  String.valueOf(funcionario);
		} else {
			return null;
		}
    }

}
