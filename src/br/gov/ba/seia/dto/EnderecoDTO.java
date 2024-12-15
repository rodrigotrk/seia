package br.gov.ba.seia.dto;

import br.gov.ba.seia.entity.Bairro;
import br.gov.ba.seia.entity.Estado;
import br.gov.ba.seia.entity.Logradouro;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.entity.TipoLogradouro;

public class EnderecoDTO {

	private String id;
	private String cep;
	private String logradouro;
	private String bairro;
	private String municipio;
	private String uf;
    private Integer ideBairro;
    private Integer ideEstado =5;
    private Integer ideMunicipio;
	private Integer ideLogradouro;
	private Integer ideTipoLogradouro;
	private String nomOrigem;



	public EnderecoDTO() {
	}

	public EnderecoDTO(String id, String cep, String logradouro, String bairro, String municipio, String uf) {
		super();
		this.id = id;
		this.cep = cep;
		this.logradouro = logradouro;
		this.bairro = bairro;
		this.municipio = municipio;
		this.uf = uf;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getMunicipio() {
		return municipio;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	
	public Integer getIdeBairro() {
		return ideBairro;
	}

	public void setIdeBairro(Integer ideBairro) {
		this.ideBairro = ideBairro;
	}

	public Integer getIdeMunicipio() {
		return ideMunicipio;
	}

	public void setIdeMunicipio(Integer ideMunicipio) {
		this.ideMunicipio = ideMunicipio;
	}


	public Integer getIdeLogradouro() {
		return ideLogradouro;
	}

	public void setIdeLogradouro(Integer ideLogradouro) {
		this.ideLogradouro = ideLogradouro;
	}

	@Override
	public String toString() {
		return "EnderecoDTO [id=" + id + ", cep=" + cep + ", logradouro=" + logradouro + ", bairro=" + bairro
				+ ", municipio=" + municipio + ", uf=" + uf + ", ideBairro=" + ideBairro + ", ideMunicipio="
				+ ideMunicipio + ", ideLogradouro=" + ideLogradouro + "]";
	}

	public Integer getIdeTipoLogradouro() {
		return ideTipoLogradouro;
	}

	public void setIdeTipoLogradouro(Integer ideTipoLogradouro) {
		this.ideTipoLogradouro = ideTipoLogradouro;
	}

	public Integer getIdeEstado() {
		return ideEstado;
	}

	public void setIdeEstado(Integer ideEstado) {
		this.ideEstado = ideEstado;
	}

	public String getNomOrigem() {
		return nomOrigem;
	}

	public void setNomOrigem(String nomOrigem) {
		this.nomOrigem = nomOrigem;
	}

	

}
