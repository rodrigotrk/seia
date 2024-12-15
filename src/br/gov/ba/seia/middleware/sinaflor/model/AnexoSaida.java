package br.gov.ba.seia.middleware.sinaflor.model;
/**
 * Classe modelo anexo saida
 * @author 
 *
 */
public class AnexoSaida {
	
	private	String conteudo;
	private	String nome;
	
	public AnexoSaida() {
		
	}
	
	public AnexoSaida(String nome, String conteudo) {
		this.nome=nome;
		this.conteudo=conteudo;
	}
	
	public String getConteudo() {
		return conteudo;
	}
	
	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public String toString() {
		return "AnexoSaida [conteudo=" + conteudo + ", nome=" + nome + "]";
	}

}
