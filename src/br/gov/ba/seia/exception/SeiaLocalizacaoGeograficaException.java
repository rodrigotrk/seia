package br.gov.ba.seia.exception;

import javax.ejb.ApplicationException;

/**
 * Tratamento de exceção para localização geografica seia
 * @author 
 *
 */
@ApplicationException(rollback=true)
public class SeiaLocalizacaoGeograficaException extends Exception {

	private static final long serialVersionUID = -461022094409867929L;

	public SeiaLocalizacaoGeograficaException(Throwable cause) {
		super(cause);
	}
	
	public SeiaLocalizacaoGeograficaException(String message) {
		super(message);
	}
	
	public SeiaLocalizacaoGeograficaException(String message, Throwable cause) {
		super(message,cause);
	}

}
