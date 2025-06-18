package br.gov.ba.seia.enumerator;

public enum PessoaENUM {

	FISICA(1), JURIDICA(2);

	private Integer id;

	private PessoaENUM(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

}
