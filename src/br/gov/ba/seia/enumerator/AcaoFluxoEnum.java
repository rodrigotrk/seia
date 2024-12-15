package br.gov.ba.seia.enumerator;

public enum AcaoFluxoEnum {
	
	ENCAMINHAR(1),
	NOTIFICAR(2),
	APROVAR_NOTIFICACAO(3),
	APENSAR_DOCUMENTO(4);
	
	private int id;
	
	private AcaoFluxoEnum(int id){
		this.id = id;
	}
	
	public int getId(){
		return this.id;	
	}

}
