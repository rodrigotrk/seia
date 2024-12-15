package br.gov.ba.seia.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.primefaces.component.picklist.PickList;
import org.primefaces.model.DualListModel;

import br.gov.ba.seia.entity.Municipio;


@FacesConverter(value = "pickListMunicipioConverter")
public class PickListMunicipioConverter  implements Converter {

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
	   
		Object ret = null;
	    if (arg1 instanceof PickList) {
	    	
	        Object dualList = ((PickList) arg1).getValue();
	        @SuppressWarnings("rawtypes")
			DualListModel dl = (DualListModel) dualList;
	        
	        for (Object o : dl.getSource()) {
	            String id = "" + ((Municipio) o).getIdeMunicipio();
	            if (arg2.equals(id)) {
	                ret = o;
	                break;
	            }
	        }
	        if (ret == null)
	            for (Object o : dl.getTarget()) {
	                String id = "" + ((Municipio) o).getIdeMunicipio();
	                if (arg2.equals(id)) {
	                    ret = o;
	                    break;
	                }
	            }
	    }
	    return ret;
	
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		
		String str = "";
		    if (arg2 instanceof Municipio) {
		        str = "" + ((Municipio) arg2).getIdeMunicipio();
		    }
		    return str;
		
	}

	

	
	
	
	
	
}