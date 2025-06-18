package br.gov.ba.seia.enumerator;

public enum TipoGestaoEnum {

	FEDERAL(1), ESTADUAL(2), MUNICIPAL(3);

	private Integer id;

	private TipoGestaoEnum(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

}
