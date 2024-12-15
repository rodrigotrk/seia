package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the fce_turismo_origem_energia database table.
 * 
 */
@Embeddable
public class FceTurismoOrigemEnergiaPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="ide_fce_turismo")
	private Integer ideFceTurismo;

	@Column(name="ide_tipo_origem_energia")
	private Integer ideTipoOrigemEnergia;

	public FceTurismoOrigemEnergiaPK() {
	}

	/**
	 * @param ideFceTurismo
	 * @param ideTipoOrigemEnergia
	 */
	public FceTurismoOrigemEnergiaPK(FceTurismo fceTurismo, TipoOrigemEnergia tipoOrigemEnergia) {
		this.ideFceTurismo = fceTurismo.getIdeFceTurismo();
		this.ideTipoOrigemEnergia = tipoOrigemEnergia.getIdeTipoOrigemEnergia();
	}

	public Integer getIdeFceTurismo() {
		return this.ideFceTurismo;
	}
	public void setIdeFceTurismo(Integer ideFceTurismo) {
		this.ideFceTurismo = ideFceTurismo;
	}
	public Integer getIdeTipoOrigemEnergia() {
		return this.ideTipoOrigemEnergia;
	}
	public void setIdeTipoOrigemEnergia(Integer ideTipoOrigemEnergia) {
		this.ideTipoOrigemEnergia = ideTipoOrigemEnergia;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ideFceTurismo == null) ? 0 : ideFceTurismo.hashCode());
		result = prime
				* result
				+ ((ideTipoOrigemEnergia == null) ? 0 : ideTipoOrigemEnergia
						.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		FceTurismoOrigemEnergiaPK other = (FceTurismoOrigemEnergiaPK) obj;
		if (ideFceTurismo == null) {
			if (other.ideFceTurismo != null) {
				return false;
			}
		} else if (!ideFceTurismo.equals(other.ideFceTurismo)) {
			return false;
		}
		if (ideTipoOrigemEnergia == null) {
			if (other.ideTipoOrigemEnergia != null) {
				return false;
			}
		} else if (!ideTipoOrigemEnergia.equals(other.ideTipoOrigemEnergia)) {
			return false;
		}
		return true;
	}
}