package br.gov.ba.seia.enumerator;

public enum TipoImovelEnum {

	RURAL(1,"Rural"),
	URBANO(2,"Urbano"),
	CESSIONARIO(3,"Cessão");


	private String desc;
	private Integer id;

	private TipoImovelEnum(Integer id, String desc) {
		this.id = id;
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

	public Integer getId() {
		return id;
	}


}
