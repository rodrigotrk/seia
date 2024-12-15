package br.gov.ba.seia.enumerator;

public enum StatusNotificacaoEnum {
	
	EMITIDA(1),
	REJEITADA(2),
	CANCELADA(3),
	AGUARDANDO_APROVACAO(4);
	
    private int status;
    
	private StatusNotificacaoEnum(int i) {
		status = i;
	}
	
	public int getStatus() {
		return status;
	}

}
