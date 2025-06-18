package br.gov.ba.seia.middleware.restful;
/**
 * Classe parametro request
 * @author 
 *
 */
public class RequestParameter {

	private String parameter;
	private String value;

	public RequestParameter(String parameter, String value) {
		this.parameter=parameter;
		this.value=value;
	}
	
	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public String toString() {
		return parameter + "=" + value;
	}

}
