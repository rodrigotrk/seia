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

import br.gov.ba.seia.interfaces.BaseEntity;


/**
 * The persistent class for the fce_abastecimento_humano database table.
 * 
 */
@Entity
@Table(name="fce_abastecimento_humano")
public class FceAbastecimentoHumano implements Serializable, BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fce_abastecimento_humano_fce_ide_abastecimento_humano_generator")
	@SequenceGenerator(name = "fce_abastecimento_humano_fce_ide_abastecimento_humano_generator", sequenceName = "fce_abastecimento_humano_fce_ide_abastecimento_humano_seq", allocationSize = 1)
	@Column(name="fce_ide_abastecimento_humano")
	private Integer fceIdeAbastecimentoHumano;

	@NotNull
	@JoinColumn(name="ide_fce",referencedColumnName = "ide_fce", nullable = false)
	@OneToOne(fetch = FetchType.LAZY)
	private Fce ideFce;

	@Column(name="num_consumo_atual")
	private BigDecimal numConsumoAtual;

	@Column(name="num_consumo_per_capita")
	private BigDecimal numConsumoPerCapita;

	@Column(name="num_consumo_projetado")
	private BigDecimal numConsumoProjetado;

	@Column(name="num_horizonte_projeto")
	private Integer numHorizonteProjeto;

	@Column(name="num_populacao_atual")
	private Integer numPopulacaoAtual;

	@Column(name="num_populacao_projetada")
	private Integer numPopulacaoProjetada;

	@Column(name="num_tempo_captacao")
	private Integer numTempoCaptacao;

	//bi-directional many-to-one association to TipoPeriodoDerivacao
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_tipo_periodo_derivacao")
	private TipoPeriodoDerivacao ideTipoPeriodoDerivacao;

	public FceAbastecimentoHumano() {
		
	}
	
	public FceAbastecimentoHumano(Fce fce) {
		this.setIdeFce(fce);
	}

	public FceAbastecimentoHumano(TipoPeriodoDerivacao tipoPeriodoDerivacao){
		this.ideTipoPeriodoDerivacao = tipoPeriodoDerivacao;
	}

	public Integer getFceIdeAbastecimentoHumano() {
		return this.fceIdeAbastecimentoHumano;
	}

	public void setFceIdeAbastecimentoHumano(Integer fceIdeAbastecimentoHumano) {
		this.fceIdeAbastecimentoHumano = fceIdeAbastecimentoHumano;
	}

	public BigDecimal getNumConsumoAtual() {
		return this.numConsumoAtual;
	}

	public void setNumConsumoAtual(BigDecimal numConsumoAtual) {
		this.numConsumoAtual = numConsumoAtual;
	}

	public BigDecimal getNumConsumoPerCapita() {
		return this.numConsumoPerCapita;
	}

	public void setNumConsumoPerCapita(BigDecimal numConsumoPerCapita) {
		this.numConsumoPerCapita = numConsumoPerCapita;
	}

	public BigDecimal getNumConsumoProjetado() {
		return this.numConsumoProjetado;
	}

	public void setNumConsumoProjetado(BigDecimal numConsumoProjetado) {
		this.numConsumoProjetado = numConsumoProjetado;
	}

	public Integer getNumHorizonteProjeto() {
		return this.numHorizonteProjeto;
	}

	public void setNumHorizonteProjeto(Integer numHorizonteProjeto) {
		this.numHorizonteProjeto = numHorizonteProjeto;
	}

	public Integer getNumPopulacaoAtual() {
		return this.numPopulacaoAtual;
	}

	public void setNumPopulacaoAtual(Integer numPopulacaoAtual) {
		this.numPopulacaoAtual = numPopulacaoAtual;
	}

	public Integer getNumPopulacaoProjetada() {
		return this.numPopulacaoProjetada;
	}

	public void setNumPopulacaoProjetada(Integer numPopulacaoProjetada) {
		this.numPopulacaoProjetada = numPopulacaoProjetada;
	}

	public Integer getNumTempoCaptacao() {
		return this.numTempoCaptacao;
	}

	public void setNumTempoCaptacao(Integer numTempoCaptacao) {
		this.numTempoCaptacao = numTempoCaptacao;
	}

	@Override
	public Long getId() {
		return new Long(fceIdeAbastecimentoHumano);
	}

	public TipoPeriodoDerivacao getIdeTipoPeriodoDerivacao() {
		return ideTipoPeriodoDerivacao;
	}

	public void setIdeTipoPeriodoDerivacao(TipoPeriodoDerivacao ideTipoPeriodoDerivacao) {
		this.ideTipoPeriodoDerivacao = ideTipoPeriodoDerivacao;
	}

	public Fce getIdeFce() {
		return ideFce;
	}

	public void setIdeFce(Fce ideFce) {
		this.ideFce = ideFce;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fceIdeAbastecimentoHumano == null) ? 0 : fceIdeAbastecimentoHumano.hashCode());
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
		FceAbastecimentoHumano other = (FceAbastecimentoHumano) obj;
		if (fceIdeAbastecimentoHumano == null) {
			if (other.fceIdeAbastecimentoHumano != null)
				return false;
		}
		else if (!fceIdeAbastecimentoHumano.equals(other.fceIdeAbastecimentoHumano))
			return false;
		return true;
	}

}