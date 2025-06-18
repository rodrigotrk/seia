package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the fce_asv_classi_vegetacao database table.
 * 
 */
@Embeddable
public class FceAsvClassiVegetacaoPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="ide_fce_asv")
	private Integer ideFceAsv;

	@Column(name="ide_classificacao_vegetacao")
	private Integer ideClassificacaoVegetacao;

    public FceAsvClassiVegetacaoPK() {
	}
	public FceAsvClassiVegetacaoPK(Integer fceAsv, Integer classificacaoVegetacao) {
    	this.ideFceAsv=fceAsv;
    	this.ideClassificacaoVegetacao=classificacaoVegetacao;
    }
	public Integer getIdeFceAsv() {
		return this.ideFceAsv;
	}
	public void setIdeFceAsv(Integer ideFceAsv) {
		this.ideFceAsv = ideFceAsv;
	}
	public Integer getIdeCalssificacaoVegetacao() {
		return this.ideClassificacaoVegetacao;
	}
	public void setIdeCalssificacaoVegetacao(Integer ideCalssificacaoVegetacao) {               
		this.ideClassificacaoVegetacao = ideCalssificacaoVegetacao;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof FceAsvClassiVegetacaoPK)) {
			return false;
		}
		FceAsvClassiVegetacaoPK castOther = (FceAsvClassiVegetacaoPK)other;
		return 
			this.ideFceAsv.equals(castOther.ideFceAsv)
			&& this.ideClassificacaoVegetacao.equals(castOther.ideClassificacaoVegetacao);

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.ideFceAsv.hashCode();
		hash = hash * prime + this.ideClassificacaoVegetacao.hashCode();
		
		return hash;
    }
}