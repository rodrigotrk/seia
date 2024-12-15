package br.gov.ba.seia.enumerator;

public enum OrgaoEnum {

	INEMA(1, 1);
	
	private Integer id;
	private Integer codOrgao;

	OrgaoEnum(Integer id, Integer idCod) {
		this.id = id;
		this.codOrgao = idCod;
	}

	public Integer getId() {
		return id;
	}

	public Integer getCodOrgao() {
		return codOrgao;
	}

	
}
