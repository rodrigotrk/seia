package br.gov.ba.seia.middleware.sinaflor.model;
/**
 * Classe modelo de taxa de saida
 * @author 
 *
 */
public class TaxonomiaSaida {

	private Integer id;
	private String nome;

	public TaxonomiaSaida() {

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
}
