package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.gov.ba.seia.interfaces.BaseEntity;


/**
 * The persistent class for the ocorrencia_area database table.
 * 
 */
@Entity
@Table(name="ocorrencia_area")
public class OcorrenciaArea implements Serializable, BaseEntity {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name="ide_ocorrencia_area")
	private Integer ideOcorrenciaArea;

	@Column(name="dsc_ocorrencia_area")
	private String dscOcorrenciaArea;

	@Column(name="ind_ativo")
	private Boolean indAtivo;

	@Transient
	private Double numArea;

	public OcorrenciaArea() {
	}

	public OcorrenciaArea(Integer integer) {
		this.ideOcorrenciaArea = integer;
	}

	public Integer getIdeOcorrenciaArea() {
		return this.ideOcorrenciaArea;
	}

	public void setIdeOcorrenciaArea(Integer ideOcorrenciaArea) {
		this.ideOcorrenciaArea = ideOcorrenciaArea;
	}

	public String getDscOcorrenciaArea() {
		return this.dscOcorrenciaArea;
	}

	public void setDscOcorrenciaArea(String dscOcorrenciaArea) {
		this.dscOcorrenciaArea = dscOcorrenciaArea;
	}

	public Boolean getIndAtivo() {
		return this.indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

	@Override
	public Long getId() {
		return new Long(ideOcorrenciaArea);
	}

	public Double getNumArea() {
		return numArea;
	}

	public void setNumArea(Double numArea) {
		this.numArea = numArea;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideOcorrenciaArea == null) ? 0 : ideOcorrenciaArea
						.hashCode());
		return result;
	}

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
		OcorrenciaArea other = (OcorrenciaArea) obj;
		if (ideOcorrenciaArea == null) {
			if (other.ideOcorrenciaArea != null) {
				return false;
			}
		} else if (!ideOcorrenciaArea.equals(other.ideOcorrenciaArea)) {
			return false;
		}
		return true;
	}
}