/**
 * 
 */
package br.gov.ba.seia.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.apache.log4j.Level;

import br.gov.ba.seia.entity.DocumentoAto;
import br.gov.ba.seia.util.Log4jUtil;

/**
 * @author mario.junior
 */
public class DocumentoAtoConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String newValue) {
		DocumentoAto documentoAto = null;
		try {
			documentoAto = new DocumentoAto();
			if (newValue != null && !newValue.trim().isEmpty()) {
				Integer ideDocumentoObrigatorio = Integer.parseInt(newValue.substring(newValue.indexOf("=", newValue.indexOf("=")) + 1, newValue.indexOf(", ")).trim());
				Integer ideAtoAmbiental = Integer.parseInt(newValue.substring(newValue.lastIndexOf("=") + 1, newValue.lastIndexOf(" ")).trim());
			}
		} catch (Throwable ex) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR,ex );
		}
		return documentoAto;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		DocumentoAto documentoAto = (DocumentoAto) value;
		return documentoAto.toString();
	}

}
