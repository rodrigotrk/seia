package br.gov.ba.seia.exception;

import javax.ejb.ApplicationException;

/**
 * Tratamento de exceção para seia
 * @author 
 *
 */
@ApplicationException(rollback=true)
public class SeiaException extends Exception {
	
	private static final long serialVersionUID = 7621777552532697058L;

	public SeiaException(Throwable cause) {
		super(cause);
	}
	
	public SeiaException(String message) {
		super(message);
	}
		
	public SeiaException(String message, Throwable cause) {
		super(message,cause);
	}

}
