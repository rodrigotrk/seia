package br.gov.ba.seia.enumerator;


public enum TipoMensagemEnum {

	SUCESSO("sucesso"),
	ALERTA("alerta"),
	ERRO("erro");
	
	private TipoMensagemEnum(String value) {
		this.value = value;
	}

	private String value;

	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

}
