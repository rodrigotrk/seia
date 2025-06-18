package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.inject.Named;

import org.apache.log4j.Level;

import br.gov.ba.seia.entity.Estado;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.entity.TipoLogradouro;
import br.gov.ba.seia.facade.EnderecoFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.interfaces.EstadoMunicipioInterface;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Named("estadoMunicipioController")
@ViewScoped
public class EstadoMunicipioController implements EstadoMunicipioInterface {

	@EJB
	private EnderecoFacade enderecoFacade;

	protected Municipio municipio;
	protected List<Municipio> listaMunicipio = new ArrayList<Municipio>();
	
	protected Estado estado;
	protected List<Estado> listaEstados = new ArrayList<Estado>();
	
	private List<TipoLogradouro> listaTipoLogradouros;

	public List<SelectItem> getValuesComboBoxEstado(){
		loadListaEstados();
		List<SelectItem> toReturn = new ArrayList<SelectItem>();
		Iterator<Estado> i = listaEstados.iterator();
		while (i.hasNext()) {
			Estado estadoObj = i.next();
			toReturn.add(new SelectItem(estadoObj, estadoObj.getDesSigla()));  
		}
		return toReturn;
	}

	private void loadListaEstados() {
		try {
			if(Util.isNullOuVazio(listaEstados)){
				listaEstados = enderecoFacade.listarEstados();
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public void changeEstado(ValueChangeEvent event){
		try {
			Estado estadoObj = (Estado) event.getNewValue();
			if(!estadoObj.getIdeEstado().equals(0) && !estadoObj.equals(getEstado())){
				municipio = new Municipio();
				listaMunicipio = (List<Municipio>) enderecoFacade.filtrarMunicipiosByEstado(estadoObj);
			}
		} 
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public List<SelectItem> getValuesComboBoxMunicipio(){
		if(!Util.isNullOuVazio(municipio) && !listaMunicipio.contains(municipio)){
			listaMunicipio.add(municipio);
		}
		List<SelectItem> toReturn = new ArrayList<SelectItem>();
		Iterator<Municipio> i = listaMunicipio.iterator();
		while (i.hasNext()) {
			Municipio municipioObj = i.next();
			toReturn.add(new SelectItem(municipioObj, municipioObj.getNomMunicipio()));  
		}
		return toReturn;
	}

	public void listarMunicipios(Estado estado) {
		if(!Util.isNull(estado) 
				&& !Util.isNull(estado.getIdeEstado()) 
				&& !estado.getIdeEstado().equals(0)) {
			try {
				listaMunicipio = (List<Municipio>) enderecoFacade.filtrarMunicipiosByEstado(estado);
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
	}

	public Collection<SelectItem> getValuesComboBox(){
		loadListaLogradouros();
		Collection<SelectItem> toReturn = new ArrayList<SelectItem>();
		Iterator<TipoLogradouro> i = listaTipoLogradouros.iterator();
		toReturn.add(new SelectItem(null, "Selecione..."));
		while (i.hasNext()) {
			TipoLogradouro tipoLogradouro = i.next();
			toReturn.add(new SelectItem(tipoLogradouro, tipoLogradouro.getNomTipoLogradouro()));  
		}
		return toReturn;
	}

	public void loadListaLogradouros() {
		try {
			listaTipoLogradouros = enderecoFacade.listarTipoLogradouro();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	@Override
	public Estado getEstado() {
		return this.estado;
	}

	@Override
	public void setEstado(Estado estado) {
		this.estado = estado;
	}
	
	@Override
	public Municipio getMunicipio() {
		return municipio;
	}
	
	@Override
	public void setMunicipio(Municipio municipio) {
		this.municipio = municipio;
	}
	
}
