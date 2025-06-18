package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.interfaces.BaseEntity;

@Entity
@Table(name = "tipo_alteracao", uniqueConstraints = { @UniqueConstraint(columnNames = { "nom_tipo_alteracao" }) })
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "TipoAlteracao.findAll", query = "SELECT t FROM TipoAlteracao t order by t.nomTipoAlteracao"),
		@NamedQuery(name = "TipoAlteracao.findByIdeTipoAlteracao", query = "SELECT t FROM TipoAlteracao t WHERE t.ideTipoAlteracao = :ideTipoAlteracao"),
		@NamedQuery(name = "TipoAlteracao.findByNomTipoAlteracao", query = "SELECT t FROM TipoAlteracao t WHERE t.nomTipoAlteracao = :nomTipoAlteracao") })
public class TipoAlteracao implements Serializable, BaseEntity, Cloneable {

	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@NotNull
	@Column(name = "ide_tipo_alteracao", nullable = false)
	private Integer ideTipoAlteracao;

	@Basic(optional = false)
	@NotNull
	@Column(name = "nom_tipo_alteracao")
	private String nomTipoAlteracao;
	
	@Basic(optional = true)
	@Column(name = "ind_ativo", nullable = true)
	private Boolean indAtivo;

	public TipoAlteracao() {
	}

	public TipoAlteracao(Integer ideTipoAlteracao) {
		this.ideTipoAlteracao = ideTipoAlteracao;
	}

	public TipoAlteracao(Integer ideTipoAlteracao, String nomTipoAlteracao) {
		this.ideTipoAlteracao = ideTipoAlteracao;
		this.nomTipoAlteracao = nomTipoAlteracao;
	}
	
	public TipoAlteracao(Integer ideTipoAlteracao, String nomTipoAlteracao, boolean indAtivo) {
		this.ideTipoAlteracao = ideTipoAlteracao;
		this.nomTipoAlteracao = nomTipoAlteracao;
		this.indAtivo = indAtivo;
	}

	public Integer getIdeTipoAlteracao() {
		return ideTipoAlteracao;
	}

	public void setIdeTipoAlteracao(Integer ideTipoAlteracao) {
		this.ideTipoAlteracao = ideTipoAlteracao;
	}

	public String getNomTipoAlteracao() {
		return nomTipoAlteracao;
	}

	public void setNomTipoAlteracao(String nomTipoAlteracao) {
		this.nomTipoAlteracao = nomTipoAlteracao;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (ideTipoAlteracao != null ? ideTipoAlteracao.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof TipoAlteracao)) {
			return false;
		}
		TipoAlteracao other = (TipoAlteracao) object;
		if ((this.ideTipoAlteracao == null && other.ideTipoAlteracao != null)
				|| (this.ideTipoAlteracao != null && !this.ideTipoAlteracao.equals(other.ideTipoAlteracao))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "br.gov.ba.seia.entity.TipoAlteracao[ ideTipoAlteracao=" + ideTipoAlteracao + " ]";
	}

	@Override
	public Long getId() {
		return new Long(this.ideTipoAlteracao);
	}

	
	/**
	 * @return the indAtivo
	 */
	public Boolean getIndAtivo() {
	
		return indAtivo;
	}

	
	/**
	 * @param indAtivo the indAtivo to set
	 */
	public void setIndAtivo(Boolean indAtivo) {
	
		this.indAtivo = indAtivo;
	}

	@Override
	public TipoAlteracao clone() throws CloneNotSupportedException {
		return (TipoAlteracao) super.clone();
	}
}
