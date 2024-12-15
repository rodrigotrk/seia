package br.gov.ba.seia.enumerator;

public enum TipoDadoSilviculturaEnum {
	DADOS_FLORESTA_PRODUCAO(1), 
	ESPECIES_VARIED_PROPOSTAS_REFLOREST(2), 
	ESPECIES_HIBRIDOS(3),
	INCREMENTO_MEDIO_ANUAL(4),
	PRATICAS_EXIS_PLANEJ_MANEJO_CONSERV_SOLO(5);

	private Integer id;

	private TipoDadoSilviculturaEnum(Integer Id) {
		this.id = Id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
