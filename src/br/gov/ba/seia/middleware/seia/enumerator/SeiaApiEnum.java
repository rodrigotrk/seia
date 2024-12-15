package br.gov.ba.seia.middleware.seia.enumerator;
/**
 * Enum referente ao envio do dae e baixa de boleto
 * @author 
 *
 */
public enum SeiaApiEnum {
	
	TRANSMITIR_DAE("/dae/transmitir"),
	BAIXA_BOLETO("/boleto/baixa")
	;
	
	private String servico;
	
	private SeiaApiEnum(String servico) {
		this.servico = servico;
	}
	
	public String toString() {
		return this.servico;
	}
}
