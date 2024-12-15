package br.gov.ba.seia.enumerator;

public enum TipoPocoEnum {

	TUBULAR(1),
	OUTROS(2);	
	private Integer id;

	private TipoPocoEnum(Integer id){
		this.id = id;
	}
	
	public Integer getId() {
		return id;
	}

}
