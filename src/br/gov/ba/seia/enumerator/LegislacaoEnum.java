package br.gov.ba.seia.enumerator;

public enum LegislacaoEnum {
	ART180(1);
	
	
	private Integer id;
	
	private LegislacaoEnum(Integer id){
		this.id = id;
	}
	
	public Integer getId() {
		return id;
	}

}
