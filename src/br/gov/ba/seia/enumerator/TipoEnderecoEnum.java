package br.gov.ba.seia.enumerator;

public enum TipoEnderecoEnum {
	
	RESIDENCIAL(1,"Residencial"),
	CORRESPONDENCIA(2,"Correspondencia"),
	COMERCIAL(3,"Comercial"),
	LOCALIZACAO(4,"Localizacao"),
	GERACAO_RESIDUO(5,"Geração Resíduo"),
	DESTINACAO_RESIDUO(6, "Destinação Resíduo");
	
	
	private String desc;
	private Integer id;
	
	private TipoEnderecoEnum(Integer id, String desc) {
		this.id = id;
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

	public Integer getId() {
		return id;
	}


}
