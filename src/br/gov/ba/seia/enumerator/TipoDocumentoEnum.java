package br.gov.ba.seia.enumerator;

public enum TipoDocumentoEnum {
	CTPS("CTPS",3),
	CARTEIRA_DE_IDENT_DE_CONS_DE_CLASSE("Carteira de identidade de conselho de classe",5),
	CPF("Cpf", 6),
	CNPJ("Cnpj", 7);

	
	private String desc;
	private Integer id;
	
	TipoDocumentoEnum(String desc, Integer id){
		this.desc = desc;
		this.id = id;
	}
			
	@Override
	public String toString(){
		return this.desc;
	}
	
	public Integer getValue(){
		return id;
	}
	
}
