package br.gov.ba.seia.middleware.sinaflor.exception;
/**
 * Classe de tempo de execução do sinaflor
 * @author 
 *
 */
public class SinaflorRuntimeException extends RuntimeException {
	
	private static final long serialVersionUID = -4308632597093449384L;
	
	public SinaflorRuntimeException(Throwable cause) {
		super(cause);
	}
	
	public SinaflorRuntimeException(String message) {
		super(message);
	}
		
	public SinaflorRuntimeException(String message, Throwable cause) {
		super(message,cause);
	}
	
}
