package br.gov.ba.seia.middleware.sinaflor.model;
/**
 * Classe modelo de detalhe de erro
 * @author 
 *
 */
public class DetalheErro {
	
	private String message;
	
	public DetalheErro() {
		
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "DetalheErro [message=" + message + "]";
	}

}
