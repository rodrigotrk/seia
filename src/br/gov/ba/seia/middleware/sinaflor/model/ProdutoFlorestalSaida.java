package br.gov.ba.seia.middleware.sinaflor.model;
/**
 * Classe modelo produto florestal de saida
 * @author 
 *
 */
public class ProdutoFlorestalSaida {

	private Integer id;
	private String descricao;
	private Integer unidadeMedida;

	public ProdutoFlorestalSaida() {

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Integer getUnidadeMedida() {
		return unidadeMedida;
	}

	public void setUnidadeMedida(Integer unidadeMedida) {
		this.unidadeMedida = unidadeMedida;
	}

}
