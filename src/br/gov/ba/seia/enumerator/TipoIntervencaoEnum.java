package br.gov.ba.seia.enumerator;

public enum TipoIntervencaoEnum {
	
	TRAVESSIA_DUTO(1),
	PONTE(2),
	CANALIZACAO_DESASSOREAMENTO_DRAGAGEM(3),
	CONSTRUCAO_DIQUE(4),
	CONSTRUCAO_CAIS(5),
	DRENAGEM_PLUVIAL(6),
	CONSTRUCAO_BARRAGEM(7),
	DESASSOREAMENTO(8),
	EXPLOTACAO(9),
	CAIS_PIER_DIQUE(10),
	TRAVESSIA(11),
	CANALIZACAO_RETIFICACAO(12),
	AQUICULTURA(13),
	OUTRAS_INTERVENCOES_Q_INTERFIRAM_QUANTIDADE_QUALIDADE_AGUAS(14)
	;

	private Integer id;

	private TipoIntervencaoEnum(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}
}
