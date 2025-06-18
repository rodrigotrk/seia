package br.gov.ba.seia.enumerator;

public enum TipoLocalAtividadeInexigivelEnum {
	
	LOCAL_ESPECIFICO(1),
	DIVERSOS_MUNICIPIOS_NA_BAHIA(2);
	
	private Integer id;
	
	private TipoLocalAtividadeInexigivelEnum(Integer id){
		this.id = id;
	}
	
	public Integer getId(){
		return this.id;	
	}

}
