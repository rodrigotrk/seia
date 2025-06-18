package br.gov.ba.seia.enumerator;

public enum TipoAlteracaoEnum {
	
	AMPLIAR_VAZAO_PONTO_CAPTACAO(1),
	DEDUZIR_VAZAO_PONTO_CAPTACAO(2),
	INCLUIR_PONTO_CAPTACAO_LANCAMENTO(3),
	REMOVER_CANCELAR_PONTO_CAPTACAO_LANCAMENTO(4),
	RELOCAR_PONTO_CAPTACAO_LANCAMENTO(5);
	
	private Integer id;
	
	private TipoAlteracaoEnum(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}


}
