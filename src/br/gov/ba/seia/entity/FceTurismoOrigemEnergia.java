package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * Entidade que representa os Suprimento de Energia ({@link TipoOrigemEnergia}) presentes no {@link FceTurismo}.
 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
 * @since 04/12/2014
 */
@Entity
@Table(name="fce_turismo_origem_energia")
@NamedQuery(name = "FceTurismoOrigemEnergia.removeByIdeFceTurismo", query = "DELETE FROM FceTurismoOrigemEnergia f WHERE f.ideFceTurismo.ideFceTurismo = :ideFceTurismo")
public class FceTurismoOrigemEnergia implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private FceTurismoOrigemEnergiaPK ideFceTurismoOrigemEnergiaPK;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_fce_turismo", nullable = false, insertable = false, updatable = false)
	private FceTurismo ideFceTurismo;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_tipo_origem_energia", nullable = false, insertable = false, updatable = false)
	private TipoOrigemEnergia ideTipoOrigemEnergia;

	public FceTurismoOrigemEnergia() {
	}
	/**
	 * @param ideFceTurismo
	 * @param ideTipoOrigemEnergia
	 */
	public FceTurismoOrigemEnergia(FceTurismo ideFceTurismo, TipoOrigemEnergia ideTipoOrigemEnergia) {
		this.ideFceTurismoOrigemEnergiaPK = new FceTurismoOrigemEnergiaPK(ideFceTurismo, ideTipoOrigemEnergia);
		this.ideFceTurismo = ideFceTurismo;
		this.ideTipoOrigemEnergia = ideTipoOrigemEnergia;
	}

	/**
	 * @param ideFceTurismoOrigemEnergiaPK
	 */
	public FceTurismoOrigemEnergia(FceTurismoOrigemEnergiaPK ideFceTurismoOrigemEnergiaPK) {
		this.ideFceTurismoOrigemEnergiaPK = ideFceTurismoOrigemEnergiaPK;
	}

	public FceTurismo getIdeFceTurismo() {
		return ideFceTurismo;
	}

	public void setIdeFceTurismo(FceTurismo ideFceTurismo) {
		this.ideFceTurismo = ideFceTurismo;
	}

	public TipoOrigemEnergia getIdeTipoOrigemEnergia() {
		return ideTipoOrigemEnergia;
	}

	public void setIdeTipoOrigemEnergia(TipoOrigemEnergia ideTipoOrigemEnergia) {
		this.ideTipoOrigemEnergia = ideTipoOrigemEnergia;
	}


	public FceTurismoOrigemEnergiaPK getIdeFceTurismoOrigemEnergiaPK() {
		return ideFceTurismoOrigemEnergiaPK;
	}


	public void setIdeFceTurismoOrigemEnergiaPK(FceTurismoOrigemEnergiaPK ideFceTurismoOrigemEnergiaPK) {
		this.ideFceTurismoOrigemEnergiaPK = ideFceTurismoOrigemEnergiaPK;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideFceTurismoOrigemEnergiaPK == null) ? 0
						: ideFceTurismoOrigemEnergiaPK.hashCode());
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
		FceTurismoOrigemEnergia other = (FceTurismoOrigemEnergia) obj;
		if (ideFceTurismoOrigemEnergiaPK == null) {
			if (other.ideFceTurismoOrigemEnergiaPK != null) {
				return false;
			}
		} else if (!ideFceTurismoOrigemEnergiaPK
				.equals(other.ideFceTurismoOrigemEnergiaPK)) {
			return false;
		}
		return true;
	}

}