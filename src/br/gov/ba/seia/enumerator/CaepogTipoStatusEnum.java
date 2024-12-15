package br.gov.ba.seia.enumerator;

public enum CaepogTipoStatusEnum {

	CADASTRO_INCOMPLETO(1, "Cadastro Incompleto"),
	CADASTRO_COMPLETO(2, "Cadastro Completo"),
	AGUARDANDO_VALIDACAO(3, "Aguardando Validação"),
	PENDENCIA_VALIDACAO(4, "Pendência de Validação"),
	VALIDADO(5, "Validado");
	
	private int id;
	private String nome;
	
	private CaepogTipoStatusEnum(int id, String nome) {
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