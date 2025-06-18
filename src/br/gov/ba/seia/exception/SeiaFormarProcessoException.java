package br.gov.ba.seia.exception;

import javax.ejb.ApplicationException;

/**
 * Tratamento de exceção para formar processo
 * @author 
 *
 */
@ApplicationException(rollback=true)
public class SeiaFormarProcessoException extends Exception {
	
	private static final long serialVersionUID = 7621777552532697058L;

	public SeiaFormarProcessoException(Throwable cause) {
		super(cause);
	}
	
	public SeiaFormarProcessoException(String message) {
		super(message);
	}
		
	public SeiaFormarProcessoException(String message, Throwable cause) {
		super(message,cause);
	}

}
