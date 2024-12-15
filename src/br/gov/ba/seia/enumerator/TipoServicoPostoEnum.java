package br.gov.ba.seia.enumerator;

public enum TipoServicoPostoEnum {
	
	OUTROS(11);

	private Integer id;

	private TipoServicoPostoEnum(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
