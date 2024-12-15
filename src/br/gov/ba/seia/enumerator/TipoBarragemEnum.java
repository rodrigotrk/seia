package br.gov.ba.seia.enumerator;

public enum TipoBarragemEnum {
	NIVEL(1), REGULARIZACAO(2);
	
	private Integer id;

	private TipoBarragemEnum(Integer pId) {
		this.id = pId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getLabel() {
		return this.getDeclaringClass().getCanonicalName()+"."+this.name();
	}
	
}
