package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import br.gov.ba.seia.interfaces.BaseEntity;

/**
 * 
 * @author Eduardo Fernandes
 */
@Entity
@Table(name = "tipo_area_protegida")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "TipoAreaProtegida.findAll", query = "SELECT t FROM TipoAreaProtegida t"),
		@NamedQuery(name = "TipoAreaProtegida.findByIdeRequerimento", query = "SELECT t FROM TipoAreaProtegida t INNER JOIN t.requerimentoCollection l WHERE l.ideRequerimento = :ideRequerimento"),
		@NamedQuery(name = "TipoAreaProtegida.findByideTipoAreaProtegida", query = "SELECT t FROM TipoAreaProtegida t WHERE t.ideTipoAreaProtegida = :ideTipoAreaProtegida"),
		@NamedQuery(name = "TipoAreaProtegida.findByNomTipoAreaProtegida", query = "SELECT t FROM TipoAreaProtegida t WHERE t.nomTipoAreaProtegida = :nomTipoAreaProtegida"),
		@NamedQuery(name = "TipoAreaProtegida.findByIndAtivo", query = "SELECT t FROM TipoAreaProtegida t WHERE t.indAtivo = :indAtivo") })
public class TipoAreaProtegida implements Serializable, BaseEntity {
	private static final long serialVersionUID = 1L;
	@Id
	@NotNull
	@Column(name = "ide_tipo_area_protegida", nullable = false)
	private Integer ideTipoAreaProtegida;

	@NotNull
	@Column(name = "nom_tipo_area_protegida", nullable = false, length = 50)
	private String nomTipoAreaProtegida;

	@Column(name = "ind_ativo")
	private Boolean indAtivo;

	@ManyToMany(mappedBy = "tipoAreaProtegidaCollection", fetch = FetchType.LAZY)
	private Collection<Requerimento> requerimentoCollection;

	public TipoAreaProtegida() {

	}

	public TipoAreaProtegida(Integer ideTipoArea) {
		this.ideTipoAreaProtegida = ideTipoArea;
	}

	public Integer getIdeTipoAreaProtegida() {
		return ideTipoAreaProtegida;
	}

	public void setIdeTipoAreaProtegida(Integer ideTipoAreaProtegida) {
		this.ideTipoAreaProtegida = ideTipoAreaProtegida;
	}

	public String getNomTipoAreaProtegida() {
		return nomTipoAreaProtegida;
	}

	public void setNomTipoAreaProtegida(String dscTipoAreaProtegida) {
		this.nomTipoAreaProtegida = dscTipoAreaProtegida;
	}

	public Boolean getIndAtivo() {
		return indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

	@XmlTransient
	public Collection<Requerimento> getRequerimentoCollection() {
		return requerimentoCollection;
	}

	public void setRequerimentoCollection(Collection<Requerimento> requerimentoCollection) {
		this.requerimentoCollection = requerimentoCollection;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (ideTipoAreaProtegida != null ? ideTipoAreaProtegida.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof TipoAreaProtegida)) {
			return false;
		}
		TipoAreaProtegida other = (TipoAreaProtegida) object;
		if ((this.ideTipoAreaProtegida == null && other.ideTipoAreaProtegida != null)
				|| (this.ideTipoAreaProtegida != null && !this.ideTipoAreaProtegida.equals(other.ideTipoAreaProtegida))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "br.gov.ba.seia.entity.TipoAto[ ideTipoAreaProtegida=" + ideTipoAreaProtegida + " ]";
	}

	@Override
	public Long getId() {
		return new Long(this.ideTipoAreaProtegida);
	}

}
