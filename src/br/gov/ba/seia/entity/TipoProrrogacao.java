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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.interfaces.BaseEntity;

@Entity
@Table(name = "tipo_prorrogacao", uniqueConstraints = { @UniqueConstraint(columnNames = { "nom_tipo_prorrogacao" }) })
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "TipoProrrogacao.findAll", query = "SELECT t FROM TipoProrrogacao t order by t.nomTipoProrrogacao"),
		@NamedQuery(name = "TipoProrrogacao.findByIdeTipoProrrogacao", query = "SELECT t FROM TipoProrrogacao t WHERE t.ideTipoProrrogacao = :ideTipoProrrogacao"),
		@NamedQuery(name = "TipoProrrogacao.findByNomTipoProrrogacao", query = "SELECT t FROM TipoProrrogacao t WHERE t.nomTipoProrrogacao = :nomTipoProrrogacao") })
public class TipoProrrogacao implements Serializable, BaseEntity {
	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@NotNull
	@Column(name = "ide_tipo_prorrogacao", nullable = false)
	private Integer ideTipoProrrogacao;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 80)
	@Column(name = "nom_tipo_prorrogacao", nullable = false, length = 80)
	private String nomTipoProrrogacao;

	public TipoProrrogacao() {
	}

	public TipoProrrogacao(Integer ideTipoProrrogacao) {
		this.ideTipoProrrogacao = ideTipoProrrogacao;
	}

	public TipoProrrogacao(Integer ideTipoProrrogacao, String nomTipoProrrogacao) {
		this.ideTipoProrrogacao = ideTipoProrrogacao;
		this.nomTipoProrrogacao = nomTipoProrrogacao;
	}

	public Integer getIdeTipoProrrogacao() {
		return ideTipoProrrogacao;
	}

	public void setIdeTipoProrrogacao(Integer ideTipoProrrogacao) {
		this.ideTipoProrrogacao = ideTipoProrrogacao;
	}

	public String getNomTipoProrrogacao() {
		return nomTipoProrrogacao;
	}

	public void setNomTipoProrrogacao(String nomTipoProrrogacao) {
		this.nomTipoProrrogacao = nomTipoProrrogacao;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (ideTipoProrrogacao != null ? ideTipoProrrogacao.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		
		
		if (!(object instanceof TipoProrrogacao)) {
			return false;
		}
		TipoProrrogacao other = (TipoProrrogacao) object;
		if ((this.ideTipoProrrogacao == null && other.ideTipoProrrogacao != null)
				|| (this.ideTipoProrrogacao != null && !this.ideTipoProrrogacao.equals(other.ideTipoProrrogacao))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "br.gov.ba.seia.entity.TipoProrrogacao[ ideTipoProrrogacao=" + ideTipoProrrogacao + " ]";
	}

	@Override
	public Long getId() {
		return new Long(this.ideTipoProrrogacao);
	}

}
