package br.gov.ba.seia.enumerator;

public enum TipoCaracteristicaExtracaoEnum {
	
	LEITO_SECO(1), 
	DRAGAGEM(2), 
	OUTROS(3);

	private Integer id;

	private TipoCaracteristicaExtracaoEnum(Integer id) {

		this.id = id;
	}

	public Integer getId() {

		return id;
	}
}
