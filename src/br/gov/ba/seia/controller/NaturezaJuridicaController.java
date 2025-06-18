package br.gov.ba.seia.controller;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.model.SelectItem;
import javax.inject.Named;

import org.apache.log4j.Level;

import br.gov.ba.seia.entity.NaturezaJuridica;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.NaturezaJuridicaService;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Named("naturezaJuridicaController")
@ViewScoped
public class NaturezaJuridicaController {
	
	@EJB
	private NaturezaJuridicaService naturezaJuridicaService;
	private List<NaturezaJuridica> listaNaturezaJuridica;
	private List<SelectItem> naturezaJuridicaItens;
	
	@PostConstruct
	public void init(){
		loadListaNaturezaJuridica();
	}
		
	public void loadListaNaturezaJuridica(){
		try {
			listaNaturezaJuridica = (List<NaturezaJuridica>) naturezaJuridicaService.listarTodos();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public Collection<SelectItem> getValuesCombo(){
		Collection<SelectItem> collCombo = new ArrayList<SelectItem>();
		collCombo.add(new SelectItem(new Integer(0) , "Selecione"));
		for(NaturezaJuridica natJur : listaNaturezaJuridica){
			collCombo.add(new SelectItem(natJur.getIdeNaturezaJuridica(), natJur.getNomNaturezaJuridica()));
		}		
		return collCombo;
	}

	public List<NaturezaJuridica> getListaNaturezaJuridica() {
		return listaNaturezaJuridica;
	}

	public void setListaNaturezaJuridica(
			List<NaturezaJuridica> listaNaturezaJuridica) {
		this.listaNaturezaJuridica = listaNaturezaJuridica;
	}


	public List<SelectItem> getNaturezaJuridicaItens() {
		return naturezaJuridicaItens;
	}


	public void setNaturezaJuridicaItens(List<SelectItem> naturezaJuridicaItens) {
		this.naturezaJuridicaItens = naturezaJuridicaItens;
	}	
	
}
