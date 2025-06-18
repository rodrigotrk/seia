package br.gov.ba.seia.enumerator;

public enum TabViewStep {
	TIPOLOGIA("abatipologia"),
	CNAE("abacnae"),
	FORMA_MANEJO("abaformmanejo");
	
	private String id;
	
	TabViewStep(String id){
		this.id = id;
	}
	
	public String getId(){
		return this.id;
	}
	
	@Override
	public String toString(){
		return this.id;	
	}
}	
