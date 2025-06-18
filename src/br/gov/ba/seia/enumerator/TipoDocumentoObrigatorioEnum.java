package br.gov.ba.seia.enumerator;

public enum TipoDocumentoObrigatorioEnum {
	
	DOCUMENTO(1),
	ESTUDO(2),
	DOCUMENTO_ADICIONAL(3)
	;

	private Integer id;
	
	TipoDocumentoObrigatorioEnum(Integer id){
		this.id = id;
	}
			
	public Integer getId(){
		return id;
	}
	
}
