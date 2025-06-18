package br.gov.ba.seia.enumerator;

public enum CategoriaDocumento {

	PORTARIA(1), CERTIFICADO(2), MINUTA(3), PARECER(4), RESPOSTA(5), OUTROS(6);

	private Integer id;

	private CategoriaDocumento(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

}
