package br.gov.ba.seia.exception;
/**
 * Tratamento de excecão ao informar codigo de IBGE do municipio invalido
 * @author 
 *
 */
public class CodigoIbgeMunicipioInvalidoException extends Exception {
	private static final long serialVersionUID = 2243472078005170161L;
	
	public CodigoIbgeMunicipioInvalidoException(String mensagem) {
		super(mensagem);
	}
}
