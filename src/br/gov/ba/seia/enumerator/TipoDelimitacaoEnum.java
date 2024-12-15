package br.gov.ba.seia.enumerator;

public enum TipoDelimitacaoEnum {
	CERCA(1),
	MURO(2),
	OUTROS(3);
	
	private Integer ide;
	
	private TipoDelimitacaoEnum(Integer i) {
		ide = i;
	}
	
	public Integer getIde() {
		return ide;
	}
}