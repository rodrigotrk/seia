package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.ba.seia.interfaces.BaseEntity;


/**
 * The persistent class for the classificacao_residuo database table.
 * 
 */
@Entity
@Table(name="classificacao_residuo")
@NamedQuery(name="ClassificacaoResiduo.findAll", query="SELECT c FROM ClassificacaoResiduo c")
public class ClassificacaoResiduo implements Serializable, BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="CLASSIFICACAO_RESIDUO_IDECLASSIFICACAORESIDUO_GENERATOR", sequenceName="CLASSIFICACAO_RESIDUO_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CLASSIFICACAO_RESIDUO_IDECLASSIFICACAORESIDUO_GENERATOR")
	@Column(name="ide_classificacao_residuo")
	private Integer ideClassificacaoResiduo;

	@Column(name="ind_ativo")
	private Boolean indAtivo;

	@Column(name="nom_classificacao_residuo")
	private String nomClassificacaoResiduo;

	//bi-directional many-to-one association to ClassificacaoResiduoCaracterizacaoAmbiental
	@OneToMany(mappedBy="classificacaoResiduo")
	private List<ClassificacaoResiduoCaracterizacaoAmbiental> classificacaoResiduoCaracterizacaoAmbientals;

	public ClassificacaoResiduo() {
	}

	public Integer getIdeClassificacaoResiduo() {
		return this.ideClassificacaoResiduo;
	}

	public void setIdeClassificacaoResiduo(Integer ideClassificacaoResiduo) {
		this.ideClassificacaoResiduo = ideClassificacaoResiduo;
	}

	public Boolean getIndAtivo() {
		return this.indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

	public String getNomClassificacaoResiduo() {
		return this.nomClassificacaoResiduo;
	}

	public void setNomClassificacaoResiduo(String nomClassificacaoResiduo) {
		this.nomClassificacaoResiduo = nomClassificacaoResiduo;
	}

	public List<ClassificacaoResiduoCaracterizacaoAmbiental> getClassificacaoResiduoCaracterizacaoAmbientals() {
		return this.classificacaoResiduoCaracterizacaoAmbientals;
	}

	public void setClassificacaoResiduoCaracterizacaoAmbientals(List<ClassificacaoResiduoCaracterizacaoAmbiental> classificacaoResiduoCaracterizacaoAmbientals) {
		this.classificacaoResiduoCaracterizacaoAmbientals = classificacaoResiduoCaracterizacaoAmbientals;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideClassificacaoResiduo == null) ? 0
						: ideClassificacaoResiduo.hashCode());
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
		ClassificacaoResiduo other = (ClassificacaoResiduo) obj;
		if (ideClassificacaoResiduo == null) {
			if (other.ideClassificacaoResiduo != null)
				return false;
		} else if (!ideClassificacaoResiduo
				.equals(other.ideClassificacaoResiduo))
			return false;
		return true;
	}

	@Override
	public Long getId() {
		
		return new Long(this.ideClassificacaoResiduo);
	}

	
}