package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.model.SelectItem;
import javax.inject.Named;

import org.apache.log4j.Level;

import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.MunicipioService;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Named
@ViewScoped
public class MunicipioController {
	
	private List<Municipio> listaMunicipios;

	@EJB
	private MunicipioService service;
	
	public void loadListaMunicipios() {
		try {
			listaMunicipios = (List<Municipio>) service.listarMunicipio();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public Collection<SelectItem> getValuesComboBox(){
		loadListaMunicipios();
		 Collection<SelectItem> toReturn = new ArrayList<SelectItem>();
	        Iterator<Municipio> i = listaMunicipios.iterator();
	        while (i.hasNext()) {
	        	Municipio municipio = i.next();
	        	toReturn.add(new SelectItem(municipio.getIdeMunicipio(), municipio.getNomMunicipio()));  
	        }
	        return toReturn;
     }
	
	
	public List<Municipio> getListaMunicipios() {
		return listaMunicipios;
	}

	public void setListaMunicipios(List<Municipio> listaMunicipios) {
		this.listaMunicipios = listaMunicipios;
	}
}
