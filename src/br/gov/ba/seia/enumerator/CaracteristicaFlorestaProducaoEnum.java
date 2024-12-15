package br.gov.ba.seia.enumerator;

public enum CaracteristicaFlorestaProducaoEnum {

	
	VINCULADA(1,"Vinculada a Reposição Florestal"), 
	DESTINADA(2,"Destinada a carvoejamento"),
	COMPOSTA(3,"Composta por essências nativas"),	
	NENHUMA(4,"Nenhuma das características anteriores")	
	;
	
	private Integer id;
	private String nome;

	CaracteristicaFlorestaProducaoEnum(Integer id, String nome) {
		this.id = id;
		this.setNome(nome);
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