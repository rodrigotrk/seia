package br.gov.ba.seia.enumerator;

public enum PaisEnum {
	
	BRASIL(1);
	
	private int id;
	
	private PaisEnum(int id){
		this.id = id;
	}
	
	public int getId(){
		return this.id;	
	}
}
