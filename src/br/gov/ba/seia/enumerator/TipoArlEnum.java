package br.gov.ba.seia.enumerator;

public enum TipoArlEnum {

	ECOND(1, "Em condomínio"), 
	NPI  (2, "No próprio imóvel"), 
	ECIP (3, "Em compensação entre imóveis de mesmo proprietário"), 
	ECSF (4, "Em compensação por Servidão Ambiental"),
	CDAUC (5, "Em compensação por doação de área em Unidade de Conservação");

	private Integer id;
	private String descricao;

	TipoArlEnum(Integer id, String descricao) {
		this.id = id;
		this.descricao = descricao;
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

}
