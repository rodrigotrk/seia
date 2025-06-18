package br.gov.ba.seia.enumerator;

public enum TipoAreaEnum {
	DIRETORIA(1),
	COORDENACAO(2),
	OUTROS(3);
	
	private int tipo;
	
	private TipoAreaEnum(int i) {
		tipo = i;
	}
	
	public int getTipo() {
		return tipo;
	}
}