package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "florestal_ato_ambiental")
@XmlRootElement

@NamedQueries({ @NamedQuery(name = "FlorestalAtoAmbiental.findByIdeFlorestal", query = "SELECT f FROM FlorestalAtoAmbiental f WHERE f.ideFlorestal = :ideFlorestal") ,
			@NamedQuery(name = "FlorestalAtoAmbiental.remover", query = "DELETE FROM  FlorestalAtoAmbiental WHERE ideFlorestal.ideFlorestal = :ideFlorestal"),
			@NamedQuery(name = "FlorestalAtoAmbiental.removerByIdeRequerimento", query = "DELETE FROM  FlorestalAtoAmbiental WHERE ideFlorestal.ideFlorestal in (SELECT fl.ideFlorestal from Florestal fl where fl.ideRequerimento.ideRequerimento = :ideRequerimento)")})
public class FlorestalAtoAmbiental implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	protected FlorestalAtoAmbientalPK florestalAtoAmbientalPK;

	@NotNull
	@JoinColumn(name = "ide_ato_ambiental", referencedColumnName = "ide_ato_ambiental", nullable = false, insertable = false, updatable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private AtoAmbiental ideAtoAmbiental;

	@NotNull
	@JoinColumn(name = "ide_florestal", referencedColumnName = "ide_florestal", nullable = false, insertable = false, updatable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private Florestal ideFlorestal;

	@NotNull
	@Basic(optional = false)
	@Column(name = "num_portaria", nullable = false, length = 50)
	private String numPortaria;

	public FlorestalAtoAmbiental() {

	}

	public FlorestalAtoAmbiental(FlorestalAtoAmbientalPK florestalAtoAmbientalPK) {
		this.florestalAtoAmbientalPK = florestalAtoAmbientalPK;
	}

	public FlorestalAtoAmbiental(int ideAtoAmbiental, int ideFlorestal) {
		this.florestalAtoAmbientalPK = new FlorestalAtoAmbientalPK(ideAtoAmbiental, ideFlorestal);
	}

	public FlorestalAtoAmbientalPK getFlorestalAtoAmbientalPK() {
		return florestalAtoAmbientalPK;
	}

	public void setFlorestalAtoAmbientalPK(FlorestalAtoAmbientalPK florestalAtoAmbientalPK) {
		this.florestalAtoAmbientalPK = florestalAtoAmbientalPK;
	}

	public AtoAmbiental getIdeAtoAmbiental() {
		return ideAtoAmbiental;
	}

	public void setIdeAtoAmbiental(AtoAmbiental ideAtoAmbiental) {
		this.ideAtoAmbiental = ideAtoAmbiental;
	}

	public Florestal getIdeFlorestal() {
		return ideFlorestal;
	}

	public void setIdeFlorestal(Florestal ideFlorestal) {
		this.ideFlorestal = ideFlorestal;
	}

	public String getNumPortaria() {
		return numPortaria;
	}

	public void setNumPortaria(String numPortaria) {
		this.numPortaria = numPortaria;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((florestalAtoAmbientalPK == null) ? 0 : florestalAtoAmbientalPK.hashCode());
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
		FlorestalAtoAmbiental other = (FlorestalAtoAmbiental) obj;
		if (florestalAtoAmbientalPK == null) {
			if (other.florestalAtoAmbientalPK != null)
				return false;
		} else if (!florestalAtoAmbientalPK.equals(other.florestalAtoAmbientalPK))
			return false;
		return true;
	}

}
