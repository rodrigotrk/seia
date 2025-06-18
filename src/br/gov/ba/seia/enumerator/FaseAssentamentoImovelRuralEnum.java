package br.gov.ba.seia.enumerator;

public enum FaseAssentamentoImovelRuralEnum {
	
	EM_OBTENCAO(1),
	IMPLANTADO(2);

	private Integer id;

	private FaseAssentamentoImovelRuralEnum(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
