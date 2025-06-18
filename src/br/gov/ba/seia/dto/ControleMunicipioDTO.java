package br.gov.ba.seia.dto;

import java.util.ArrayList;
import java.util.List;

import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.util.Util;

public class ControleMunicipioDTO {

	private Municipio municipio;
	
	private List<ControleMunicipioAtribuicaoDTO> listaAtribuicao;
	
	private List<ControleMunicipioAtribuicaoDTO> listaDetalhe;
	
	public ControleMunicipioDTO(Municipio municipio) {
		this.municipio = municipio;
		
		this.listaAtribuicao = new ArrayList<ControleMunicipioAtribuicaoDTO>();
		
		this.listaAtribuicao.add(new ControleMunicipioAtribuicaoDTO(Util.getString("controle_municipio_ind_bloqueio_DQC"), isBloqueioDQC()));
		this.listaAtribuicao.add(new ControleMunicipioAtribuicaoDTO(Util.getString("controle_municipio_ind_estado_emergencia"), isEstadoEmergencia()));
	}
	
	public Boolean isBloqueioDQC() {
		return !Util.isNullOuVazio(municipio.getIndBloqueioDQC()) && municipio.getIndBloqueioDQC();
	}
	
	public Boolean isEstadoEmergencia() {
		return !Util.isNullOuVazio(municipio.getIndEstadoEmergencia()) && municipio.getIndEstadoEmergencia();
	}
	
	public Municipio getMunicipio() {
		return municipio;
	}

	public void setMunicipio(Municipio municipio) {
		this.municipio = municipio;
	}

	public List<ControleMunicipioAtribuicaoDTO> getListaDetalhe() {
		return listaDetalhe;
	}

	public void setListaDetalhe(List<ControleMunicipioAtribuicaoDTO> listaDetalhe) {
		this.listaDetalhe = listaDetalhe;
	}

	public List<ControleMunicipioAtribuicaoDTO> getListaAtribuicao() {
		return listaAtribuicao;
	}

	public void setListaAtribuicao(
			List<ControleMunicipioAtribuicaoDTO> listaAtribuicao) {
		this.listaAtribuicao = listaAtribuicao;
	}
}
