package br.gov.ba.ws.enumerator;

public enum TipoDispositivoEnum {
	ANDROID(1),
	IOS(2);
	
	private Integer id;
	
	private TipoDispositivoEnum(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}
}
