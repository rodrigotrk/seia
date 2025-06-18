package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.model.SelectItem;
import javax.inject.Named;

import org.apache.log4j.Level;

import br.gov.ba.seia.entity.TipoIdentificacao;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.TipoIdentificacaoService;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Named
@ViewScoped
public class TipoIdentificacaoController {
	
	private List<TipoIdentificacao> listaTiposIdentificacao;
	
	@EJB
	private TipoIdentificacaoService service;
	
	public void loadListaTiposIdentificacao() {
		try {
			listaTiposIdentificacao = (List<TipoIdentificacao>) service.listar();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public Collection<SelectItem> getValuesComboBox(){
		loadListaTiposIdentificacao();
		 Collection<SelectItem> toReturn = new ArrayList<SelectItem>();
	        Iterator<TipoIdentificacao> i = listaTiposIdentificacao.iterator();
	        while (i.hasNext()) {
	        	TipoIdentificacao tipoIdentificacao = i.next();
	        	toReturn.add(new SelectItem(tipoIdentificacao, tipoIdentificacao.getNomTipoIdentificacao()));  
	        }
	        return toReturn;
     }
	
	public List<TipoIdentificacao> getListaTiposIdentificacao() {
		return listaTiposIdentificacao;
	}
	
	public void setListaTiposIdentificacao(List<TipoIdentificacao> listaTiposIdentificacao) {
		this.listaTiposIdentificacao = listaTiposIdentificacao;	
	}

}
