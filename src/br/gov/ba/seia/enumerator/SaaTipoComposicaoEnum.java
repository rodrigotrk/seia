package br.gov.ba.seia.enumerator;

public enum SaaTipoComposicaoEnum {
	
	CALHA_PARSHALL(1, "Calha Parshall"),
	FLOCULADOR(2, "Floculador"),
	DECANTADOR(3, "Decantador"),
	FILTRO(4, "Filtro"),
	UNIDADE_DESINFECCAO(5, "Unidade de desinfecção"),
	UNIDADE_REAPROVEITAMENTO_AGUA_LAVAGEM(6, "Unidade de reaproveitamento de água de lavagem"),
	SISTEMA_SIMPLIFICIADO_SIMPLES_DESINFECCAO(7, "Sistema simplificado com simples desinfecção"),
	OUTROS(8, "Outros");

	private Integer id;
	private String descricao;

	private SaaTipoComposicaoEnum(Integer id, String descricao) {
		this.id = id;
		this.descricao = descricao;
	}

	public Integer getId() {
		return id;
	}
	
	public String getDescricao(){
		return descricao;
	}

}
