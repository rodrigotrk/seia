package br.gov.ba.seia.exception;

import javax.ejb.ApplicationException;
/**
 * Tratamento de exceções
 * @author 
 *
 */
@SuppressWarnings("serial")
@ApplicationException(rollback=true)
public class AppExceptionError extends RuntimeException {
	
	public AppExceptionError(String message){
		super(message);
	}
}
