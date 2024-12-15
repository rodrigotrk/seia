package br.gov.ba.seia.enumerator;

public enum TipoNotificacaoEnum {
	NOTIFICACAO_PRAZO(1),
	NOTIFICACAO_COMUNICACAO(2),
	NOTIFICACAO_HOMOLOGACAO(3);
	
	private int tipo;
	
	private TipoNotificacaoEnum(int i) {
		tipo = i;
	}
	
	public int getTipo() {
		return tipo;
	}
}