package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "lac_legislacao")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "LacLegislacao.findAll", query = "SELECT l FROM LacLegislacao l"),
		@NamedQuery(name = "LacLegislacao.findByIdeLac", query = "SELECT l FROM LacLegislacao l WHERE l.lacLegislacaoPK.ideLac = :ideLac"),
		@NamedQuery(name = "LacLegislacao.findByIdeRequerimento", query = "SELECT l FROM LacLegislacao l WHERE l.lac.ideRequerimento.ideRequerimento = :ideRequerimento"),
		@NamedQuery(name = "LacLegislacao.findByIdeLegislacao", query = "SELECT l FROM LacLegislacao l WHERE l.lacLegislacaoPK.ideLegislacao = :ideLegislacao"),
		@NamedQuery(name = "LacLegislacao.findByIndAceitacao", query = "SELECT l FROM LacLegislacao l WHERE l.indAceitacao = :indAceitacao") })
public class LacLegislacao implements Serializable {
	private static final long serialVersionUID = 1L;
	@EmbeddedId
	protected LacLegislacaoPK lacLegislacaoPK;
	@Basic(optional = false)
	@NotNull
	@Column(name = "ind_aceitacao", nullable = false)
	private Boolean indAceitacao;
	@JoinColumn(name = "ide_legislacao", referencedColumnName = "ide_legislacao", nullable = false, insertable = false, updatable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Legislacao legislacao;
	@JoinColumn(name = "ide_lac", referencedColumnName = "ide_lac", nullable = false, insertable = false, updatable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Lac lac;

	public LacLegislacao() {
	}

	public LacLegislacao(LacLegislacaoPK lacLegislacaoPK) {
		this.lacLegislacaoPK = lacLegislacaoPK;
	}

	public LacLegislacao(LacLegislacaoPK lacLegislacaoPK, Boolean indAceitacao) {
		this.lacLegislacaoPK = lacLegislacaoPK;
		this.indAceitacao = indAceitacao;
	}

	public LacLegislacao(int ideLac, int ideLegislacao) {
		this.lacLegislacaoPK = new LacLegislacaoPK(ideLac, ideLegislacao);
	}

	public LacLegislacaoPK getLacLegislacaoPK() {
		return lacLegislacaoPK;
	}

	public void setLacLegislacaoPK(LacLegislacaoPK lacErbLegislacaoPK) {
		this.lacLegislacaoPK = lacErbLegislacaoPK;
	}

	public Boolean getIndAceitacao() {
		return indAceitacao;
	}

	public void setIndAceitacao(Boolean indAceitacao) {
		this.indAceitacao = indAceitacao;
	}

	public Legislacao getLegislacao() {
		return legislacao;
	}

	public void setLegislacao(Legislacao legislacao) {
		this.legislacao = legislacao;
	}

	public Lac getLac() {
		return lac;
	}

	public void setLac(Lac lac) {
		this.lac = lac;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((legislacao == null) ? 0 : legislacao.hashCode());
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
		LacLegislacao other = (LacLegislacao) obj;
		if (legislacao == null) {
			if (other.legislacao != null)
				return false;
		} else if (!legislacao.equals(other.legislacao))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "br.gov.ba.seia.entity.LacErbLegislacao[ lacErbLegislacaoPK=" + lacLegislacaoPK + " ]";
	}

}
