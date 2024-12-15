package br.gov.ba.seia.middleware.sinaflor.model;
/**
 * Classe modelo de dados de apoio de saida
 * @author 
 *
 */
public class DadosApoioSaida {
	
	private	String conteudo;
	private	String nome;
	
	public DadosApoioSaida() {
		
	}
	
	public DadosApoioSaida(String nome, String conteudo) {
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

}
