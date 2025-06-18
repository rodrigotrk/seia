package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.model.SelectItem;
import javax.inject.Named;

import org.apache.log4j.Level;

import br.gov.ba.seia.entity.Pais;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.PaisService;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Named
@ViewScoped
public class PaisController {
	
	@EJB
	private PaisService paisService;
	
	private List<Pais> listaPaises;
	
	
	public List<Pais> listar(){
		return paisService.listar();
	}
	
	
	public void loadListaPaises() {
		try {
			this.listaPaises = paisService.listar();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		} 
	}

	public Collection<SelectItem> getValuesComboBox(){
		loadListaPaises();
		
		Collection<SelectItem> toReturn = new ArrayList<SelectItem>();
	    for (Pais pais : listaPaises) {
	    	toReturn.add(new SelectItem(pais, pais.getNomPais()));  
		}
	    
        return toReturn;
    }
	
	public List<Pais> getListaPaises() {
		return listaPaises;
	}

	public void setListaPaises(List<Pais> listaPaises) {
		this.listaPaises = listaPaises;
	}
		
	
	
	

}
