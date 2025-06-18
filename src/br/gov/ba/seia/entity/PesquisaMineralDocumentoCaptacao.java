package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.ba.seia.interfaces.BaseEntity;


/**
 * The persistent class for the pesquisa_mineral_documento_captacao database table.
 * 
 */
@Entity
@Table(name="pesquisa_mineral_documento_captacao")
@NamedQuery(name="PesquisaMineralDocumentoCaptacao.findAll", query="SELECT p FROM PesquisaMineralDocumentoCaptacao p")
public class PesquisaMineralDocumentoCaptacao implements Serializable, BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "PESQUISA_MINERAL_DOCUMENTO_CAPTACAO_GENERATOR", sequenceName = "PESQUISA_MINERAL_DOCUMENTO_CAPTACAO_SEQ")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PESQUISA_MINERAL_DOCUMENTO_CAPTACAO_GENERATOR")
	@Column(name="ide_pesquisa_mineral_documento_captacao")
	private Integer idePesquisaMineralDocumentoCaptacao;

	@Column(name="nom_documento")
	private String nomDocumento;

	public PesquisaMineralDocumentoCaptacao() {
	}

	public Integer getIdePesquisaMineralDocumentoCaptacao() {
		return this.idePesquisaMineralDocumentoCaptacao;
	}

	public void setIdePesquisaMineralDocumentoCaptacao(Integer idePesquisaMineralDocumentoCaptacao) {
		this.idePesquisaMineralDocumentoCaptacao = idePesquisaMineralDocumentoCaptacao;
	}

	public String getNomDocumento() {
		return this.nomDocumento;
	}

	public void setNomDocumento(String nomDocumento) {
		this.nomDocumento = nomDocumento;
	}

	/* (non-Javadoc)
	 * @see br.gov.ba.seia.entity.BaseEntity#getId()
	 */
	@Override
	public Long getId() {
		return new Long(this.idePesquisaMineralDocumentoCaptacao);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idePesquisaMineralDocumentoCaptacao == null) ? 0 : idePesquisaMineralDocumentoCaptacao.hashCode());
		result = prime * result + ((nomDocumento == null) ? 0 : nomDocumento.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PesquisaMineralDocumentoCaptacao other = (PesquisaMineralDocumentoCaptacao) obj;
		if (idePesquisaMineralDocumentoCaptacao == null) {
			if (other.idePesquisaMineralDocumentoCaptacao != null)
				return false;
		}
		else if (!idePesquisaMineralDocumentoCaptacao.equals(other.idePesquisaMineralDocumentoCaptacao))
			return false;
		if (nomDocumento == null) {
			if (other.nomDocumento != null)
				return false;
		}
		else if (!nomDocumento.equals(other.nomDocumento))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PesquisaMineralDocumentoCaptacao [nomDocumento=" + nomDocumento + "]";
	}
	
}