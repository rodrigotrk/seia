package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the agro_classificacao_toxicologica database table.
 * 
 */
@Embeddable
public class AgroClassificacaoToxicologicaPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="ide_fce_agrossilvopastoril")
	private Integer ideFceAgrossilvopastoril;

	@Column(name="ide_classificacao_toxicologica")
	private Integer ideClassificacaoToxicologica;

    public AgroClassificacaoToxicologicaPK() {
    }
    
    public AgroClassificacaoToxicologicaPK(Integer ideFceAgrossilvopastoril, Integer ideClassificacaoToxicologica) {
    	this.ideFceAgrossilvopastoril=ideFceAgrossilvopastoril;
    	this.ideClassificacaoToxicologica=ideClassificacaoToxicologica;
    }
    
	public Integer getIdeFceAgrossilvopastoril() {
		return this.ideFceAgrossilvopastoril;
	}
	public void setIdeFceAgrossilvopastoril(Integer ideFceAgrossilvopastoril) {
		this.ideFceAgrossilvopastoril = ideFceAgrossilvopastoril;
	}
	public Integer getIdeClassificacaoToxicologica() {
		return this.ideClassificacaoToxicologica;
	}
	public void setIdeClassificacaoToxicologica(Integer ideClassificacaoToxicologica) {
		this.ideClassificacaoToxicologica = ideClassificacaoToxicologica;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof AgroClassificacaoToxicologicaPK)) {
			return false;
		}
		AgroClassificacaoToxicologicaPK castOther = (AgroClassificacaoToxicologicaPK)other;
		return 
			this.ideFceAgrossilvopastoril.equals(castOther.ideFceAgrossilvopastoril)
			&& this.ideClassificacaoToxicologica.equals(castOther.ideClassificacaoToxicologica);

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.ideFceAgrossilvopastoril.hashCode();
		hash = hash * prime + this.ideClassificacaoToxicologica.hashCode();
		
		return hash;
    }
}