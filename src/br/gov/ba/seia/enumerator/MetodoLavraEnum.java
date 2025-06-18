package br.gov.ba.seia.enumerator;

public enum MetodoLavraEnum {

	CEU_ABERTO(1),
	SUBTERRANEA(2); 

	private Integer ideMetodoLavra;

	MetodoLavraEnum(Integer ide){
		this.ideMetodoLavra = ide;
	}

	public Integer getId() {
		return ideMetodoLavra;
	}

	public void setId(Integer ide) {
		this.ideMetodoLavra = ide;
	}
}
