package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * The persistent class for the criacao_animal_finalidade_tipo_atividade database table.
 * 
 */
@Entity
@XmlRootElement
@Table(name="criacao_animal_finalidade_tipologia_atividade")
@NamedQuery(name = "CriacaoAnimalFinalidadeTipologiaAtividade.excluirByIdeCriacaoAnimal", query = "DELETE FROM CriacaoAnimalFinalidadeTipologiaAtividade cta WHERE cta.ideCriacaoAnimal.ideCriacaoAnimal = :ideCriacaoAnimal")
public class CriacaoAnimalFinalidadeTipologiaAtividade implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private CriacaoAnimalFinalidadeTipologiaAtividadePK id;

	@NotNull
	@JoinColumn(name = "ide_finalidade_tipologia_atividade", referencedColumnName = "ide_finalidade_tipologia_atividade", nullable = false, insertable = false, updatable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private FinalidadeTipologiaAtividade ideFinalidadeTipologiaAtividade;
	
	@NotNull
	@JoinColumn(name = "ide_criacao_animal", referencedColumnName = "ide_criacao_animal", nullable = false, insertable = false, updatable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private CriacaoAnimal ideCriacaoAnimal;

	
    public CriacaoAnimalFinalidadeTipologiaAtividade() {
    }

	public FinalidadeTipologiaAtividade getIdeFinalidadeTipologiaAtividade() {
		return ideFinalidadeTipologiaAtividade;
	}

	public void setIdeFinalidadeTipologiaAtividade(
			FinalidadeTipologiaAtividade ideFinalidadeTipologiaAtividade) {
		this.ideFinalidadeTipologiaAtividade = ideFinalidadeTipologiaAtividade;
	}


	public CriacaoAnimal getIdeCriacaoAnimal() {
		return ideCriacaoAnimal;
	}

	public void setIdeCriacaoAnimal(CriacaoAnimal ideCriacaoAnimal) {
		this.ideCriacaoAnimal = ideCriacaoAnimal;
	}

	public CriacaoAnimalFinalidadeTipologiaAtividadePK getId() {
		return this.id;
	}
	
	public void setId(CriacaoAnimalFinalidadeTipologiaAtividadePK id) {
		this.id = id;
	}
	
}