package br.gov.ba.seia.enumerator;

public enum TipoCaptacaoEnum {
	
	CAPTACAO_CONCESSIONARIA_PUBLICA(1), CAPTACAO_PRECIPITACAO_METEOROLOGICA_PLUVIAL(2), CAPTACAO_SUPERFICIAL(3), CAPTACAO_SUBTERRANEA(4);
	
	private Integer id;
	
	private TipoCaptacaoEnum(Integer id) {
	
		this.id = id;
	}
	
	public Integer getId() {
	
		return id;
	}
	
}