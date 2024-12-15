package br.gov.ba.seia.middleware.sinaflor.model;
/**
 * Classe modelo responsavel tecnico de saida
 * @author 
 *
 */
public class ResponsavelTecnicoSaida {
	
	private Integer art;
	private PessoaSaida pessoa;
	
	public ResponsavelTecnicoSaida() {
		
	}

	public Integer getArt() {
		return art;
	}

	public void setArt(Integer art) {
		this.art = art;
	}

	public PessoaSaida getPessoa() {
		return pessoa;
	}

	public void setPessoa(PessoaSaida pessoa) {
		this.pessoa = pessoa;
	}
	
	
	
}
