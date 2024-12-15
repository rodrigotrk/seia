package br.gov.ba.seia.enumerator;

public enum LacTransporteOutroEnum {
	
	PRODUTO_OUTROS(9999),
	RESIDUO_OUTROS(9999);
	
	private Integer id;

	private LacTransporteOutroEnum(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}
}
