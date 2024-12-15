package br.gov.ba.seia.middleware.restful;
/**
 * Enum request que retorna arquivo JSON E XML
 * @author 
 *
 */
public enum RequestContentType {
	
	JSON("application/json"),
	XML("application/xml"),
	;
	
	private String requestContentType;
	
	private RequestContentType(String requestContentType) {
		this.requestContentType = requestContentType;
	}
	
	public String toString() {
		return this.requestContentType;
	}
}
