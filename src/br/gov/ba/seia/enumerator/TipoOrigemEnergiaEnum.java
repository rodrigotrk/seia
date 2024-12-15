package br.gov.ba.seia.enumerator;

public enum TipoOrigemEnergiaEnum {
	GERADOR(3),
	OUTROS(7);

	private Integer id;

	private TipoOrigemEnergiaEnum(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
