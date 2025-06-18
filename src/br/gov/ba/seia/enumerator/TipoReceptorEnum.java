package br.gov.ba.seia.enumerator;

public enum TipoReceptorEnum {
	
	CONCESSIONARIA_PUBLICA(1),	
	MANANCIAL_CORPO_HIDRICO(2);	
	
private Integer id;
	
	private TipoReceptorEnum(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

}
