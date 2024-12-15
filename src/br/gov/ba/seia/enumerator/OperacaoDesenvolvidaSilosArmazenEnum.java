package br.gov.ba.seia.enumerator;


public enum OperacaoDesenvolvidaSilosArmazenEnum {

	BENEFICIAMENTO(5),
	OUTROS(10),
	OUTROS_BENEFICIAMENTO(14);
	
	private Integer id;
	
	OperacaoDesenvolvidaSilosArmazenEnum(Integer id){
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
