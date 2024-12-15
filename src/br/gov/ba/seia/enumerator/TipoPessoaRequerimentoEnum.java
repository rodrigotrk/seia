package br.gov.ba.seia.enumerator;

public enum TipoPessoaRequerimentoEnum {
	REQUERENTE(1,"Requerente"),
	PROCURADOR(2,"Procurador"),
	REPRESENTANTE_LEGAL(3,"Representante Legal"),
	ATENDENTE(4, "Atendente"),
	CONVENIADO(5, "Conveniado");
	
	
	private String desc;
	private Integer id;


	private TipoPessoaRequerimentoEnum(Integer id, String desc) {
		this.id = id;
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

}
