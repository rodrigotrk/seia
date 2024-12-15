package br.gov.ba.seia.enumerator;

public enum MedidaControleEmissaoEnum {

	OUTROS(7);
	
	private Integer id;
	
	private MedidaControleEmissaoEnum(Integer id) {
		
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
