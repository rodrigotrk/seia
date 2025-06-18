package br.gov.ba.seia.enumerator;

public enum BancoEnum {

	BANCO_DO_BRASIL("001");
	
	private String codigo;

	private BancoEnum(String codigo) {
		this.codigo = codigo;
	}
	
	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	
	
}
