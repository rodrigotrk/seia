package br.gov.ba.seia.middleware.sinaflor.model;
/**
 * Classe modelo unidade ibama saida
 * @author 
 *
 */
public class UnidadeIbamaSaida {
	
	private Integer id;
	private String nome;
	
	public UnidadeIbamaSaida() {
		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public String toString() {
		return "UnidadeIbamaSaida [ id=" + id + ", nome=" + nome + "]";
	}
	
	

}
