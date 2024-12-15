package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class FlorestalCaracteristicaFlorestaProducaoPK implements Serializable {

	private static final long serialVersionUID = 1L;

	@Basic(optional = false)
	@NotNull
	@Column(name = "ide_caracteristica_floresta_producao", nullable = false)
	private int ideCaracteristicaFlorestaProducao;

	@Basic(optional = false)
	@NotNull
	@Column(name = "ide_florestal", nullable = false)
	private int ideFlorestal;

	public FlorestalCaracteristicaFlorestaProducaoPK() {

	}

	public FlorestalCaracteristicaFlorestaProducaoPK(int ideCaracteristicaFlorestaProducao, int ideFlorestal) {
		this.ideCaracteristicaFlorestaProducao = ideCaracteristicaFlorestaProducao;
		this.ideFlorestal = ideFlorestal;
	}

	public int getIdeCaracteristicaFlorestaProducao() {
		return ideCaracteristicaFlorestaProducao;
	}

	public void setIdeCaracteristicaFlorestaProducao(int ideCaracteristicaFlorestaProducao) {
		this.ideCaracteristicaFlorestaProducao = ideCaracteristicaFlorestaProducao;
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
		result = prime * result + ideCaracteristicaFlorestaProducao;
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
		FlorestalCaracteristicaFlorestaProducaoPK other = (FlorestalCaracteristicaFlorestaProducaoPK) obj;
		if (ideCaracteristicaFlorestaProducao != other.ideCaracteristicaFlorestaProducao)
			return false;
		if (ideFlorestal != other.ideFlorestal)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "br.gov.ba.seia.entity.FlorestalCaracteristicaFlorestaProducaoPK [ideCaracteristicaFlorestaProducao="
				+ ideCaracteristicaFlorestaProducao + ", ideFlorestal=" + ideFlorestal + "]";
	}
}
