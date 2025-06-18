package br.gov.ba.seia.enumerator;

public enum INCRAEnum {
	
	CNPJ1("00375972000756"),
	CNPJ2("00375972002104"),
	CNPJ3("00375972003348");
	
	private String numeroCnpj;
	
	INCRAEnum(String numeroCnpj){		
		this.numeroCnpj = numeroCnpj;
	}

	public String getNumeroCnpj() {
		return numeroCnpj;
	}
}
