package br.gov.ba.seia.middleware.restful;
/**
 * 	Classe response do rest ful
 * @author 
 *
 */
public class RESTfulResponse {
	
	private Integer responseCode;
	private String responseContent;
	
	public RESTfulResponse() {
		
	}
	
	public RESTfulResponse(Integer responseCode, String responseContent) {
		this.responseCode=responseCode;
		this.setResponseContent(responseContent);
	}
	
	public Integer getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(Integer responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseContent() {
		return responseContent;
	}

	public void setResponseContent(String responseContent) {
		this.responseContent = responseContent;
	}
	
}
