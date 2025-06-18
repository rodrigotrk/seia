package br.gov.ba.seia.middleware.sinaflor.model;

import java.math.BigDecimal;
/**
 * Classe modelo de volume de autorizaçao de saida
 * @author 
 *
 */
public class VolumeAutorizadoSaida {
	
	private TaxonomiaSaida especie;
	private Integer id;
	private NomePopularSaida nomePopular;
	private ProdutoFlorestalSaida produto;
	private BigDecimal volume;
	
	public VolumeAutorizadoSaida() {
		
	}

	public TaxonomiaSaida getEspecie() {
		return especie;
	}

	public void setEspecie(TaxonomiaSaida especie) {
		this.especie = especie;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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
