package br.gov.ba.seia.middleware.sinaflor.model;

import java.math.BigDecimal;
/**
 * Classe modelo individuo de saida
 * @author 
 *
 */
public class IndividuoSaida {

	private Integer id;
	private BigDecimal latitude;
	private BigDecimal longitude;
	private TaxonomiaSaida especie;
	private NomePopularSaida nomePopular;
	private ProdutoFlorestalSaida produto;
	private BigDecimal volume;

	public IndividuoSaida() {

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

	public TaxonomiaSaida getEspecie() {
		return especie;
	}

	public void setEspecie(TaxonomiaSaida especie) {
		this.especie = especie;
	}

	public NomePopularSaida getNomePopular() {
		return nomePopular;
	}

	public void setNomePopular(NomePopularSaida nomePopular) {
		this.nomePopular = nomePopular;
	}

	public ProdutoFlorestalSaida getProduto() {
		return produto;
	}

	public void setProduto(ProdutoFlorestalSaida produto) {
		this.produto = produto;
	}

	public BigDecimal getVolume() {
		return volume;
	}

	public void setVolume(BigDecimal volume) {
		this.volume = volume;
	}

}
