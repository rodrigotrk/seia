package br.gov.ba.seia.enumerator;

public enum MotivoCancelamentoEnum {

	MOTIVO_UM(1), MOTIVO_DOIS(2);
	

	private Integer idModalidade;
	private Integer idTipologia;
	
	private MotivoCancelamentoEnum(Integer id) {
		this.idModalidade = id;
	}
	
	private MotivoCancelamentoEnum(Integer id, Integer idTipologia) {
		this.idModalidade = id;
		this.idTipologia = idTipologia;
	}

	public Integer getIdModalidade() {
		return idModalidade;
	}
	
	public Integer getIdTipologia() {
		return idTipologia;
	}

}
