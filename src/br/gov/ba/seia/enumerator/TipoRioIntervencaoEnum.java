package br.gov.ba.seia.enumerator;

public enum TipoRioIntervencaoEnum {
	
	ESTADUAL(1),
	FEDERAL(2);
	
	private Integer id;
	
	private TipoRioIntervencaoEnum(Integer id){
		this.id = id;
	}
	
	public Integer getId(){
		return this.id;	
	}

}
