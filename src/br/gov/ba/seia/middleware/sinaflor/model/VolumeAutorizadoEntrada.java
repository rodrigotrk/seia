package br.gov.ba.seia.middleware.sinaflor.model;

import java.math.BigDecimal;
/**
 * Classe modelo volume autorizado de entrada
 * @author 
 *
 */
public class VolumeAutorizadoEntrada {

	private Integer especie;
	private Integer nomePopular;
	private Integer produto;
	private BigDecimal volume;

	public VolumeAutorizadoEntrada() {

	}

	public Integer getEspecie() {
		return especie;
	}

	public void setEspecie(Integer especie) {
		this.especie = especie;
	}

	public Integer getNomePopular() {
		return nomePopular;
	}

	public void setNomePopular(Integer nomePopular) {
		this.nomePopular = nomePopular;
	}

	public Integer getProduto() {
		return produto;
	}

	public void setProduto(Integer produto) {
		this.produto = produto;
	}

	public BigDecimal getVolume() {
		return volume;
	}

	public void setVolume(BigDecimal volume) {
		this.volume = volume;
	}

}
