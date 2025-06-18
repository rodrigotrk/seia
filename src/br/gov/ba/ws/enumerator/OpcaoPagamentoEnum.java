/**
 * 
 */
package br.gov.ba.ws.enumerator;

/**
 * @author lesantos
 *
 */
public enum OpcaoPagamentoEnum {

	INTEGRAL("Integral(Parcela única)"),
	PARECELADO("Parcelado");
	
	private String desc;
	
	private OpcaoPagamentoEnum(String desc){
		this.desc = desc;
	}
	
	public String getDesc() {
		return desc;
	}
}
