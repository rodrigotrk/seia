package br.gov.ba.seia.enumerator;

public enum UsoBarragem {
	CAPTACAO(1), LANCAMENTO(2), TODOS(3);

	private Integer id;

	private UsoBarragem(Integer pId) {
		this.id = pId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLabel() {
		return this.getDeclaringClass().getCanonicalName() + "." + this.name();
	}
}
