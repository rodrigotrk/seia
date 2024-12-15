package br.gov.ba.seia.dto;
/**
 * Classe de interação entre com atributos de outras classes
 * @author 
 *
 */
public class AbaDTO {

	private int id;
	private String nome;
	private boolean isValid;
	private boolean disabled;
	private boolean abaAtual;

	
	
	public String getNome() {
		return nome;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public boolean isDisabled() {
		return disabled;
	}
	
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public boolean isAbaAtual() {
		return abaAtual;
	}

	public void setAbaAtual(boolean abaAtual) {
		this.abaAtual = abaAtual;
	}

	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

	@Override
	public String toString() {
		return "AbaDTO [nome=" + nome + "]";
	}
	
	
	
}
