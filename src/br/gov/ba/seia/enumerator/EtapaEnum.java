package br.gov.ba.seia.enumerator;

public enum EtapaEnum {
	ETAPA_1(1), ETAPA_2(2), ETAPA_3(3), ETAPA_4(4), ETAPA_5(5), ETAPA_6(6);

	private Integer id;

	private EtapaEnum(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}
}
