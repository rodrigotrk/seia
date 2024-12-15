package br.gov.ba.seia.middleware.sinaflor.model;
/**
 * Classe modelo retorno sucesso criacao
 * @author 
 *
 */
public class RetornoSucessoCriacao {

	private Integer id;
	private String message;

	public RetornoSucessoCriacao() {

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "RetornoSucessoCriacao [id=" + id + ", message=" + message + "]";
	}
	
}
