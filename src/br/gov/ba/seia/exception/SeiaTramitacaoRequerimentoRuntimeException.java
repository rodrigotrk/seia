package br.gov.ba.seia.exception;

import javax.ejb.ApplicationException;

/**
 * Tratamento de exceção para tempo de execução na tramitação do requerimento
 * @author 
 *
 */
@ApplicationException(rollback=true)
public class SeiaTramitacaoRequerimentoRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 4174700344700840287L;

	public SeiaTramitacaoRequerimentoRuntimeException(Throwable cause) {
		super(cause);
	}
	
	public SeiaTramitacaoRequerimentoRuntimeException(String message) {
		super(message);
	}
		
	public SeiaTramitacaoRequerimentoRuntimeException(String message, Throwable cause) {
		super(message,cause);
	}

}
