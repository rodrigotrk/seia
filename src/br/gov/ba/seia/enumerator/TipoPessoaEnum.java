package br.gov.ba.seia.enumerator;

public enum TipoPessoaEnum {
	FISICA(0,"Física"),
	JURIDICA(1,"Juridica");
	
	private String desc;
	private Integer id;
	
	
	private TipoPessoaEnum(Integer id, String desc) {
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
