package br.gov.ba.seia.enumerator;

public enum CerhStatusEnum {

	CADASTRO_INCOMPLETO(1, "Cadastro Incompleto"),
	CADASTRO_COMPLETO(2, "Cadastro Completo"),
	CANCELADO(3, "Cancelado");
	
	private int id;
	private String nome;
	
	private CerhStatusEnum(int id, String nome) {
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