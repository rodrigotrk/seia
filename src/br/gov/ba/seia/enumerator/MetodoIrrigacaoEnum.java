package br.gov.ba.seia.enumerator;

public enum MetodoIrrigacaoEnum {
	ASPERSAO_COM_MANGUEIRA(3),
	ASPERSAO_COM_PIVO_LATERAL(5),
	ASPERSAO_POR_MALHA(7),
	INUNDACAO(9),
	SULCO(12),
	XIQUE_XIQUE(13);

	private Integer id;

	private MetodoIrrigacaoEnum(Integer pId) {
		this.id = pId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLabel() {
		return this.getDeclaringClass().getCanonicalName()+"."+this.name();
	}

}
