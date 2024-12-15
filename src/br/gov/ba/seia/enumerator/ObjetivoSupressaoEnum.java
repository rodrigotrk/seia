package br.gov.ba.seia.enumerator;

public enum ObjetivoSupressaoEnum {

	ATIVIDADE_AGROSSILVOPASTORIL(1),
	ATIVIDADE_DE_MINERAÇÃO(2),
	EMP_INDUSTRIAL(3),
	EMP_DE_INFRA_E_ENERGIA(4),
	EMP_DE_INTERESSE_SOCIAL(5),
	CONSTRUÇÃO_CIVIL(6),
	EMP_URBAN_TUR_E_LAZER(7),
	OUTRO(8),
	OLEO_E_GAS(12),
	MINERACAO(13);

	private Integer id;

	private ObjetivoSupressaoEnum(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}
}
