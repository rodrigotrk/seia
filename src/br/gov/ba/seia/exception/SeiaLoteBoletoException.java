package br.gov.ba.seia.exception;

import javax.ejb.ApplicationException;

/**
 * Tratamento de exceção para lote de boleto
 * @author 
 *
 */
@ApplicationException(rollback=true)
public class SeiaLoteBoletoException extends Exception {
	
	private static final long serialVersionUID = 7621777552532697058L;

	public SeiaLoteBoletoException(Throwable cause) {
		super(cause);
	}
	
	public SeiaLoteBoletoException(String message) {
		super(message);
	}
		
	public SeiaLoteBoletoException(String message, Throwable cause) {
		super(message,cause);
	}

}
