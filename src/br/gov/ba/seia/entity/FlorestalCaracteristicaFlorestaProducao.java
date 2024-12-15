package br.gov.ba.seia.entity;

import java.io.Serializable;

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
@Table(name = "florestal_caracteristica_floresta_producao")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "FlorestalCaracteristicaFlorestaProducao.findByIdeFlorestal", query = "SELECT f FROM FlorestalCaracteristicaFlorestaProducao f WHERE f.ideFlorestal = :ideFlorestal"),
	@NamedQuery(name = "FlorestalCaracteristicaFlorestaProducao.remover", query = "DELETE FROM FlorestalCaracteristicaFlorestaProducao f WHERE f.ideFlorestal.ideFlorestal = :ideFlorestal"),
	@NamedQuery(name = "FlorestalCaracteristicaFlorestaProducao.removerByIdeRequerimento", query = "DELETE FROM FlorestalCaracteristicaFlorestaProducao f WHERE f.ideFlorestal.ideFlorestal in (SELECT fl.ideFlorestal from Florestal fl where fl.ideRequerimento.ideRequerimento = :ideRequerimento)")})
public class FlorestalCaracteristicaFlorestaProducao implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	protected FlorestalCaracteristicaFlorestaProducaoPK florestalCaracteristicaFlorestaProducaoPK;

	@NotNull
	@JoinColumn(name = "ide_caracteristica_floresta_producao", referencedColumnName = "ide_caracteristica_floresta_producao", nullable = false, insertable = false, updatable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private CaracteristicaFlorestaProducao ideCaracteristicaFlorestaProducao;

	@NotNull
	@JoinColumn(name = "ide_florestal", referencedColumnName = "ide_florestal", nullable = false, insertable = false, updatable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private Florestal ideFlorestal;

	public FlorestalCaracteristicaFlorestaProducao() {

	}

	public FlorestalCaracteristicaFlorestaProducao(
			FlorestalCaracteristicaFlorestaProducaoPK florestalCaracteristicaFlorestaProducaoPK) {
		this.florestalCaracteristicaFlorestaProducaoPK = florestalCaracteristicaFlorestaProducaoPK;
	}

	public FlorestalCaracteristicaFlorestaProducao(CaracteristicaFlorestaProducao caracteristicaFlorestaProducao,
			Florestal florestal) {
		this.ideCaracteristicaFlorestaProducao = caracteristicaFlorestaProducao;
		this.ideFlorestal = florestal;
	}

	public FlorestalCaracteristicaFlorestaProducaoPK getFlorestalCaracteristicaFlorestaProducaoPK() {
		return florestalCaracteristicaFlorestaProducaoPK;
	}

	public void setFlorestalCaracteristicaFlorestaProducaoPK(
			FlorestalCaracteristicaFlorestaProducaoPK florestalCaracteristicaFlorestaProducaoPK) {
		this.florestalCaracteristicaFlorestaProducaoPK = florestalCaracteristicaFlorestaProducaoPK;
	}

	public CaracteristicaFlorestaProducao getIdeCaracteristicaFlorestaProducao() {
		return ideCaracteristicaFlorestaProducao;
	}

	public void setIdeCaracteristicaFlorestaProducao(CaracteristicaFlorestaProducao ideCaracteristicaFlorestaProducao) {
		this.ideCaracteristicaFlorestaProducao = ideCaracteristicaFlorestaProducao;
	}

	public Florestal getIdeFlorestal() {
		return ideFlorestal;
	}

	public void setIdeFlorestal(Florestal ideFlorestal) {
		this.ideFlorestal = ideFlorestal;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((florestalCaracteristicaFlorestaProducaoPK == null) ? 0 : florestalCaracteristicaFlorestaProducaoPK
						.hashCode());
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
		FlorestalCaracteristicaFlorestaProducao other = (FlorestalCaracteristicaFlorestaProducao) obj;
		if (florestalCaracteristicaFlorestaProducaoPK == null) {
			if (other.florestalCaracteristicaFlorestaProducaoPK != null)
				return false;
		} else if (!florestalCaracteristicaFlorestaProducaoPK.equals(other.florestalCaracteristicaFlorestaProducaoPK))
			return false;
		return true;
	}
}
