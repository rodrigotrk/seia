package br.gov.ba.seia.enumerator;

public enum TipoModalidadeEnum {
	TORRE(1), EDIFICACAO(2), INDOOR(3);

	private Integer ide;

	private TipoModalidadeEnum(Integer i) {
		ide = i;
	}

	public Integer getIde() {
		return ide;
	}
}