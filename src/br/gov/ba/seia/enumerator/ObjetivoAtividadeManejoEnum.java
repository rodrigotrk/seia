package br.gov.ba.seia.enumerator;

public enum ObjetivoAtividadeManejoEnum {

	ESTUDO(1),
	PESQUISA_ACADEMICA(2),
	OBRAS_DO_EMPREENDIMENTO(3),
	;

	private Integer id;

	private ObjetivoAtividadeManejoEnum(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}


}
