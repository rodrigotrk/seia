package br.gov.ba.seia.enumerator;

public enum PorteEnum {

	NAO_IDENTIFICADO(6),MEDIO(3),PEQUENO(2);
	
	private Integer id;
	
	private PorteEnum(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
}
