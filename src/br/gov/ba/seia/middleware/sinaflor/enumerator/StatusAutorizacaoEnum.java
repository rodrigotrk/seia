package br.gov.ba.seia.middleware.sinaflor.enumerator;

public enum StatusAutorizacaoEnum {
	
	AUTORIZACAO_EMITIDA(309),
	AUTORIZACAO_SUSPENSA(1507),
	AUTORIZACAO_RENOVADA(1552),
	AUTORIZACAO_RETIFICADA(1553),
	AUTORIZACAO_CANCELADA(1554)
	;
	
	private Integer id;
	
	private StatusAutorizacaoEnum(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return id;
	}
	
}
