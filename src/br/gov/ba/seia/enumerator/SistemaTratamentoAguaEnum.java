package br.gov.ba.seia.enumerator;

public enum SistemaTratamentoAguaEnum {

	OUTROS(8);
	
	private Integer id;
	
	private SistemaTratamentoAguaEnum(Integer id){
		
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
