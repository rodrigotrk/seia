package br.gov.ba.seia.enumerator;

public enum FinalidadeEnum {
	REPRODUCAO(1), 
	CRIA(2), 
	RECRIA(3),
	TERMINACAO(4),
	LEITE(5),
	CRECHE(6),
	OVOS(7),
	OUTROS(8);

	private Integer id;

	private FinalidadeEnum(Integer Id) {
		this.id = Id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
