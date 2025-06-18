package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author luis
 */
@Entity
@Table(name = "tipo_servico_posto")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "TipoServicoPosto.findAll", query = "SELECT t FROM TipoServicoPosto t"),
		@NamedQuery(name = "TipoServicoPosto.findByIdeTipoServicoPosto", query = "SELECT t FROM TipoServicoPosto t WHERE t.ideTipoServicoPosto = :ideTipoServicoPosto"),
		@NamedQuery(name = "TipoServicoPosto.findByDscTipoServicoPosto", query = "SELECT t FROM TipoServicoPosto t WHERE t.dscTipoServicoPosto = :dscTipoServicoPosto") })
public class TipoServicoPosto implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@NotNull
	@Column(name = "ide_tipo_servico_posto")
	private Integer ideTipoServicoPosto;

	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 50)
	@Column(name = "dsc_tipo_servico_posto")
	private String dscTipoServicoPosto;

	public TipoServicoPosto() {
	}

	public TipoServicoPosto(Integer ideTipoServicoPosto) {
		this.ideTipoServicoPosto = ideTipoServicoPosto;
	}

	public TipoServicoPosto(Integer ideTipoServicoPosto, String dscTipoServicoPosto) {
		this.ideTipoServicoPosto = ideTipoServicoPosto;
		this.dscTipoServicoPosto = dscTipoServicoPosto;
	}

	public Integer getIdeTipoServicoPosto() {
		return ideTipoServicoPosto;
	}

	public void setIdeTipoServicoPosto(Integer ideTipoServicoPosto) {
		this.ideTipoServicoPosto = ideTipoServicoPosto;
	}

	public String getDscTipoServicoPosto() {
		return dscTipoServicoPosto;
	}

	public void setDscTipoServicoPosto(String dscTipoServicoPosto) {
		this.dscTipoServicoPosto = dscTipoServicoPosto;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (ideTipoServicoPosto != null ? ideTipoServicoPosto.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		
		
		if (!(object instanceof TipoServicoPosto)) {
			return false;
		}
		TipoServicoPosto other = (TipoServicoPosto) object;
		if ((this.ideTipoServicoPosto == null && other.ideTipoServicoPosto != null)
				|| (this.ideTipoServicoPosto != null && !this.ideTipoServicoPosto.equals(other.ideTipoServicoPosto))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
    	return String.valueOf(ideTipoServicoPosto);
	}

}
