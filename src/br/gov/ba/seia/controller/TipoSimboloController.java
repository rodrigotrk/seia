package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.model.SelectItem;
import javax.inject.Named;

import org.apache.log4j.Level;

import br.gov.ba.seia.entity.Simbolo;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.TipoSimboloService;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

/**
*
* @author rubem.filho
*/


@Named("tipoSimboloController")
@ViewScoped
public class TipoSimboloController {
	
	private List<Simbolo> listaSimbolos;

	@EJB
	private TipoSimboloService service;
	
	public void loadListaSimbolo() {
		try {
			listaSimbolos = (List<Simbolo>) service.listar();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public Collection<SelectItem> getValuesComboBox(){
		loadListaSimbolo();
		 Collection<SelectItem> toReturn = new ArrayList<SelectItem>();
	        Iterator<Simbolo> i = listaSimbolos.iterator();
	        toReturn.add(new SelectItem(0, "---"));
	        
	        while (i.hasNext()) {
	        	Simbolo simbolo = (Simbolo)i.next();
	        	toReturn.add(new SelectItem(simbolo.getIdeSimbolo(), simbolo.getCodSimbolo()));  
	        }
	        return toReturn;
     }
	
	
	public List<Simbolo> getListaSimbolo() {
		return listaSimbolos;
	}

	public void setListaSimbolo(List<Simbolo> listaSimbolo) {
		this.listaSimbolos = listaSimbolo;
	}
	
	
	

}
