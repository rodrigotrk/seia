package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;

import br.gov.ba.seia.faces.ViewScoped;

import javax.faces.model.SelectItem;

import br.gov.ba.seia.entity.Telefone;
import br.gov.ba.seia.entity.TipoTelefone;
import br.gov.ba.seia.service.TelefoneServiceTeste;
import br.gov.ba.seia.service.TipoTelefoneServiceTeste;
import br.gov.ba.seia.util.JsfUtil;

@Named
@ViewScoped
public class TelefoneControllerTeste {

	private List<Telefone> telefoneList;
	
	private List<SelectItem> tiposTelefone;
	
	private Telefone filtro;
	
	private boolean flgEdit;
	
	@EJB
	private TelefoneServiceTeste telefoneService;
	
	@EJB
	private TipoTelefoneServiceTeste tipoTelefoneService;
	
	@PostConstruct
	public void init() {
		flgEdit = false;
		filtro = new Telefone();
		filtro.setIdeTipoTelefone(new TipoTelefone());
		
		try {
			final List<TipoTelefone> tipos = tipoTelefoneService.searchAll();
			tiposTelefone = new ArrayList<SelectItem>();
			for (final TipoTelefone tipo : tipos) {
				tiposTelefone.add(new SelectItem(tipo.getIdeTipoTelefone(), tipo.getNomTipoTelefone()));
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
	}
	
	public String pesquisar() {

		try {
			flgEdit = false;
			telefoneList = telefoneService.search(filtro);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
		
		return "";
	}
	
	public String salvar() {
		
		try {
			if (flgEdit) {
				telefoneService.edit(filtro);
				
				filtro = new Telefone();
				filtro.setIdeTipoTelefone(new TipoTelefone());
			} else {
				filtro.setDtcCriacao(new Date());
				telefoneService.save(filtro);
				
				filtro = new Telefone();
				filtro.setIdeTipoTelefone(new TipoTelefone());
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
		
		return pesquisar();
	}
	
	public String editar() {
		flgEdit = true;
		return "";
	}
	
	public String excluir() {
		
		try {
			telefoneService.delete(filtro);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
		
		return pesquisar();
	}

	public List<Telefone> getTelefoneList() {
		return telefoneList;
	}

	public Telefone getFiltro() {
		return filtro;
	}

	public List<SelectItem> getTiposTelefone() {
		return tiposTelefone;
	}

}
