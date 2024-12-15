package br.gov.ba.seia.enumerator;

public enum TipoProjetoEnum {

	PDA_CERH(1),
	CONVENIO_PCT(2);

	private Integer id;

	private TipoProjetoEnum(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}
}
