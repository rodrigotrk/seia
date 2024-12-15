package br.gov.ba.seia.enumerator;

public enum TipoTelefoneEnum {
	RESIDENCIAL(1),
	COMERCIAL(2),
	CELULAR(3),
	FAX(4);

	private Integer id;

	private TipoTelefoneEnum(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
