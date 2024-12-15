package br.gov.ba.seia.enumerator;

public enum TipoPautaEnum {
	PAUTA_AREA(1),
	PAUTA_COORDENADOR_AREA(2),
	PAUTA_TECNICA(3),
	PAUTA_DIRETOR_AREA(4);
	
	private int tipo;
	
	private TipoPautaEnum(int i) {
		tipo = i;
	}
	
	public int getTipo() {
		return tipo;
	}
}