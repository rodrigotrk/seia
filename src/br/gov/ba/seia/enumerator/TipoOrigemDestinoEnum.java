package br.gov.ba.seia.enumerator;

public enum TipoOrigemDestinoEnum {

	SALDO_DISPONIVEL_TCCA(1, "Saldo disponível do TCCA"),
	SALDO_OUTRO_PROJETO(2, "Saldo de outro projeto"),
	SALDO_OUTRO_TCCA(3, "Saldo de outro TCCA"),
	SALDO_PRODUTO(4, "Saldo do produto"),
	SALDO_SUPLEMENTADO(5, "Saldo suplementado");
	
	private int id;
	private String nome;
	
	private TipoOrigemDestinoEnum(int id, String nome) {
		this.id = id;
		this.nome = nome;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
}