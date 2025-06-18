package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.gov.ba.seia.enumerator.CerhStatusEnum;
import br.gov.ba.seia.interfaces.BaseEntity;


@Entity
@Table(name="cerh_status")
public class CerhStatus implements Serializable, BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ide_cerh_status")
	private Integer ideCerhStatus;

	@Column(name="dsc_status")
	private String dscStatus;

	@OneToMany(mappedBy="ideCerhStatus")
	private Collection<Cerh> cerhCollection;

	public CerhStatus() {
	}
	
	public CerhStatus(Integer ideCerhStatus) {
		this.ideCerhStatus = ideCerhStatus;
	}
	
	public CerhStatus(CerhStatusEnum cerhTipoStatusEnum) {
		this.ideCerhStatus = cerhTipoStatusEnum.getId();
	}

	public Integer getIdeCerhStatus() {
		return ideCerhStatus;
	}

	public void setIdeCerhStatus(Integer ideCerhStatus) {
		this.ideCerhStatus = ideCerhStatus;
	}

	public String getDscStatus() {
		return dscStatus;
	}

	public void setDscStatus(String dscStatus) {
		this.dscStatus = dscStatus;
	}

	public Collection<Cerh> getCerhCollection() {
		return cerhCollection;
	}

	public void setCerhCollection(Collection<Cerh> cerhCollection) {
		this.cerhCollection = cerhCollection;
	}
	
	@Override
	public String toString() {
		return String.valueOf(ideCerhStatus);
	}

	@Override
	public Long getId() {
		return Long.valueOf(ideCerhStatus);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideCerhStatus == null) ? 0 : ideCerhStatus.hashCode());
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
		CerhStatus other = (CerhStatus) obj;
		if (ideCerhStatus == null) {
			if (other.ideCerhStatus != null)
				return false;
		} else if (!ideCerhStatus.equals(other.ideCerhStatus))
			return false;
		return true;
	}

}