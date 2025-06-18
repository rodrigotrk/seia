package br.gov.ba.seia.enumerator;

public enum TipoConservacaoSoloAguaEnum {
	CURVAS_NIVEL(1), BACIA_CONCATENACAO_AGUAS_PLUVIAIS(2), CULTIVO_MINIMO(3), OUTROS(4);

	private Integer id;

	private TipoConservacaoSoloAguaEnum(Integer pId) {
		this.id = pId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
