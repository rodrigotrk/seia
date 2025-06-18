package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the agro_classificacao_defensivo database table.
 * 
 */
@Embeddable
public class AgroClassificacaoDefensivoPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="ide_fce_agrossilvopastoril")
	private Integer ideFceAgrossilvopastoril;

	@Column(name="ide_classificacao_defensivo_agricola")
	private Integer ideClassificacaoDefensivoAgricola;

    public AgroClassificacaoDefensivoPK() {
    }
    
    public AgroClassificacaoDefensivoPK(Integer ideFceAgrossilvopastoril, Integer ideClassificacaoDefensivoAgricola){
    	this.ideFceAgrossilvopastoril=ideFceAgrossilvopastoril;
    	this.ideClassificacaoDefensivoAgricola=ideClassificacaoDefensivoAgricola;
    }
    
	public Integer getIdeFceAgrossilvopastoril() {
		return this.ideFceAgrossilvopastoril;
	}
	public void setIdeFceAgrossilvopastoril(Integer ideFceAgrossilvopastoril) {
		this.ideFceAgrossilvopastoril = ideFceAgrossilvopastoril;
	}
	public Integer getIdeClassificacaoDefensivoAgricola() {
		return this.ideClassificacaoDefensivoAgricola;
	}
	public void setIdeClassificacaoDefensivoAgricola(Integer ideClassificacaoDefensivoAgricola) {
		this.ideClassificacaoDefensivoAgricola = ideClassificacaoDefensivoAgricola;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof AgroClassificacaoDefensivoPK)) {
			return false;
		}
		AgroClassificacaoDefensivoPK castOther = (AgroClassificacaoDefensivoPK)other;
		return 
			this.ideFceAgrossilvopastoril.equals(castOther.ideFceAgrossilvopastoril)
			&& this.ideClassificacaoDefensivoAgricola.equals(castOther.ideClassificacaoDefensivoAgricola);

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.ideFceAgrossilvopastoril.hashCode();
		hash = hash * prime + this.ideClassificacaoDefensivoAgricola.hashCode();
		
		return hash;
    }
}