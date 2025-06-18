package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


/**
 * The persistent class for the fce_irrigacao database table.
 * 
 */
@Entity
@Table(name="fce_irrigacao")
public class FceIrrigacao implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="FCE_IRRIGACAO_IDEFCEIRRIGACAO_GENERATOR", sequenceName="FCE_IRRIGACAO_IDE_FCE_IRRIGACAO_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="FCE_IRRIGACAO_IDEFCEIRRIGACAO_GENERATOR")
	@Column(name="ide_fce_irrigacao")
	private Integer ideFceIrrigacao;

	@NotNull
	@JoinColumn(name="ide_fce",referencedColumnName = "ide_fce", nullable = false)
	@OneToOne(fetch = FetchType.LAZY)
	private Fce ideFce;

	@NotNull
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_tipo_periodo_derivacao",referencedColumnName = "ide_tipo_periodo_derivacao", nullable = false)
	private TipoPeriodoDerivacao ideTipoPeriodoDerivacao;

	@Column(name="num_tempo_captacao")
	private Integer numTempoCaptacao;

	@Column(name="num_volume_derivar")
	private BigDecimal numVolumeDerivar;

	//bi-directional many-to-one association to TipologiaIrrigacao
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_tipologia_irrigacao")
	private TipologiaIrrigacao ideTipologiaIrrigacao;

	@Column(name="ind_outros_cultura")
	private Boolean indOutrosCultura;

	public FceIrrigacao() {
		
	}
	
	public FceIrrigacao(Fce fce) {
		this.ideFce = fce;
	}

	public Integer getIdeFceIrrigacao() {
		return ideFceIrrigacao;
	}

	public void setIdeFceIrrigacao(Integer ideFceIrrigacao) {
		this.ideFceIrrigacao = ideFceIrrigacao;
	}

	public Integer getNumTempoCaptacao() {
		return numTempoCaptacao;
	}

	public void setNumTempoCaptacao(Integer numTempoCaptacao) {
		this.numTempoCaptacao = numTempoCaptacao;
	}

	public BigDecimal getNumVolumeDerivar() {
		return numVolumeDerivar;
	}

	public void setNumVolumeDerivar(BigDecimal numVolumeDerivar) {
		this.numVolumeDerivar = numVolumeDerivar;
	}

	public TipologiaIrrigacao getIdeTipologiaIrrigacao() {
		return ideTipologiaIrrigacao;
	}

	public void setIdeTipologiaIrrigacao(TipologiaIrrigacao tipologiaIrrigacao) {
		ideTipologiaIrrigacao = tipologiaIrrigacao;
	}

	public Fce getIdeFce() {
		return ideFce;
	}

	public void setIdeFce(Fce ideFce) {
		this.ideFce = ideFce;
	}

	public TipoPeriodoDerivacao getIdeTipoPeriodoDerivacao() {
		return ideTipoPeriodoDerivacao;
	}

	public void setIdeTipoPeriodoDerivacao(TipoPeriodoDerivacao ideTipoPeriodoDerivacao) {
		this.ideTipoPeriodoDerivacao = ideTipoPeriodoDerivacao;
	}

	public Boolean getIndOutrosCultura() {
		return indOutrosCultura;
	}

	public void setIndOutrosCultura(Boolean indOutrosCultura) {
		this.indOutrosCultura = indOutrosCultura;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ideFceIrrigacao == null) ? 0 : ideFceIrrigacao.hashCode());
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
		FceIrrigacao other = (FceIrrigacao) obj;
		if (ideFceIrrigacao == null) {
			if (other.ideFceIrrigacao != null) {
				return false;
			}
		} else if (!ideFceIrrigacao.equals(other.ideFceIrrigacao)) {
			return false;
		}
		return true;
	}
}