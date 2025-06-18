package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * The persistent class for the unidade_tipologia_atividade database table.
 * 
 */
@Entity
@XmlRootElement
@Table(name="unidade_tipologia_atividade")
public class UnidadeTipologiaAtividade implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "unidade_tipologia_atividade_ide_unidade_tipologia_atividade_generator")
	@SequenceGenerator(name = "unidade_tipologia_atividade_ide_unidade_tipologia_atividade_generator", sequenceName = "unidade_tipologia_atividade_ide_unidade_tipologia_atividade_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name="ide_unidade_tipologia_atividade")
	private Integer ideUnidadeTipologiaAtividade;

	@JoinColumn(name = "ide_tipologia_atividade", referencedColumnName = "ide_tipologia_atividade", nullable = true)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private TipologiaAtividade ideTipologiaAtividade;

	@JoinColumn(name = "ide_unidade_medida", referencedColumnName = "ide_unidade_medida", nullable = true)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private UnidadeMedida ideUnidadeMedida;
	
    public UnidadeTipologiaAtividade() {
    }

	public UnidadeTipologiaAtividade(TipologiaAtividade ideTipologiaAtividade) {
		this.ideTipologiaAtividade = ideTipologiaAtividade;
	}

	public Integer getIdeUnidadeTipologiaAtividade() {
		return this.ideUnidadeTipologiaAtividade;
	}

	public void setIdeUnidadeTipologiaAtividade(Integer ideUnidadeTipologiaAtividade) {
		this.ideUnidadeTipologiaAtividade = ideUnidadeTipologiaAtividade;
	}

	public TipologiaAtividade getIdeTipologiaAtividade() {
		return ideTipologiaAtividade;
	}

	public void setIdeTipologiaAtividade(TipologiaAtividade ideTipologiaAtividade) {
		this.ideTipologiaAtividade = ideTipologiaAtividade;
	}

	public UnidadeMedida getIdeUnidadeMedida() {
		return ideUnidadeMedida;
	}

	public void setIdeUnidadeMedida(UnidadeMedida ideUnidadeMedida) {
		this.ideUnidadeMedida = ideUnidadeMedida;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideTipologiaAtividade == null) ? 0 : ideTipologiaAtividade
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UnidadeTipologiaAtividade other = (UnidadeTipologiaAtividade) obj;
		if (ideTipologiaAtividade == null) {
			if (other.ideTipologiaAtividade != null)
				return false;
		} else if (!ideTipologiaAtividade.equals(other.ideTipologiaAtividade))
			return false;
		return true;
	}
	
	

}