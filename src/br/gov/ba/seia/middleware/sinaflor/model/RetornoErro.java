package br.gov.ba.seia.middleware.sinaflor.model;

import java.util.List;
/**
 * Classe modelo de retorno de erro
 * @author 
 *
 */
public class RetornoErro {
	
	private Long codigoErro;
	private	String message;
	private	List<DetalheErro> subErrors;
	private String timestamp;
	
	public RetornoErro() {
		
	}

	public Long getCodigoErro() {
		return codigoErro;
	}

	public void setCodigoErro(Long codigoErro) {
		this.codigoErro = codigoErro;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<DetalheErro> getSubErrors() {
		return subErrors;
	}

	public void setSubErrors(List<DetalheErro> subErrors) {
		this.subErrors = subErrors;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "RetornoErro [codigoErro=" + codigoErro + ", message=" + message + ", subErrors=" + subErrors
				+ ", timestamp=" + timestamp + "]";
	}
	
	

}
