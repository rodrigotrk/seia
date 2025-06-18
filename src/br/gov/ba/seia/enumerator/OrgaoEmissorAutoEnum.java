package br.gov.ba.seia.enumerator;

public enum OrgaoEmissorAutoEnum {
	
	INEMA(1),
	IBAMA(2),
	SECRETARIA_MUNICIPAL(3);
	
	private Integer id;
	
	private OrgaoEmissorAutoEnum(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
