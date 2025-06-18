package br.gov.ba.seia.util.json.enums;

public enum TipoVinculoEnum {

	PROPRIEDADE("PROPRIEDADE"),
	POSSE("POSSE"),
	CONCESSAO("CONCESSAO"),
	OUTROS("OUTROS");
	
	private String tipo;
	
	TipoVinculoEnum(String string) {
	}
	
	public String getTipo() {
		return tipo;
	}
	
}
