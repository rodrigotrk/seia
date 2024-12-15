package br.gov.ba.seia.enumerator;

public enum DestinacaoFinalEnum {

	OUTROS(9);
	
	private Integer id;
	
	private DestinacaoFinalEnum(Integer id) {
		
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
}
