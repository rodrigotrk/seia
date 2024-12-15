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
 * Entitade criada para representar a tabela <i>fce_catergoria_turismo</i>, associativa para {@link FceTurismo} e {@link CategoriaTurismo}.
 */
@Entity
@Table(name="fce_categoria_turismo")
@NamedQuery(name = "FceCategoriaTurismo.removeByIdeFceTurismo", query = "DELETE FROM FceCategoriaTurismo f WHERE f.ideFceTurismo.ideFceTurismo = :ideFceTurismo")
public class FceCategoriaTurismo implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private FceCategoriaTurismoPK ideFceCategoriaTurismoPK;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_fce_turismo", nullable = false, insertable = false, updatable = false)
	private FceTurismo ideFceTurismo;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_categoria_turismo", nullable = false, insertable = false, updatable = false)
	private CategoriaTurismo ideCategoriaTurismo;

	public FceCategoriaTurismo() {
	}

	/**
	 * @param ideFceCategoriaTurismoPK
	 */
	public FceCategoriaTurismo(FceCategoriaTurismoPK ideFceCategoriaTurismoPK) {
		this.ideFceCategoriaTurismoPK = ideFceCategoriaTurismoPK;
	}

	/**
	 * @param ideFceTurismo
	 * @param ideCategoriaTurismo
	 */
	public FceCategoriaTurismo(FceTurismo ideFceTurismo, CategoriaTurismo ideCategoriaTurismo) {
		this.ideFceCategoriaTurismoPK = new FceCategoriaTurismoPK(ideFceTurismo, ideCategoriaTurismo);
	}

	public FceCategoriaTurismoPK getIdeFceCategoriaTurismoPK() {
		return ideFceCategoriaTurismoPK;
	}

	public void setIdeFceCategoriaTurismoPK(
			FceCategoriaTurismoPK ideFceCategoriaTurismoPK) {
		this.ideFceCategoriaTurismoPK = ideFceCategoriaTurismoPK;
	}

	public FceTurismo getIdeFceTurismo() {
		return ideFceTurismo;
	}

	public void setIdeFceTurismo(FceTurismo ideFceTurismo) {
		this.ideFceTurismo = ideFceTurismo;
	}

	public CategoriaTurismo getIdeCategoriaTurismo() {
		return ideCategoriaTurismo;
	}

	public void setIdeCategoriaTurismo(CategoriaTurismo ideCategoriaTurismo) {
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
				+ ((ideFceCategoriaTurismoPK == null) ? 0
						: ideFceCategoriaTurismoPK.hashCode());
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
		FceCategoriaTurismo other = (FceCategoriaTurismo) obj;
		if (ideFceCategoriaTurismoPK == null) {
			if (other.ideFceCategoriaTurismoPK != null) {
				return false;
			}
		} else if (!ideFceCategoriaTurismoPK
				.equals(other.ideFceCategoriaTurismoPK)) {
			return false;
		}
		return true;
	}
}