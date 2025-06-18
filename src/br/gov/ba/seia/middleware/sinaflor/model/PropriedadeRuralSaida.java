package br.gov.ba.seia.middleware.sinaflor.model;
/**
 * Classe modelo de propriedade rural de saida
 * @author 
 *
 */
public class PropriedadeRuralSaida {
	
	private String codigoCAR;
	private Integer id;
	private String nome;
	
	public PropriedadeRuralSaida() {
		
	}

	public String getCodigoCAR() {
		return codigoCAR;
	}

	public void setCodigoCAR(String codigoCAR) {
		this.codigoCAR = codigoCAR;
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
