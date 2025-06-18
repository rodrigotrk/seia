package br.gov.ba.seia.enumerator;

public enum TipoUsoRecursoHidricoEnum {
	
		BARRAGEM(1,"Barragem"),
		CAPTACAO_SUBTERRANEA(2,"Captação Subterrânea"),
		CAPTACAO_SUPERFICIAL(3, "Captação Superficial"),
		INTERVENCAO(4, "Intervenção"),
		LANCAMENTO_EFLUENTE(5, "Lançamento Efluente")
	;
	
	private int id;
	
	private String desc;
	
	private TipoUsoRecursoHidricoEnum(int id, String desc){
		this.id = id;
		this.desc = desc;
	}
	 
	public int getId(){
		return this.id;	
	}

	public String getDesc(){
		return this.desc;	
	}
	
	public static TipoUsoRecursoHidricoEnum getById(int id) { 
		for (TipoUsoRecursoHidricoEnum tipoUsoRecursoHidricoEnum : TipoUsoRecursoHidricoEnum.values()) {
			if(tipoUsoRecursoHidricoEnum.getId() == id ){
				return tipoUsoRecursoHidricoEnum;
			}
		}
		
		throw new IllegalArgumentException("Não encontrado!");
	}

	
}
