package br.gov.ba.seia.enumerator;

public enum DiaEnum {
	
	SEGUNDA(1),
	TERCA(2),
	QUARTA(3),
	QUINTA(4),
	SEXTA(5),
	SABADO(6),
	DOMINGO(7);
	
	private String desc;
	private Integer id;
	
	DiaEnum(Integer id){		
		this.id = id;
	}
			
	@Override
	public String toString(){
		return this.desc;
	}
	
	public Integer getValue(){
		return id;
	}
}
