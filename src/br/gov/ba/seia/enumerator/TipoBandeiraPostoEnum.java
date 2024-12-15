package br.gov.ba.seia.enumerator;

public enum TipoBandeiraPostoEnum {
	
	DISTRIBUIDORA(2);

	private Integer id;

	private TipoBandeiraPostoEnum(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
