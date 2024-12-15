/**
 * 
 */
package br.gov.ba.seia.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import br.gov.ba.seia.entity.DocumentoObrigatorio;

/**
 * @author mario.junior
 */
public class DocumentoObrigatorioConverter implements Converter {

	private static final String SELECIONE = "Selecione...";
	@Override
 	public Object getAsObject(FacesContext context, UIComponent component, String newValue) {
		
		if (!"null".equals(newValue) && !SELECIONE.equals(newValue) &&(!"".equals(newValue)) ){
				
			DocumentoObrigatorio d = new DocumentoObrigatorio(Integer.parseInt(newValue));
			return d;
		}			
		else{
			return null;
		}
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		
		if (!"null".equals(value) && !SELECIONE.equals(value) &&(!"".equals(value)) ){
		
			DocumentoObrigatorio docObrigatorio = (DocumentoObrigatorio) value;
			return docObrigatorio.toString();
		}
		return null;
	}

}
