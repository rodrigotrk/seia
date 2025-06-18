/**
 * 
 */
package br.gov.ba.seia.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import br.gov.ba.seia.entity.DocumentoImovelRural;
import br.gov.ba.seia.util.Util;

/**
 * @author carlos.cruz
 */
public class DocumentoImovelRuralConverter implements Converter {

	private static final String SELECIONE = "Selecione...";
	@Override
 	public Object getAsObject(FacesContext context, UIComponent component, String newValue) {
		if (!Util.isNullOuVazio(newValue) && !SELECIONE.equals(newValue)){
			return new DocumentoImovelRural(Integer.parseInt(newValue));
		}			
		else{
			return null;
		}
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (!Util.isNullOuVazio(value)) {
			DocumentoImovelRural docObrigatorio = (DocumentoImovelRural) value;
			return docObrigatorio.toString();
		} else {
			return null;
		}
	}

}
