package br.gov.ba.seia.enumerator;

public enum BarragemEnum {

	OUTROS(361, "Outros");
	
	private int id;
	private String nome;
	
	private BarragemEnum(int id, String nome) {
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