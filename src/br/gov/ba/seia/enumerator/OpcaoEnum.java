package br.gov.ba.seia.enumerator;

public enum OpcaoEnum {

	EXCLUSAO(1,"Solicitar Exclusão"), INCLUSAO(2,"Solicitar Inclusão"), MUDANCA_VALOR(3,"Aumentar em"), REDUZIR_PARA(4, "Reduzir para");

	private Integer id;

	private String descricao;

	private OpcaoEnum(Integer id, String descricao) {
		this.id = id;
		this.descricao = descricao;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public OpcaoEnum getOpcao(Integer ide) {
		if(ide.equals(1))
			return EXCLUSAO;
		else if(ide.equals(2))
			return INCLUSAO;
		else if(ide.equals(3))
			return MUDANCA_VALOR;
		else if(ide.equals(4))
			return REDUZIR_PARA;
		else
			return null;
	}

}
