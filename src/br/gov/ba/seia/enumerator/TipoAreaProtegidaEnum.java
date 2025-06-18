package br.gov.ba.seia.enumerator;

public enum TipoAreaProtegidaEnum {

	RESERVA_LEGAL(1), AREA_DE_PRESERVACAO_PERMANENTE(2), UNIDADE_DE_CONSERVACAO(3), TERRENOS_DA_UNIAO(4), TERRAS_INDIGENAS(
			5), TERRAS_QUILOMBOLAS(6);

	private Integer id;

	private TipoAreaProtegidaEnum(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

}
