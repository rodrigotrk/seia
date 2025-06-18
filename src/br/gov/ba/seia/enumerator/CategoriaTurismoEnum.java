package br.gov.ba.seia.enumerator;

public enum CategoriaTurismoEnum {
	COMPLEXO_TURISTICO(6),
	EMPREENDIMENTO_HOTELEIRO(7),
	LOTEAMENTOS(8),
	DESMEMBRAMENTOS(9),
	CONJUNTOS_HABITACIONAIS(10),
	HABITACAO_INTERESSE_SOCIAL(11);

	private Integer id;

	private CategoriaTurismoEnum(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}