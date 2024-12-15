package br.gov.ba.seia.middleware.sinaflor.model;

import java.math.BigDecimal;
/**
 * Classe modelo individuo de entrada
 * @author 
 *
 */
public class IndividuoEntrada {

	private Integer id;
	private BigDecimal latitude;
	private BigDecimal longitude;
	private Integer nomePopular;
	private Integer produto;
	private BigDecimal volume;

	public IndividuoEntrada() {

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public BigDecimal getLatitude() {
		return latitude;
	}

	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}

	public BigDecimal getLongitude() {
		return longitude;
	}

	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
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
