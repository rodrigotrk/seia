package br.gov.ba.seia.enumerator;

public enum EstadoEnum {
	
	BAHIA(5);
	
	private int id;
	
	private EstadoEnum(int id){
		this.id = id;
	}
	
	public int getId(){
		return this.id;	
	}

}
