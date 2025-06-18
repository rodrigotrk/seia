package br.gov.ba.seia.exception;

/**
 * Tratamento de exceção para limite de usuario 
 * @author 
 *
 */
public class SeiaUserLimitException extends RuntimeException {

	private static final long serialVersionUID = -4308632597093449384L;
	
	public SeiaUserLimitException(Throwable cause) {
		super(cause);
	}
	
	public SeiaUserLimitException(String message) {
		super(message);
	}
		
	public SeiaUserLimitException(String message, Throwable cause) {
		super(message,cause);
	}
}
