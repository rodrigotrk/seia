package br.gov.ba.seia.enumerator;

public enum SilosArmazensOrigemAguaEnum {
	
	POCO_DE_CAPTACAO(1),
	RECURSO_HIDRICO_SUPERFICIAL(2),
	CONCESSIONARIA(3);
	
	
	private Integer id;

	private SilosArmazensOrigemAguaEnum(Integer id) {
		
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
