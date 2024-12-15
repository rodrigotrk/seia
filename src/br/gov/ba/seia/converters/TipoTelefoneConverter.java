/**package br.gov.ba.seia.controller.util;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import br.gov.ba.seia.entity.TipoTelefone;

public class TipoTelefoneConverter implements Converter  {

	@Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
       TipoTelefone tipoTelefone = null;
        try {
        	String[] split = value.split(";");
        	String ideTipoTelefone = split[0];
        	String nomTipoTelefone = split[1];
        	tipoTelefone = new TipoTelefone(Integer.parseInt(ideTipoTelefone));
        	tipoTelefone.setNomTipoTelefone(nomTipoTelefone);
        }catch(Throwable ex) {
        	Log4jUtil.log(this.getClass().getName(),Level.ERROR, ex);
        }
        return tipoTelefone;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
    	TipoTelefone tipoTelefone = (TipoTelefone) value;
    	return tipoTelefone.getIdeTipoTelefone().toString() + ";" + tipoTelefone.getNomTipoTelefone();
    }
    

} **/


package br.gov.ba.seia.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import br.gov.ba.seia.entity.TipoTelefone;
import br.gov.ba.seia.util.Util;

public class TipoTelefoneConverter implements Converter  {

	private static final String SELECIONE = "Selecione...";

	@Override
    public Object getAsObject(FacesContext context, UIComponent component, String newValue) {

		if (!Util.isNullOuVazio(newValue) && !SELECIONE.equals(newValue)) return new TipoTelefone(Integer.valueOf(newValue));
		else return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {

		if (!Util.isNullOuVazio(value)) return String.valueOf(value);
		else return null;
    }
}
