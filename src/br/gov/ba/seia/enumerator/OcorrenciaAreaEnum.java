package br.gov.ba.seia.enumerator;

public enum OcorrenciaAreaEnum {

	ABANDONADA(1),
	INVIAVEL(2);

	private Integer id;

	private OcorrenciaAreaEnum(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}
}
