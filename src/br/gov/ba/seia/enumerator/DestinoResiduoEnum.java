package br.gov.ba.seia.enumerator;

public enum DestinoResiduoEnum {

	PILHA_ESTERIL(6),
	OUTROS(5)
	;
	
	private int id;
	
	private DestinoResiduoEnum(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
	
	
}
