package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "numero_car_reposicao_florestal")
@XmlRootElement
public class NumeroCarReposicaoFlorestal implements Serializable {

	private static final long serialVersionUID = 5468159831107514495L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NUMERO_CAR_REPOSICAO_FLORESTAL_SEQ")
	@SequenceGenerator(name = "NUMERO_CAR_REPOSICAO_FLORESTAL_SEQ", sequenceName = "NUMERO_CAR_REPOSICAO_FLORESTAL_SEQ", allocationSize = 1)
	@NotNull
	@Column(name = "ide_numero_car_reposicao_florestal")
	private Integer ideNumeroCarReposicaoFlorestal;

	@JoinColumn(name = "ide_consumidor_reposicao_florestal", referencedColumnName = "ide_consumidor_reposicao_florestal", nullable = false)
	@ManyToOne(optional = false)
	private ConsumidorReposicaoFlorestal ideConsumidorReposicaoFlorestal;

	@Column(name = "num_car", length = 50, nullable = false)
	private String numCAR;

	public Integer getIdeNumeroCarReposicaoFlorestal() {
		return ideNumeroCarReposicaoFlorestal;
	}

	public void setIdeNumeroCarReposicaoFlorestal(
			Integer ideNumeroCarReposicaoFlorestal) {
		this.ideNumeroCarReposicaoFlorestal = ideNumeroCarReposicaoFlorestal;
	}

	public ConsumidorReposicaoFlorestal getIdeConsumidorReposicaoFlorestal() {
		return ideConsumidorReposicaoFlorestal;
	}

	public void setIdeConsumidorReposicaoFlorestal(
			ConsumidorReposicaoFlorestal ideConsumidorReposicaoFlorestal) {
		this.ideConsumidorReposicaoFlorestal = ideConsumidorReposicaoFlorestal;
	}

	public String getNumCAR() {
		return numCAR;
	}

	public void setNumCAR(String numCAR) {
		this.numCAR = numCAR;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideConsumidorReposicaoFlorestal == null) ? 0
						: ideConsumidorReposicaoFlorestal.hashCode());
		result = prime
				* result
				+ ((ideNumeroCarReposicaoFlorestal == null) ? 0
						: ideNumeroCarReposicaoFlorestal.hashCode());
		result = prime * result + ((numCAR == null) ? 0 : numCAR.hashCode());
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
		NumeroCarReposicaoFlorestal other = (NumeroCarReposicaoFlorestal) obj;
		if (ideConsumidorReposicaoFlorestal == null) {
			if (other.ideConsumidorReposicaoFlorestal != null)
				return false;
		} else if (!ideConsumidorReposicaoFlorestal
				.equals(other.ideConsumidorReposicaoFlorestal))
			return false;
		if (ideNumeroCarReposicaoFlorestal == null) {
			if (other.ideNumeroCarReposicaoFlorestal != null)
				return false;
		} else if (!ideNumeroCarReposicaoFlorestal
				.equals(other.ideNumeroCarReposicaoFlorestal))
			return false;
		if (numCAR == null) {
			if (other.numCAR != null)
				return false;
		} else if (!numCAR.equals(other.numCAR))
			return false;
		return true;
	}
	
	
}
