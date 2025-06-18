package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;

import org.apache.log4j.Level;

import br.gov.ba.seia.dto.ControleMunicipioDTO;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.ControleMunicipioService;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Named("controleMunicipioController")
@ViewScoped
public class ControleMunicipioController {

	@EJB
	private ControleMunicipioService controleMunicipioService;
	
	private String nomeMunicipio;
	private ControleMunicipioDTO controleMunicipioDTOSelecionado;
	
	private List<ControleMunicipioDTO> listaControleMunicipioDTO;
	private List<ControleMunicipioDTO> listaControleMunicipioDTOAll;
	
	@PostConstruct
	public void init() {
		try {
			listaControleMunicipioDTO = controleMunicipioService.listarMunicipiosDaBahia();
			listaControleMunicipioDTOAll = new ArrayList<ControleMunicipioDTO>(listaControleMunicipioDTO);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void salvarControleMunicipio() {
		try {
			controleMunicipioService.salvarControleMunicipio(controleMunicipioDTOSelecionado);
			
			JsfUtil.addSuccessMessage("Controle de municípios " + Util.getString("messagem_016"));
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e, "Erro ao editar o município." + e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public void pesquisarMunicipio(){
		listaControleMunicipioDTO = controleMunicipioService.pesquisarMunicipio(listaControleMunicipioDTO, listaControleMunicipioDTOAll, nomeMunicipio);
	}

	public List<ControleMunicipioDTO> getListaControleMunicipioDTO() {
		return listaControleMunicipioDTO;
	}

	public void setListaControleMunicipioDTO(
			List<ControleMunicipioDTO> listaControleMunicipioDTO) {
		this.listaControleMunicipioDTO = listaControleMunicipioDTO;
	}

	public String getNomeMunicipio() {
		return nomeMunicipio;
	}

	public void setNomeMunicipio(String nomeMunicipio) {
		this.nomeMunicipio = nomeMunicipio;
	}

	public ControleMunicipioDTO getControleMunicipioDTOSelecionado() {
		return controleMunicipioDTOSelecionado;
	}

	public void setControleMunicipioDTOSelecionado(
			ControleMunicipioDTO controleMunicipioDTOSelecionado) {
		this.controleMunicipioDTOSelecionado = controleMunicipioDTOSelecionado;
	}
}
