package br.gov.ba.seia.enumerator;

public enum StatusCadastroImovelRuralEnum {
	
	REGISTRO_INCOMPLETO(0),
	REGISTRADO(1),
	@Deprecated
	CADASTRO_INCOMPLETO(2),
	CADASTRADO(3);

	private Integer id;
	
	StatusCadastroImovelRuralEnum(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
