package br.gov.ba.seia.faces;

import java.lang.annotation.Annotation;
import java.util.Map;

import javax.enterprise.context.spi.Context;
import javax.enterprise.context.spi.Contextual;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.faces.context.FacesContext;
/**
 * Classe que trata do view scope do SEIA
 * @author 
 *
 */
public class ViewContext implements Context {
	 @Override
	    public Class<? extends Annotation> getScope() {
	        return ViewScoped.class;
	    }

	    @SuppressWarnings({"unchecked"})
	    @Override
	    public <T> T get(Contextual<T> contextual, CreationalContext<T> creationalContext) {
	        Bean<T> bean = (Bean<T>)contextual;
	        Map<String,Object> viewMap = getViewMap();
	        if(viewMap.containsKey(bean.getName())) {
	            return (T) viewMap.get(bean.getName());
	        } else {
	            T t = bean.create(creationalContext);
	            viewMap.put(bean.getName(), t);
	            return t;
	        }
	    }

	    @SuppressWarnings({"unchecked"})
	    @Override
	    public <T> T get(Contextual<T> contextual) {
	        Bean<T> bean = (Bean<T>)contextual;
	        Map<String,Object> viewMap = getViewMap();
	        if(viewMap.containsKey(bean.getName()))
	            return (T) viewMap.get(bean.getName());

	        return null;
	    }

	    @Override
	    public boolean isActive() {
	        return true;
	    }

	    private Map<String,Object> getViewMap() {
	        return FacesContext.getCurrentInstance().getViewRoot().getViewMap(true);
	    }
}