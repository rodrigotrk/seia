package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.model.SelectItem;
import javax.inject.Named;

import org.apache.log4j.Level;

import br.gov.ba.seia.entity.TipoTelefone;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.TipoTelefoneService;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Named("tipoTelefoneController")
@ViewScoped
public class TipoTelefoneController {
	
	private List<TipoTelefone> listaTiposTelefone;	
	
	@EJB
	private TipoTelefoneService service;
	
	public void loadListaTiposTelefone() {
		try {
			listaTiposTelefone = (List<TipoTelefone>) service.listarTipoTelefone();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public Collection<SelectItem> getValuesComboBox(){
		loadListaTiposTelefone();
		 Collection<SelectItem> toReturn = new ArrayList<SelectItem>();
	        Iterator<TipoTelefone> i = listaTiposTelefone.iterator();
	        while (i.hasNext()) {
	        	TipoTelefone tipoTelefone = i.next();
	        	toReturn.add(new SelectItem(tipoTelefone, tipoTelefone.getNomTipoTelefone()));  
	        }
	        return toReturn;
     }
		
	
	public List<TipoTelefone> getListaTiposTelefone() {
		return listaTiposTelefone;
	}

	public void setListaTiposTelefone(List<TipoTelefone> listaTiposTelefone) {
		this.listaTiposTelefone = listaTiposTelefone;
	}	

}
