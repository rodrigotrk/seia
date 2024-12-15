package br.gov.ba.seia.enumerator;

public enum FaseEmpreendimentoEnum {

	LOCALIZACAO(1), IMPLANTACAO(2),OPERACAO(3), ALTERACAO(4);
	
	private Integer id;
	
	private FaseEmpreendimentoEnum(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	
	
}
