package br.gov.ba.seia.enumerator;

public enum TipoCombustivelSiloArmazenEnum {
	
	MADEIRA(4),
	OUTROS(5),
	MADEIRA_NATIVA(6),
	MADEIRA_EXOTICA(7);
	
	private Integer id;

	private TipoCombustivelSiloArmazenEnum(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
