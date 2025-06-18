package br.gov.ba.seia.enumerator;

public enum CaepogTipoResiduoEnum {

	N_PARAFINA(1, "N-Parafina"),
	BASE_AGUA(2, "Base-Água"),
	OUTROS(3, "Outros");
	
	private int id;
	private String nome;
	
	private CaepogTipoResiduoEnum(int id, String nome) {
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