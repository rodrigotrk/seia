package br.gov.ba.seia.enumerator;

public enum TccaAcaoEnum {
	
	RENOVAR_PRAZO(1),
	DUPLICAR(2),
	REAJUSTAR_VALOR(3)
	;
	
	private int id;
	
	private TccaAcaoEnum(int id){
		this.id = id;
	}
	
	public int getId(){
		return this.id;	
	}

}
