package br.gov.ba.seia.enumerator;

public enum TipoTravessiaDuto {
	AEREA(1), SUBTERRANEA(2), SUBMERSA(3);

	private Integer id;

	private TipoTravessiaDuto(Integer pId) {
		this.id = pId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLabel() {
		return this.getDeclaringClass().getCanonicalName() + "." + this.name();
	}
}
