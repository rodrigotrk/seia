package br.gov.ba.seia.enumerator;

public enum TipoPeriodoDerivacaoEnum {

	CONTINUO(1),
	SAZONAL(2),
	INTERMITENTE(3);

	private Integer id;

	private TipoPeriodoDerivacaoEnum(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}
}
