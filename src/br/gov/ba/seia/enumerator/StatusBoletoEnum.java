package br.gov.ba.seia.enumerator;

/**
 * Enum para os possiveis status do boleto.
 * 
 * @since 08/11/13
 * @author vitor.leitao
 */
public enum StatusBoletoEnum {
	
	EMITIDO(1, "Emitido"),
	CANCELAMENTO_SOLICITADO(2, "Cancelamento solicitado"),
	COMPROVANTE(3, "Comprovante enviado"),
	PAGO(4, "Pago"),
	CANCELADO(5, "Cancelado"),
	VENCIDO(6, "Vencido"),
	CANCELAMENTO_REJEITADO(7, "Cancelamento rejeitado"),
	PENDENCIA_VALIDACAO(8, "Pendência de validação de comprovante"),
	EM_PROCESSAMENTO(9, "Em Processamento");
	
	private int id;
	private String value;
	
	/**
	 * Construtor default.
	 * 
	 * @param id
	 * @param value
	 */
	private StatusBoletoEnum(int id, String value) {
		setId(id);
		setValue(value);
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
}
