package br.gov.ba.seia.enumerator;

public enum CaepogCategoriaDocumentoEnum {

	CAMPO(1),
	LOCACAO(2),
	POCO(3),
	DOCUMENTOADCIONAIS(4);
	
	private int id;
	
	private CaepogCategoriaDocumentoEnum(int id){
		this.id = id;
	}
	
	public int getId(){
		return this.id;	
	}
}
