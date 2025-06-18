package br.gov.ba.seia.middleware.sinaflor.model;
/**
 * Classe modelo pessoa de saida
 * @author 
 *
 */
public class PessoaSaida {
	
	private Integer id;
	private	String identificacao;
	private String nome;
	
	public PessoaSaida() {
		
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getIdentificacao() {
		return identificacao;
	}
	
	public void setIdentificacao(String identificacao) {
		this.identificacao = identificacao;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public String toString() {
		return "PessoaSaida [id=" + id + ", identificacao=" + identificacao + ", nome=" + nome + "]";
	}

}
