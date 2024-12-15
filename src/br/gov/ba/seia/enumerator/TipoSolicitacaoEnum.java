package br.gov.ba.seia.enumerator;

public enum TipoSolicitacaoEnum {

	NOVA_LICENCA(1), 
	ALTERACAO_LICENCA(2), 
	RENOVACAO_LICENCA(3), 
	NOVA_OUTORGA(4), 
	RENOVACAO_OUTORGA(5), 
	ALTERACAO_OUTORGA(6),
	CANCELAMENTO_OUTORGA(7),
	NOVO_FLORESTAL(8),
	NOVA_FAUNA(9),
	PRORROGACAO_PRAZO_VALIDADE(10), 
	REVISAO_CONDICIONANTE(11), 
	ALTERACAO_RAZAO_SOCIAL(12), 
	TRANSFERENCIA_LICENCA_AMBIENTAL(13), 
	TLA(13), 
	POSSUI_LICENCA_MUNICIPAL_FEDERAL(14);

	private Integer id;

	private TipoSolicitacaoEnum(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

}
