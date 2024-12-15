package br.gov.ba.seia.enumerator;

public enum TipoAlertaEnum {

	
	REQUERIMENTO(1),
	PROCESSO(2),
	PAUTA(3),
	NOTIFICACAO(4);
	
	private Integer id;
	
	private TipoAlertaEnum(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

}
