package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * 
 * @author luis
 */
@Entity
@Table(name = "tipo_bandeira_posto_combustivel")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "TipoBandeiraPostoCombustivel.findAll", query = "SELECT t FROM TipoBandeiraPostoCombustivel t"),
		@NamedQuery(name = "TipoBandeiraPostoCombustivel.findByIdeTipoBandeiraPostoCombustivel", query = "SELECT t FROM TipoBandeiraPostoCombustivel t WHERE t.ideTipoBandeiraPostoCombustivel = :ideTipoBandeiraPostoCombustivel"),
		@NamedQuery(name = "TipoBandeiraPostoCombustivel.findByDscTipoBandeiraPostoCombustivel", query = "SELECT t FROM TipoBandeiraPostoCombustivel t WHERE t.dscTipoBandeiraPostoCombustivel = :dscTipoBandeiraPostoCombustivel") })
public class TipoBandeiraPostoCombustivel implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@NotNull
	@Column(name = "ide_tipo_bandeira_posto_combustivel")
	private Integer ideTipoBandeiraPostoCombustivel;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 50)
	@Column(name = "dsc_tipo_bandeira_posto_combustivel")
	private String dscTipoBandeiraPostoCombustivel;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "ideTipoBandeiraPostoCombustivel")
	private Collection<LacPostoCombustivel> postoCombustivelCollection;

	public TipoBandeiraPostoCombustivel() {
	}

	public TipoBandeiraPostoCombustivel(Integer ideTipoBandeiraPostoCombustivel) {
		this.ideTipoBandeiraPostoCombustivel = ideTipoBandeiraPostoCombustivel;
	}

	public TipoBandeiraPostoCombustivel(Integer ideTipoBandeiraPostoCombustivel, String dscTipoBandeiraPostoCombustivel) {
		this.ideTipoBandeiraPostoCombustivel = ideTipoBandeiraPostoCombustivel;
		this.dscTipoBandeiraPostoCombustivel = dscTipoBandeiraPostoCombustivel;
	}

	public Integer getIdeTipoBandeiraPostoCombustivel() {
		return ideTipoBandeiraPostoCombustivel;
	}

	public void setIdeTipoBandeiraPostoCombustivel(Integer ideTipoBandeiraPostoCombustivel) {
		this.ideTipoBandeiraPostoCombustivel = ideTipoBandeiraPostoCombustivel;
	}

	public String getDscTipoBandeiraPostoCombustivel() {
		return dscTipoBandeiraPostoCombustivel;
	}

	public void setDscTipoBandeiraPostoCombustivel(String dscTipoBandeiraPostoCombustivel) {
		this.dscTipoBandeiraPostoCombustivel = dscTipoBandeiraPostoCombustivel;
	}

	@XmlTransient
	public Collection<LacPostoCombustivel> getPostoCombustivelCollection() {
		return postoCombustivelCollection;
	}

	public void setPostoCombustivelCollection(Collection<LacPostoCombustivel> postoCombustivelCollection) {
		this.postoCombustivelCollection = postoCombustivelCollection;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (ideTipoBandeiraPostoCombustivel != null ? ideTipoBandeiraPostoCombustivel.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		
		
		if (!(object instanceof TipoBandeiraPostoCombustivel)) {
			return false;
		}
		TipoBandeiraPostoCombustivel other = (TipoBandeiraPostoCombustivel) object;
		if ((this.ideTipoBandeiraPostoCombustivel == null && other.ideTipoBandeiraPostoCombustivel != null)
				|| (this.ideTipoBandeiraPostoCombustivel != null && !this.ideTipoBandeiraPostoCombustivel.equals(other.ideTipoBandeiraPostoCombustivel))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return String.valueOf(ideTipoBandeiraPostoCombustivel);
	}

}
