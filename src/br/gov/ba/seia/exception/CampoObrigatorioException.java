package br.gov.ba.seia.exception;
/**
 * Tratamento de exceção ao não preencher campo obrigatório
 * @author 
 *
 */
public class CampoObrigatorioException extends Exception {

	private static final long serialVersionUID = 2268221372087166553L;
	
	public CampoObrigatorioException(String mensagem) {
		super(mensagem);
	}
}
