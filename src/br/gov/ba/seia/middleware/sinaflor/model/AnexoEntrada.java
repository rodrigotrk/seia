package br.gov.ba.seia.middleware.sinaflor.model;
/**
 * Classe modelo anexo de entrada
 * @author 
 *
 */
public class AnexoEntrada {
	
	private	String conteudo;
	private	String nome;
	
	public AnexoEntrada() {
		
	}
	
	public AnexoEntrada(String nome, String conteudo) {
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
