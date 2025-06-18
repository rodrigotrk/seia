package br.gov.ba.seia.exception;

import javax.ejb.ApplicationException;
/**
 * Tratamento de exceção para tempo de execução
 * @author 
 *
 */
@ApplicationException(rollback=true)
public class SeiaRuntimeException extends RuntimeException {

	private static final long serialVersionUID = -4308632597093449384L;
	
	public SeiaRuntimeException(Throwable cause) {
		super(cause);
	}
	
	public SeiaRuntimeException(String message) {
		super(message);
	}
		
	public SeiaRuntimeException(String message, Throwable cause) {
		super(message,cause);
	}
}
