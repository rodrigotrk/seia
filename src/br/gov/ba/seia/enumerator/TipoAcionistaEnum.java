package br.gov.ba.seia.enumerator;

public enum TipoAcionistaEnum {
	
	PESSOA_FISICA(1),
	PESSOA_JURIDICA(2);
	
	private Integer id;
	
	private TipoAcionistaEnum(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

}
