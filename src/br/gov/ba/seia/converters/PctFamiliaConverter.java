package br.gov.ba.seia.converters;

import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.gov.ba.seia.entity.PctFamilia;

@FacesConverter("pctFamiliaConverter")
public class PctFamiliaConverter implements Converter {
	
	@Override
	public Object getAsObject(FacesContext ctx, UIComponent component, String value) {
		if (value != null) {
            return this.getAttributesFrom(component).get(value);
        }
        return null;
	}

	@Override
	public String getAsString(FacesContext ctx, UIComponent component, Object value) {
		if (value instanceof PctFamilia) {
		    addAttribute(component, ((PctFamilia)value));
			return ((PctFamilia)value).getIdePessoa().getPessoaFisica().getNumCpf();
		}
		return null;
	}
	
	protected void addAttribute(UIComponent component, PctFamilia pctFamilia) {
        String cpf = pctFamilia.getIdePessoa().getPessoaFisica().getNumCpf();
        this.getAttributesFrom(component).put(cpf, pctFamilia);
    }

	protected Map<String, Object> getAttributesFrom(UIComponent component) {
		return component.getAttributes();
	}

}