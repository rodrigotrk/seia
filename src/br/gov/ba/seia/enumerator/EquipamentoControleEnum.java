package br.gov.ba.seia.enumerator;

public enum EquipamentoControleEnum {

	OUTROS(8);
	
	private Integer id;
	
	private EquipamentoControleEnum(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
