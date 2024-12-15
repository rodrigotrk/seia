package br.gov.ba.seia.enumerator;

public enum ModalidadeOutorgaEnum {

	CAPTACAO(1), 
	LANCAMENTO_EFLUENTES(2,304), 
	INTERVENCAO(3,305), 
	POTENCIAL_HIDRELETRICO(4), 
	CAPTACAO_SUPERFICIAL(1,302), 
	CAPTACAO_SUBTERRANEA(1,303), 
	POCO(5);
	

	private Integer idModalidade;
	private Integer idTipologia;
	
	private ModalidadeOutorgaEnum(Integer id) {
		this.idModalidade = id;
	}
	
	private ModalidadeOutorgaEnum(Integer id, Integer idTipologia) {
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
