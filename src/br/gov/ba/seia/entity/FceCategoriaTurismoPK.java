package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the fce_turismo_origem_energia database table.
 * 
 */
@Embeddable
public class FceCategoriaTurismoPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="ide_fce_turismo")
	private Integer ideFceTurismo;

	@Column(name="ide_categoria_turismo")
	private Integer ideCategoriaTurismo;

	public FceCategoriaTurismoPK() {
	}
	/**
	 * @param ideFceTurismo
	 * @param ideCategoriaTurismo
	 */
	public FceCategoriaTurismoPK(FceTurismo ideFceTurismo,	CategoriaTurismo ideCategoriaTurismo) {
		this.ideFceTurismo = ideFceTurismo.getIdeFceTurismo();
		this.ideCategoriaTurismo = ideCategoriaTurismo.getIdeCategoriaTurismo();
	}
	public Integer getIdeFceTurismo() {
		return ideFceTurismo;
	}
	public void setIdeFceTurismo(Integer ideFceTurismo) {
		this.ideFceTurismo = ideFceTurismo;
	}
	public Integer getIdeCategoriaTurismo() {
		return ideCategoriaTurismo;
	}
	public void setIdeCategoriaTurismo(Integer ideCategoriaTurismo) {
		this.ideCategoriaTurismo = ideCategoriaTurismo;
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
				+ ((ideCategoriaTurismo == null) ? 0 : ideCategoriaTurismo
						.hashCode());
		result = prime * result
				+ ((ideFceTurismo == null) ? 0 : ideFceTurismo.hashCode());
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
		FceCategoriaTurismoPK other = (FceCategoriaTurismoPK) obj;
		if (ideCategoriaTurismo == null) {
			if (other.ideCategoriaTurismo != null) {
				return false;
			}
		} else if (!ideCategoriaTurismo.equals(other.ideCategoriaTurismo)) {
			return false;
		}
		if (ideFceTurismo == null) {
			if (other.ideFceTurismo != null) {
				return false;
			}
		} else if (!ideFceTurismo.equals(other.ideFceTurismo)) {
			return false;
		}
		return true;
	}

}