package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "tipo_canal_erb", uniqueConstraints = { @UniqueConstraint(columnNames = { "ide_tipo_canal_erb" }) })
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "TipoCanalErb.findAll", query = "SELECT t FROM TipoCanalErb t"),
		@NamedQuery(name = "TipoCanalErb.findByIdeTipoCanalErb", query = "SELECT t FROM TipoCanalErb t WHERE t.ideTipoCanalErb = :ideTipoCanalErb"),
		@NamedQuery(name = "TipoCanalErb.findByDscTipoCanalErb", query = "SELECT t FROM TipoCanalErb t WHERE t.dscTipoCanalErb = :dscTipoCanalErb") })
public class TipoCanalErb implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@NotNull
	@Column(name = "ide_tipo_canal_erb", nullable = false)
	private Integer ideTipoCanalErb;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 50)
	@Column(name = "dsc_tipo_canal_erb", nullable = false, length = 50)
	private String dscTipoCanalErb;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoCanalErb", fetch = FetchType.LAZY)
	private Collection<LacErbEquipamento> lacErbEquipamentoCollection;

	public TipoCanalErb() {
	}

	public TipoCanalErb(Integer ideTipoCanalErb) {
		this.ideTipoCanalErb = ideTipoCanalErb;
	}

	public TipoCanalErb(Integer ideTipoCanalErb, String dscTipoCanalErb) {
		this.ideTipoCanalErb = ideTipoCanalErb;
		this.dscTipoCanalErb = dscTipoCanalErb;
	}

	public Integer getIdeTipoCanalErb() {
		return ideTipoCanalErb;
	}

	public void setIdeTipoCanalErb(Integer ideTipoCanalErb) {
		this.ideTipoCanalErb = ideTipoCanalErb;
	}

	public String getDscTipoCanalErb() {
		return dscTipoCanalErb;
	}

	public void setDscTipoCanalErb(String dscTipoCanalErb) {
		this.dscTipoCanalErb = dscTipoCanalErb;
	}

	@XmlTransient
	public Collection<LacErbEquipamento> getLacErbEquipamentoCollection() {
		return lacErbEquipamentoCollection;
	}

	public void setLacErbEquipamentoCollection(Collection<LacErbEquipamento> lacErbEquipamentoCollection) {
		this.lacErbEquipamentoCollection = lacErbEquipamentoCollection;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (ideTipoCanalErb != null ? ideTipoCanalErb.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		
		
		if (!(object instanceof TipoCanalErb)) {
			return false;
		}
		TipoCanalErb other = (TipoCanalErb) object;
		if ((this.ideTipoCanalErb == null && other.ideTipoCanalErb != null)
				|| (this.ideTipoCanalErb != null && !this.ideTipoCanalErb.equals(other.ideTipoCanalErb))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "br.gov.ba.seia.entity.TipoCanalErb[ ideTipoCanalErb=" + ideTipoCanalErb + " ]";
	}

}
