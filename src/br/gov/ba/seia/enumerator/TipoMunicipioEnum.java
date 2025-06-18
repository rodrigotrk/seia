package br.gov.ba.seia.enumerator;

public enum TipoMunicipioEnum {
	
	MUNICIPIO(1),
	DISTRITO(3),
	POVOADO(4);
	
	private int id;
	
	private TipoMunicipioEnum(int id){
		this.id = id;
	}
	
	public int getId(){
		return this.id;	
	}

}
