package br.gov.ba.seia.middleware.bcb.enumerator;
/**
 * Enum do BANCO CENTRAL 
 * @author 
 *
 */
public enum BancoCentralApiEnum {
	
	DADOS("/dados"),
	SELIC_MES("/bcdata.sgs.4390")
	;
	
	private String servico;
	
	private BancoCentralApiEnum(String servico) {
		this.servico = servico;
	}
	
	public String toString() {
		return this.servico;
	}
}
