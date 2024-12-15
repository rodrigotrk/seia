package br.gov.ba.seia.enumerator;

public enum ProgramaGovernoEnum {

	LUZ(1, "Luz para Todos"),
	UNIVERSAL(2, "Programa de Universalização"),
	ANEEL(3, "Programa ANEEL");

	private Integer id;
	private String nome;

	private ProgramaGovernoEnum(Integer id, String nome) {
		this.id = id;
		this.nome = nome;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
}