package br.gov.ba.seia.middleware.restful;
/**
 * Classe do metodo request
 * @author 
 *
 */
public enum RequestMethod {
	
	DELETE("DELETE"),
	GET("GET"),
	POST("POST"),
	PUT("PUT")
	;
	
	private String requestMethod;
	
	private RequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}
	
	public String toString() {
		return this.requestMethod;
	}
}
