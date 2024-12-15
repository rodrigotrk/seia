package br.gov.ba.seia.enumerator;

public enum NaturezaJuridicaEnum {
	
	SOCIEDADE_ANONIMA(1), 
	SOCIEDADE_DE_ECONOMIA_MISTA(2), 
	SOCIEDADE_EMPRESARIA_LIMITADA(3),
	COOPERATIVA(4);
	
	private Integer id;
	
	private NaturezaJuridicaEnum(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

}
