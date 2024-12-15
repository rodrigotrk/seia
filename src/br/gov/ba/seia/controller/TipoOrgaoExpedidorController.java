package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.model.SelectItem;
import javax.inject.Named;

import org.apache.log4j.Level;

import br.gov.ba.seia.entity.OrgaoExpedidor;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.TipoOrgaoExpedidorService;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

/**
*
* @author rubem.filho
*/


@Named
@ViewScoped
public class TipoOrgaoExpedidorController {
	
	private List<OrgaoExpedidor> listaOrgaoExpedidores;

	@EJB
	private TipoOrgaoExpedidorService service;
	
	public void loadListaOrgaoExpedidor() {
		try {
			listaOrgaoExpedidores = (List<OrgaoExpedidor>) service.listarOrgaoExpedidor();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public Collection<SelectItem> getValuesComboBox(){
		loadListaOrgaoExpedidor();
		 Collection<SelectItem> toReturn = new ArrayList<SelectItem>();
	        Iterator<OrgaoExpedidor> i = listaOrgaoExpedidores.iterator();
	        toReturn.add(new SelectItem(0, "---"));
	        while (i.hasNext()) {
	        	OrgaoExpedidor orgaoExpedidor = i.next();
	        	toReturn.add(new SelectItem(orgaoExpedidor.getIdeOrgaoExpedidor(), orgaoExpedidor.getDscOrgaoExpedidor()));  
	        }
	        return toReturn;
     }
	
	
	public List<OrgaoExpedidor> getListaOrgaoExpedidor() {
		return listaOrgaoExpedidores;
	}

	public void setListaOcupacao(List<OrgaoExpedidor> listaOrgaoExpedidor) {
		this.listaOrgaoExpedidores = listaOrgaoExpedidor;
	}

}
