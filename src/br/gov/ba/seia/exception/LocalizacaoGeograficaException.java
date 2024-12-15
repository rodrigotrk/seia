package br.gov.ba.seia.exception;
/**
 * Tratamento de exceção para localização geografica
 * @author 
 *
 */
public class LocalizacaoGeograficaException extends Exception {
	
	private static final long serialVersionUID = 1521086352246362130L;
	
	public LocalizacaoGeograficaException(String mensagem) {
		super(mensagem);
	}

	public LocalizacaoGeograficaException() {
	}
}
