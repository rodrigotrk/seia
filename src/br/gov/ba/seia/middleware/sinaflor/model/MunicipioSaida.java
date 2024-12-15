package br.gov.ba.seia.middleware.sinaflor.model;
/**
 * Classe modelo municipio de saida
 * @author 
 *
 */
public class MunicipioSaida {

	private Integer id;
	private String nome;
	private String uf;

	public MunicipioSaida() {

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

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	@Override
	public String toString() {
		return "MunicipioSaida [id=" + id + ", nome=" + nome + ", uf=" + uf + "]";
	}
	
	

}
