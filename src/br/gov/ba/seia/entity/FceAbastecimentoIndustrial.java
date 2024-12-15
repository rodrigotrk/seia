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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;




/**
 * The persistent class for the fce_abastecimento_industrial database table.
 * 
 */
@Entity
@Table(name="fce_abastecimento_industrial")
public class FceAbastecimentoIndustrial implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fce_abastecimento_industrial_fce_ide_abs_industrial_generator")
	@SequenceGenerator(name = "fce_abastecimento_industrial_fce_ide_abs_industrial_generator", sequenceName = "fce_abastecimento_industrial_fce_ide_abs_industrial_seq", allocationSize = 1)
	@Column(name="ide_fce_abastecimento_industrial")
	private Integer ideFceAbastecimentoIndustrial;

	@NotNull
	@JoinColumn(name="ide_fce",referencedColumnName = "ide_fce", nullable = false)
	@OneToOne(fetch = FetchType.LAZY)
	private Fce ideFce;

	@NotNull
	@JoinColumn(name="ide_tipo_periodo_derivacao",referencedColumnName = "ide_tipo_periodo_derivacao", nullable = false)
	@OneToOne(fetch = FetchType.LAZY)
	private TipoPeriodoDerivacao ideTipoPeriodoDerivacao;

	@Column(name="num_consumo_agua_industrializado")
	private BigDecimal numConsumoAguaIndustrializado;

	@Column(name="num_tempo_captacao")
	private Integer numTempoCaptacao;

	public FceAbastecimentoIndustrial() {
		this.ideTipoPeriodoDerivacao = new TipoPeriodoDerivacao();
	}
	
	public FceAbastecimentoIndustrial(Fce fce) {
		this.ideTipoPeriodoDerivacao = new TipoPeriodoDerivacao();
		this.ideFce = fce;
	}

	public Integer getIdeFceAbastecimentoIndustrial() {
		return this.ideFceAbastecimentoIndustrial;
	}

	public void setIdeFceAbastecimentoIndustrial(Integer ideFceAbastecimentoIndustrial) {
		this.ideFceAbastecimentoIndustrial = ideFceAbastecimentoIndustrial;
	}

	public BigDecimal getNumConsumoAguaIndustrializado() {
		return this.numConsumoAguaIndustrializado;
	}

	public void setNumConsumoAguaIndustrializado(BigDecimal numConsumoAguaIndustrializado) {
		this.numConsumoAguaIndustrializado = numConsumoAguaIndustrializado;
	}

	public Integer getNumTempoCaptacao() {
		return this.numTempoCaptacao;
	}

	public void setNumTempoCaptacao(Integer numTempoCaptacao) {
		this.numTempoCaptacao = numTempoCaptacao;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime
				* result)
				+ ((ideFceAbastecimentoIndustrial == null) ? 0
						: ideFceAbastecimentoIndustrial.hashCode());
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
		FceAbastecimentoIndustrial other = (FceAbastecimentoIndustrial) obj;
		if (ideFceAbastecimentoIndustrial == null) {
			if (other.ideFceAbastecimentoIndustrial != null) {
				return false;
			}
		} else if (!ideFceAbastecimentoIndustrial
				.equals(other.ideFceAbastecimentoIndustrial)) {
			return false;
		}
		return true;
	}
}