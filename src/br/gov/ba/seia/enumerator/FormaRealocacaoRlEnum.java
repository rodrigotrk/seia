package br.gov.ba.seia.enumerator;

public enum FormaRealocacaoRlEnum {

	ECOND(1, "Realocação em área de condomínio"), 
	NPI  (2, "Realocada para o próprio imóvel"), 
	ECIP (3, "Realocação para outro imóvel do mesmo proprietário."), 
	ECSF (4, "Realocação por servidão ambiental"),
	CDAUC (5, "Em compensação por doação de área em Unidade de Conservação");

	private Integer id;
	private String descricao;

	FormaRealocacaoRlEnum(Integer id, String descricao) {
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
