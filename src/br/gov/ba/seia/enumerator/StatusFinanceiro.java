package br.gov.ba.seia.enumerator;

public enum StatusFinanceiro {
	
	AGUARDANDO_PAGAMENTO(6), AGUARDANDO_VALIDACAO(7), BOLETO_PAGO(8), CANCELADO(9), DECLARACAO_EMITIDA(19);
	
	private Integer id;
	
	private StatusFinanceiro(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLabel() {
		return this.getDeclaringClass().getCanonicalName() + "." + this.name();
	}
}
