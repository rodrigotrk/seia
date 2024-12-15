package br.gov.ba.seia.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.primefaces.component.picklist.PickList;
import org.primefaces.model.DualListModel;

import br.gov.ba.seia.entity.ProcessoAto;
import br.gov.ba.seia.entity.TipoSeguimentoPct;


@FacesConverter(value = "PickListConverter")
public class PickListConverter  implements Converter {

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
	   
		Object ret = null;
	    if (arg1 instanceof PickList) {
	    	
	        Object dualList = ((PickList) arg1).getValue();
	        
	        @SuppressWarnings("rawtypes")
			DualListModel dl = (DualListModel) dualList;
	        
	        for (Object o : dl.getSource()) {
	        	
	        	if (o instanceof ProcessoAto) {
		            String id = ((ProcessoAto) o).getIdeProcessoAto().toString();
		            if (arg2.equals(id)) {
		                ret = o;
		                break;
		            }
	        	}
	        	else if (o instanceof TipoSeguimentoPct) {
		            String id = ((TipoSeguimentoPct) o).getIdeTipoSeguimentoPct().toString();
		            if (arg2.equals(id)) {
		                ret = o;
		                break;
		            }
	        	}
	        }
	        
	        if (ret == null) {
	            for (Object o : dl.getTarget()) {
	            	if (o instanceof ProcessoAto) {
		                String id = ((ProcessoAto) o).getIdeProcessoAto().toString();
		                if (arg2.equals(id)) {
		                    ret = o;
		                    break;
		                }
	            	} 
	            	else if (o instanceof TipoSeguimentoPct) {
		                String id = ((TipoSeguimentoPct) o).getIdeTipoSeguimentoPct().toString();
		                if (arg2.equals(id)) {
		                    ret = o;
		                    break;
		                }
	            	}
	            }
	        }
	    }
	    
	    return ret;
	
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		
		String str = "";
	    if (arg2 instanceof ProcessoAto) {
	        str = ((ProcessoAto) arg2).getIdeProcessoAto().toString();
	    }
	    else if (arg2 instanceof TipoSeguimentoPct) {
	        str = ((TipoSeguimentoPct) arg2).getIdeTipoSeguimentoPct().toString();
	    }
		
	    return str;
		
	}
	
}