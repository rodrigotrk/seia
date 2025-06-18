package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class FlorestalAtoAmbientalPK implements Serializable {

	private static final long serialVersionUID = 1L;

	@Basic(optional = false)
	@NotNull
	@Column(name = "ide_ato_Ambiental", nullable = false)
	private int ideAtoAmbiental;

	@Basic(optional = false)
	@NotNull
	@Column(name = "ide_florestal", nullable = false)
	private int ideFlorestal;

	public FlorestalAtoAmbientalPK() {
	}

	public FlorestalAtoAmbientalPK(int ideAtoAmbiental, int ideFlorestal) {
		this.ideAtoAmbiental = ideAtoAmbiental;
		this.ideFlorestal = ideFlorestal;
	}

	public int getIdeAtoAmbiental() {
		return ideAtoAmbiental;
	}

	public void setIdeAtoAmbiental(int ideAtoAmbiental) {
		this.ideAtoAmbiental = ideAtoAmbiental;
	}

	public int getIdeFlorestal() {
		return ideFlorestal;
	}

	public void setIdeFlorestal(int ideFlorestal) {
		this.ideFlorestal = ideFlorestal;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ideAtoAmbiental;
		result = prime * result + ideFlorestal;
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
		FlorestalAtoAmbientalPK other = (FlorestalAtoAmbientalPK) obj;
		if (ideAtoAmbiental != other.ideAtoAmbiental)
			return false;
		if (ideFlorestal != other.ideFlorestal)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "br.gov.ba.seia.entity.FlorestalAtoAmbientalPK [ideAtoAmbiental=" + ideAtoAmbiental + ", ideFlorestal="
				+ ideFlorestal + "]";
	}
}
