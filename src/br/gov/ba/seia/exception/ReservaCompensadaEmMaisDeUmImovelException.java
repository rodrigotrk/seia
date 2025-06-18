package br.gov.ba.seia.exception;
/**
 * Tratamento de exceção para reserva compensada em mais de um imovel
 * @author 
 *
 */
public class ReservaCompensadaEmMaisDeUmImovelException extends Exception {

	private static final long serialVersionUID = 2268221372087166553L;
	
	public ReservaCompensadaEmMaisDeUmImovelException(String mensagem) {
		super(mensagem);
	}
}
