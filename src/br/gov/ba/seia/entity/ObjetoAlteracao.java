package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * The persistent class for the objeto_alteracao database table.
 * 
 */
@Entity
@Table(name = "objeto_alteracao")
public class ObjetoAlteracao implements Serializable, Comparable<ObjetoAlteracao> {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "objeto_alteracao_ide_objeto_alteracao_seq")
	@SequenceGenerator(name = "objeto_alteracao_ide_objeto_alteracao_seq", sequenceName = "objeto_alteracao_ide_objeto_alteracao_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name = "ide_objeto_alteracao", nullable = false)
	private Integer ideObjetoAlteracao;

	@Basic(optional = false)
	@Column(name = "ind_ativo")
	private Boolean indAtivo;

	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 150)
	@Column(name = "nom_objeto_alteracao")
	private String nomObjetoAlteracao;

	@JoinTable(name = "licenca_objeto_alteracao", joinColumns = { @JoinColumn(name = "ide_objeto_alteracao", referencedColumnName = "ide_objeto_alteracao", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "ide_licenca", referencedColumnName = "ide_licenca", nullable = false, updatable = false) })
	@ManyToMany(fetch = FetchType.LAZY)
	private Collection<Licenca> licencaCollection;

	@Transient
	private boolean rowSelect = false;

	public ObjetoAlteracao() {
	}

	public Integer getIdeObjetoAlteracao() {
		return this.ideObjetoAlteracao;
	}

	public void setIdeObjetoAlteracao(Integer ideObjetoAlteracao) {
		this.ideObjetoAlteracao = ideObjetoAlteracao;
	}

	public Boolean getIndAtivo() {
		return this.indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

	public String getNomObjetoAlteracao() {
		return this.nomObjetoAlteracao;
	}

	public void setNomObjetoAlteracao(String nomObjetoAlteracao) {
		this.nomObjetoAlteracao = nomObjetoAlteracao;
	}

	public boolean isRowSelect() {
		return rowSelect;
	}

	public void setRowSelect(boolean rowSelect) {
		this.rowSelect = rowSelect;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideObjetoAlteracao == null) ? 0 : ideObjetoAlteracao.hashCode());
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
		ObjetoAlteracao other = (ObjetoAlteracao) obj;
		if (ideObjetoAlteracao == null) {
			if (other.ideObjetoAlteracao != null)
				return false;
		} else if (!ideObjetoAlteracao.equals(other.ideObjetoAlteracao))
			return false;
		return true;
	}

	public Collection<Licenca> getLicencaCollection() {
		return licencaCollection;
	}

	public void setLicencaCollection(Collection<Licenca> licencaCollection) {
		this.licencaCollection = licencaCollection;
	}

	
	@Override
	public int compareTo(ObjetoAlteracao o) {
		return this.getNomObjetoAlteracao().compareTo(o.nomObjetoAlteracao);
		
	}

}