package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.interfaces.BaseEntity;


/**
 * The persistent class for the finalidade database table.
 * 
 */
@Entity
@XmlRootElement
@Table(name = "finalidade")
@NamedQueries({
@NamedQuery(name = "Finalidade.findByTipologiaAtividade", query = "SELECT f FROM FinalidadeTipologiaAtividade fta INNER JOIN fta.ideFinalidade f WHERE fta.ideTipologiaAtividade.ideTipologiaAtividade = :ideTipologiaAtividade ORDER BY f.dscAtividade ASC"),
@NamedQuery(name = "Finalidade.findFinalidadesByIdeCriacaoAnimal", query = "SELECT f FROM CriacaoAnimalFinalidadeTipologiaAtividade cta INNER JOIN cta.ideFinalidadeTipologiaAtividade fta INNER JOIN fta.ideFinalidade f WHERE cta.ideCriacaoAnimal.ideCriacaoAnimal = :ideCriacaoAnimal")
})
public class Finalidade implements Serializable,BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "finalidade_ide_finalidade_generator")
	@SequenceGenerator(name = "finalidade_ide_finalidade_generator", sequenceName = "finalidade_ide_finalidade_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name="ide_finalidade")
	private Integer ideFinalidade;

	@Size(min = 1, max = 20)
	@Column(name="dsc_atividade",nullable = false, length = 50)
	private String dscAtividade;

	@Column(name="ind_ativo")
	private Boolean indAtivo;

    public Finalidade(String dscAtividade) {
		this.dscAtividade = dscAtividade;
	}
    
	public Finalidade() {
    }
	
	public Finalidade(Integer ideFinalidade, String dscAtividade) {
		this.ideFinalidade = ideFinalidade;
		this.dscAtividade = dscAtividade;
	}

	public Finalidade(Integer ideFinalidade) {
		this.ideFinalidade = ideFinalidade;
	}

	public Integer getIdeFinalidade() {
		return this.ideFinalidade;
	}

	public void setIdeFinalidade(Integer ideFinalidade) {
		this.ideFinalidade = ideFinalidade;
	}

	public String getDscAtividade() {
		return this.dscAtividade;
	}

	public void setDscAtividade(String dscAtividade) {
		this.dscAtividade = dscAtividade;
	}

	public Boolean getIndAtivo() {
		return this.indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ideFinalidade == null) ? 0 : ideFinalidade.hashCode());
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
		Finalidade other = (Finalidade) obj;
		if (ideFinalidade == null) {
			if (other.ideFinalidade != null)
				return false;
		} else if (!ideFinalidade.equals(other.ideFinalidade))
			return false;
		return true;
	}
	@Override
	public Long getId() {
		return new Long(this.ideFinalidade);
	}

}