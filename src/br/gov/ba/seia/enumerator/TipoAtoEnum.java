package br.gov.ba.seia.enumerator;

public enum TipoAtoEnum {
	
	LICENCA(1),
	ANUENCIA_UC(2),
	FLORESTAL(3),
	OUTORGA(4),
	DECLARATORIO(7),
	JURIDICO(8),
	AUTORIZACAO(9);
	
	private Integer id;
	
	private TipoAtoEnum(Integer i){
		this.id = i;
	}
	
	
	public Integer getId() {
		return id;
	}

}
