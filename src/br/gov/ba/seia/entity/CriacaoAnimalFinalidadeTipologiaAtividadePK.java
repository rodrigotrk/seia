package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 * The primary key class for the criacao_animal_finalidade_tipo_atividade database table.
 * 
 */
@Embeddable
public class CriacaoAnimalFinalidadeTipologiaAtividadePK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;
	
	@NotNull
	@Column(name="ide_criacao_animal")
	private Integer ideCriacaoAnimal;
	
	@NotNull
	@Column(name="ide_finalidade_tipologia_atividade")
	private Integer ideFinalidadeTipoAtividade;

    public CriacaoAnimalFinalidadeTipologiaAtividadePK(Integer ideCriacaoAnimal,	Integer ideFinalidadeTipoAtividade) {
		this.ideCriacaoAnimal = ideCriacaoAnimal;
		this.ideFinalidadeTipoAtividade = ideFinalidadeTipoAtividade;
	}
	public CriacaoAnimalFinalidadeTipologiaAtividadePK() {
    	
    }
	public Integer getIdeCriacaoAnimal() {
		return this.ideCriacaoAnimal;
	}
	public void setIdeCriacaoAnimal(Integer ideCriacaoAnimal) {
		this.ideCriacaoAnimal = ideCriacaoAnimal;
	}
	public Integer getIdeFinalidadeTipoAtividade() {
		return this.ideFinalidadeTipoAtividade;
	}
	public void setIdeFinalidadeTipoAtividade(Integer ideFinalidadeTipoAtividade) {
		this.ideFinalidadeTipoAtividade = ideFinalidadeTipoAtividade;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof CriacaoAnimalFinalidadeTipologiaAtividadePK)) {
			return false;
		}
		CriacaoAnimalFinalidadeTipologiaAtividadePK castOther = (CriacaoAnimalFinalidadeTipologiaAtividadePK)other;
		return 
			this.ideCriacaoAnimal.equals(castOther.ideCriacaoAnimal)
			&& this.ideFinalidadeTipoAtividade.equals(castOther.ideFinalidadeTipoAtividade);

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.ideCriacaoAnimal.hashCode();
		hash = hash * prime + this.ideFinalidadeTipoAtividade.hashCode();
		
		return hash;
    }
}