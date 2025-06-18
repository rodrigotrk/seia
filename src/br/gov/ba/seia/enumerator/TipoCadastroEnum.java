package br.gov.ba.seia.enumerator;

public enum TipoCadastroEnum {
	
	IMOVEL_RURAL("Rural"),
	ASSENTAMENTOS("Assentamento"),
	PCT("PCT");
	
	private String tipo;
	
	private TipoCadastroEnum(String tipo) {
		this.tipo = tipo;
	}

	public String getTipo() {
		return tipo;
	}
}
