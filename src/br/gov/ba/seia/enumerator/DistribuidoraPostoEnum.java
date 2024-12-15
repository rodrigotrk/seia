package br.gov.ba.seia.enumerator;

public enum DistribuidoraPostoEnum {

	OUTROS(12);

	private Integer id;

	private DistribuidoraPostoEnum(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
