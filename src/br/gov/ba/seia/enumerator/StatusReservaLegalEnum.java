package br.gov.ba.seia.enumerator;

public enum StatusReservaLegalEnum {
	
	APROVADA(1), 
	AVERBADA(2),
	CADASTRADA(3), 
	AGUARDANDO_VALIDACAO_DOCUMENTO(4), 
	NAO_CADASTRADA(7), 
	APROVADA_AGUARDANDO_VALIDACAO(8), 
	DOCUMENTO_VALIDADO(9),
	APROVADO_AGUARDANDO_CONCLUSAO_PROCESSO(10),
	RELOCADA(11)
	
	;
	
	private int id;
	
	private StatusReservaLegalEnum(int id){
		this.id = id;
	}
	
	public int getId(){
		return this.id;	
	}

}