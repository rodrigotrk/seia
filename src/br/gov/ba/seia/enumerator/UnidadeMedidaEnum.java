package br.gov.ba.seia.enumerator;

public enum UnidadeMedidaEnum {
	M3(70, "m³"),
	MDC(87, "MDC"),
	ST(88,"ST")
	;

	private Integer id;
	private String descricao;

	private UnidadeMedidaEnum(Integer pId, String descricao) {
		this.id = pId;
		this.descricao = descricao;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescricao() {
		return this.descricao;
	}
}
