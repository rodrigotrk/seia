package br.gov.ba.seia.enumerator;

public enum TipoRevisaoCondicionanteEnum {

	ALTERACAO(1), PRORROGACAO(2);

	private Integer id;

	private TipoRevisaoCondicionanteEnum(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

}
