package br.gov.ba.seia.enumerator;

public enum TipoCaracterizacaoProjetoEnum {
	PUBLICO(1), MISTO(2), PRIVADO(3);

	private Integer id;

	private TipoCaracterizacaoProjetoEnum(Integer Id) {
		this.id = Id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
