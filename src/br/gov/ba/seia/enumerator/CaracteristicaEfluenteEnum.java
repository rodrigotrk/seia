package br.gov.ba.seia.enumerator;

public enum CaracteristicaEfluenteEnum {

	DBO(1),
	COLIFORMES(2),
	FOSFORO(3);

	private Integer id;

	private CaracteristicaEfluenteEnum(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}
}
