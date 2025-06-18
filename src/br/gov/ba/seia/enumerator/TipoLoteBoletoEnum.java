package br.gov.ba.seia.enumerator;

public enum TipoLoteBoletoEnum {

	
	REMESSA(1,"Remessa"), 
	RETORNO(2,"Retorno"),
	;
	
	private Integer id;
	private String dscTipo;

	private TipoLoteBoletoEnum(Integer id, String dscTipoLoteBoleto) {
		this.id = id;
		this.setDscTipo(dscTipoLoteBoleto);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDscTipo() {
		return dscTipo;
	}

	public void setDscTipo(String dscTipo) {
		this.dscTipo = dscTipo;
	}
}
