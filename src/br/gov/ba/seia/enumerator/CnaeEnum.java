package br.gov.ba.seia.enumerator;

public enum CnaeEnum {

	INDUSTRIAS_EXTRATIVAS(173, "B","INDÚSTRIAS EXTRATIVAS")
	;
	
	private Integer idCNAE;
	private String codCNAE;
	private String desCNAE;

	CnaeEnum(Integer idCNAE, String codCNAE, String desCNAE) {
		this.idCNAE = idCNAE;
		this.codCNAE = codCNAE;
		this.desCNAE = desCNAE;
	}

	public Integer getIdCNAE() {
		return idCNAE;
	}

	public String getCodCNAE() {
		return codCNAE;
	}

	public String getDesCNAE() {
		return desCNAE;
	}
	
}
