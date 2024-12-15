package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "legislacao", uniqueConstraints = { @UniqueConstraint(columnNames = { "ide_legislacao" }) })
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "Legislacao.findAll", query = "SELECT l FROM Legislacao l"),
		@NamedQuery(name = "Legislacao.findByIdeLegislacao", query = "SELECT l FROM Legislacao l WHERE l.ideLegislacao = :ideLegislacao"),
		@NamedQuery(name = "Legislacao.findByDscLegislacao", query = "SELECT l FROM Legislacao l WHERE l.dscLegislacao = :dscLegislacao"),
		@NamedQuery(name = "Legislacao.findByCodLegilacao", query = "SELECT l FROM Legislacao l WHERE l.codLegilacao = :codLegilacao"),
		@NamedQuery(name = "Legislacao.findByDtcPublicacaoLei", query = "SELECT l FROM Legislacao l WHERE l.dtcPublicacaoLei = :dtcPublicacaoLei"),
		@NamedQuery(name = "Legislacao.findByDtcCriacao", query = "SELECT l FROM Legislacao l WHERE l.dtcCriacao = :dtcCriacao"),
		@NamedQuery(name = "Legislacao.findByIndExcluido", query = "SELECT l FROM Legislacao l WHERE l.indExcluido = :indExcluido"),
		@NamedQuery(name = "Legislacao.findByLegislacaoTipo", query = "SELECT l FROM Legislacao l WHERE l.ideTipoLegislacao.ideTipoLegislacao = :ideTipoLegislacao AND l.indExcluido = :indExcluido"),
		@NamedQuery(name = "Legislacao.findByDtcExclusao", query = "SELECT l FROM Legislacao l WHERE l.dtcExclusao = :dtcExclusao") })
public class Legislacao implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@NotNull
	@Column(name = "ide_legislacao", nullable = false)
	private Integer ideLegislacao;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 2147483647)
	@Column(name = "dsc_legislacao", nullable = false, length = 2147483647)
	private String dscLegislacao;
	@Size(max = 10)
	@Column(name = "cod_legilacao", length = 10)
	private String codLegilacao;
	@Column(name = "dtc_publicacao_lei")
	@Temporal(TemporalType.DATE)
	private Date dtcPublicacaoLei;
	@Basic(optional = false)
	@NotNull
	@Column(name = "dtc_criacao", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcCriacao;
	@Basic(optional = false)
	@NotNull
	@Column(name = "ind_excluido", nullable = false)
	private boolean indExcluido;
	@Column(name = "dtc_exclusao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcExclusao;
	@JoinTable(name = "requerimento_legislacao", joinColumns = { @JoinColumn(name = "ide_legislacao", referencedColumnName = "ide_legislacao", nullable = false) }, inverseJoinColumns = { @JoinColumn(name = "ide_requerimento", referencedColumnName = "ide_requerimento", nullable = false) })
	@ManyToMany(fetch = FetchType.LAZY)
	private Collection<Requerimento> requerimentoCollection;
	@JoinColumn(name = "ide_tipo_legislacao", referencedColumnName = "ide_tipo_legislacao", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private TipoLegislacao ideTipoLegislacao;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "ideLegislacao", fetch = FetchType.LAZY)
	private Collection<Notificacao> notificacaoCollection;

	@Transient
	private Boolean checado;

	public Legislacao() {
	}

	public Legislacao(Integer ideLegislacao) {
		this.ideLegislacao = ideLegislacao;
	}

	public Legislacao(Integer ideLegislacao, String dscLegislacao, Date dtcCriacao, boolean indExcluido) {
		this.ideLegislacao = ideLegislacao;
		this.dscLegislacao = dscLegislacao;
		this.dtcCriacao = dtcCriacao;
		this.indExcluido = indExcluido;
	}

	public Integer getIdeLegislacao() {
		return ideLegislacao;
	}

	public void setIdeLegislacao(Integer ideLegislacao) {
		this.ideLegislacao = ideLegislacao;
	}

	public String getDscLegislacao() {
		return dscLegislacao;
	}

	public void setDscLegislacao(String dscLegislacao) {
		this.dscLegislacao = dscLegislacao;
	}

	public String getCodLegilacao() {
		return codLegilacao;
	}

	public void setCodLegilacao(String codLegilacao) {
		this.codLegilacao = codLegilacao;
	}

	public Date getDtcPublicacaoLei() {
		return dtcPublicacaoLei;
	}

	public void setDtcPublicacaoLei(Date dtcPublicacaoLei) {
		this.dtcPublicacaoLei = dtcPublicacaoLei;
	}

	public Date getDtcCriacao() {
		return dtcCriacao;
	}

	public void setDtcCriacao(Date dtcCriacao) {
		this.dtcCriacao = dtcCriacao;
	}

	public boolean getIndExcluido() {
		return indExcluido;
	}

	public void setIndExcluido(boolean indExcluido) {
		this.indExcluido = indExcluido;
	}

	public Date getDtcExclusao() {
		return dtcExclusao;
	}

	public void setDtcExclusao(Date dtcExclusao) {
		this.dtcExclusao = dtcExclusao;
	}

	public Boolean getChecado() {
		return checado;
	}

	public void setChecado(Boolean checado) {
		this.checado = checado;
	}

	@XmlTransient
	public Collection<Requerimento> getRequerimentoCollection() {
		return requerimentoCollection;
	}

	public void setRequerimentoCollection(Collection<Requerimento> requerimentoCollection) {
		this.requerimentoCollection = requerimentoCollection;
	}

	public TipoLegislacao getIdeTipoLegislacao() {
		return ideTipoLegislacao;
	}

	public void setIdeTipoLegislacao(TipoLegislacao ideTipoLegislacao) {
		this.ideTipoLegislacao = ideTipoLegislacao;
	}

	@XmlTransient
	public Collection<Notificacao> getNotificacaoCollection() {
		return notificacaoCollection;
	}

	public void setNotificacaoCollection(Collection<Notificacao> notificacaoCollection) {
		this.notificacaoCollection = notificacaoCollection;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codLegilacao == null) ? 0 : codLegilacao.hashCode());
		result = prime * result + ((ideLegislacao == null) ? 0 : ideLegislacao.hashCode());
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
		Legislacao other = (Legislacao) obj;
		if (codLegilacao == null) {
			if (other.codLegilacao != null)
				return false;
		} else if (!codLegilacao.equals(other.codLegilacao))
			return false;
		if (ideLegislacao == null) {
			if (other.ideLegislacao != null)
				return false;
		} else if (!ideLegislacao.equals(other.ideLegislacao))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "br.gov.ba.seia.entity.Legislacao[ ideLegislacao=" + ideLegislacao + " ]";
	}

}
