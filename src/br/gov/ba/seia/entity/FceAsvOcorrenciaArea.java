package br.gov.ba.seia.entity;

import java.io.Serializable;

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
@Table(name="fce_asv_ocorrencia_area")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "FceAsvOcorrenciaArea.deleteByIdeFceAsv", query = "DELETE FROM FceAsvOcorrenciaArea WHERE ideFceAsv = :ideFceAsv")})
public class FceAsvOcorrenciaArea implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private FceAsvOcorrenciaAreaPK ideFceAsvOcorrenciaPK;

	@NotNull
	@JoinColumn(name = "ide_ocorrencia_area", referencedColumnName = "ide_ocorrencia_area", nullable = false, insertable = false, updatable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private OcorrenciaArea ideOcorrenciaArea;

	@NotNull
	@JoinColumn(name = "ide_fce_asv", referencedColumnName = "ide_fce_asv", nullable = false, insertable = false, updatable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private FceAsv ideFceAsv;

	@Column(name = "num_area")
	private Double numArea;

	public FceAsvOcorrenciaArea() {
	}

	public FceAsvOcorrenciaArea(FceAsvOcorrenciaAreaPK ideFceAsvOcorrenciaPK) {
		this.ideFceAsvOcorrenciaPK = ideFceAsvOcorrenciaPK;
	}

	public FceAsvOcorrenciaArea(Integer ideFceAsv, Integer ideOcorrenciaArea) {
		this.ideFceAsvOcorrenciaPK = new FceAsvOcorrenciaAreaPK(ideFceAsv, ideOcorrenciaArea);
	}

	public OcorrenciaArea getIdeOcorrenciaArea() {
		return this.ideOcorrenciaArea;
	}

	public void setIdeOcorrenciaArea(OcorrenciaArea ideOcorrenciaArea) {
		this.ideOcorrenciaArea = ideOcorrenciaArea;
	}

	public FceAsv getIdeFceAsv() {
		return this.ideFceAsv;
	}

	public void setIdeFceAsv(FceAsv ideFceAsv) {
		this.ideFceAsv = ideFceAsv;
	}

	public FceAsvOcorrenciaAreaPK getIdeFceAsvOcorrenciaPK() {
		return this.ideFceAsvOcorrenciaPK;
	}

	public void setIdeFceAsvOcorrenciaPK(FceAsvOcorrenciaAreaPK ideFceAsvOcorrenciaPK) {
		this.ideFceAsvOcorrenciaPK = ideFceAsvOcorrenciaPK;
	}

	public Double getNumArea() {
		return numArea;
	}

	public void setNumArea(Double numArea) {
		this.numArea = numArea;
	}
}