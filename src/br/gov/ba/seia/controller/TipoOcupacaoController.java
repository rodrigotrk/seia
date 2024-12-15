package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.model.SelectItem;
import javax.inject.Named;

import org.apache.log4j.Level;

import br.gov.ba.seia.entity.Ocupacao;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.TipoOcupacaoService;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;


/**
*
* @author rubem.filho
*/


@Named
@ViewScoped
public class TipoOcupacaoController {
	
	private List<Ocupacao> listaOcupacaos;

	@EJB
	private TipoOcupacaoService service;
	
	
	@PostConstruct
	public void init(){
		loadListaOcupados();
	}
	
	
	public void loadListaOcupados() {
		try {
			listaOcupacaos = (List<Ocupacao>) service.listarOcupacao();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public Collection<SelectItem> getValuesComboBox(){
		loadListaOcupados();
		 Collection<SelectItem> toReturn = new ArrayList<SelectItem>();
	        Iterator<Ocupacao> i = listaOcupacaos.iterator();
	        toReturn.add(new SelectItem(0, "---"));
	        while (i.hasNext()) {
	        	Ocupacao ocupacao = i.next();
	        	toReturn.add(new SelectItem(ocupacao.getIdeOcupacao(), ocupacao.getNomOcupacao()));  
	        }
	        return toReturn;
     }
	
	
	public List<Ocupacao> getListaOcupacao() {
		return listaOcupacaos;
	}

	public void setListaOcupacao(List<Ocupacao> listacupacao) {
		this.listaOcupacaos = listacupacao;
	}

}
