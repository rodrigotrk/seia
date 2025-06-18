package br.gov.ba.seia.converters;

import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import br.gov.ba.seia.entity.generic.AbstractEntity;
import br.gov.ba.seia.interfaces.BaseEntity;

public class SimpleEntityConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext ctx, UIComponent component, String value) {
		if (value != null) {
            return this.getAttributesFrom(component).get(value);
        }
        return null;
	}

	@Override
	public String getAsString(FacesContext ctx, UIComponent component, Object value) {
		Integer id = 0;
		
		if(value instanceof BaseEntity){
			BaseEntity entity = (BaseEntity) value;
			if(entity == null || entity.getId()== null){
				id = 0;
			}

			id = entity.getId().intValue();
		}
		
		else if(value instanceof AbstractEntity){
			AbstractEntity entity = (AbstractEntity) value;
			if(entity == null || entity.getId()== null){
				id = 0;
			}

			id = entity.getId();
		}
		
		if(value!=null){
			component.getAttributes().put(id.toString(), value);
		}

		return String.valueOf(id);
	}

	protected void addAttribute(UIComponent component, BaseEntity o) {
        String key = o.getId().toString();
        this.getAttributesFrom(component).put(key, o);
    }
  
    protected Map<String, Object> getAttributesFrom(UIComponent component) {
        return component.getAttributes();
    }
	
}
