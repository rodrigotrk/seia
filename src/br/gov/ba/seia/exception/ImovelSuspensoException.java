package br.gov.ba.seia.exception;
/**
 * Tratamento de exceção para imovel suspenso
 * @author 
 *
 */
public class ImovelSuspensoException extends Exception {
	
	private static final long serialVersionUID = -5956340217740054693L;

	public ImovelSuspensoException(String mensagem) {
		super(mensagem);
	}
}
