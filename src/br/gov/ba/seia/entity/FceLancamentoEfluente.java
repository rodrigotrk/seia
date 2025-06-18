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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;


/**
 * The persistent class for the fce_lancamento_efluente database table.
 * 
 */
@Entity
@Table(name="fce_lancamento_efluente")
public class FceLancamentoEfluente implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="FCE_LANCAMENTO_EFLUENTE_IDEFCELANCAMENTOEFLUENTE_GENERATOR", sequenceName="FCE_LANCAMENTO_EFLUENTE_IDE_FCE_LANCAMENTO_EFLUENTE_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="FCE_LANCAMENTO_EFLUENTE_IDEFCELANCAMENTOEFLUENTE_GENERATOR")
	@Column(name="ide_fce_lancamento_efluente")
	private Integer ideFceLancamentoEfluente;

	@NotNull
	@JoinColumn(name="ide_fce_outorga_localizacao_geografica",referencedColumnName = "ide_fce_outorga_localizacao_geografica", nullable = false)
	@OneToOne(fetch = FetchType.LAZY)
	private FceOutorgaLocalizacaoGeografica ideFceOutorgaLocalizacaoGeografica;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ide_documento_obrigatorio_requerimento", referencedColumnName = "ide_documento_obrigatorio_requerimento")
	private DocumentoObrigatorioRequerimento ideDocumentoObrigatorioRequerimento;

	@Column(name="num_tempo_lancamento")
	private Integer numTempoLancamento;

	@Column(name="num_vazao_efluente")
	private BigDecimal numVazaoEfluente;

	//uni-directional many-to-one association to TipoPeriodoDerivacao
	@NotNull
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_tipo_periodo_derivacao",referencedColumnName = "ide_tipo_periodo_derivacao", nullable = false)
	private TipoPeriodoDerivacao ideTipoPeriodoDerivacao;

	@Transient
	private boolean edicao;

	public FceLancamentoEfluente() {
	}

	public FceLancamentoEfluente(FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeografica) {
		ideFceOutorgaLocalizacaoGeografica = fceOutorgaLocalizacaoGeografica;
	}

	public Integer getIdeFceLancamentoEfluente() {
		return ideFceLancamentoEfluente;
	}

	public void setIdeFceLancamentoEfluente(Integer ideFceLancamentoEfluente) {
		this.ideFceLancamentoEfluente = ideFceLancamentoEfluente;
	}

	public Integer getNumTempoLancamento() {
		return numTempoLancamento;
	}

	public void setNumTempoLancamento(Integer numTempoLancamento) {
		this.numTempoLancamento = numTempoLancamento;
	}

	public BigDecimal getNumVazaoEfluente() {
		return numVazaoEfluente;
	}

	public void setNumVazaoEfluente(BigDecimal numVazaoEfluente) {
		this.numVazaoEfluente = numVazaoEfluente;
	}

	public TipoPeriodoDerivacao getIdeTipoPeriodoDerivacao() {
		return ideTipoPeriodoDerivacao;
	}

	public void setIdeTipoPeriodoDerivacao(TipoPeriodoDerivacao ideTipoPeriodoDerivacao) {
		this.ideTipoPeriodoDerivacao = ideTipoPeriodoDerivacao;
	}

	public FceOutorgaLocalizacaoGeografica getIdeFceOutorgaLocalizacaoGeografica() {
		return ideFceOutorgaLocalizacaoGeografica;
	}

	public void setIdeFceOutorgaLocalizacaoGeografica(FceOutorgaLocalizacaoGeografica ideFceOutorgaLocalizacaoGeografica) {
		this.ideFceOutorgaLocalizacaoGeografica = ideFceOutorgaLocalizacaoGeografica;
	}

	public DocumentoObrigatorioRequerimento getIdeDocumentoObrigatorioRequerimento() {
		return ideDocumentoObrigatorioRequerimento;
	}

	public void setIdeDocumentoObrigatorioRequerimento(DocumentoObrigatorioRequerimento ideDocumentoObrigatorioRequerimento) {
		this.ideDocumentoObrigatorioRequerimento = ideDocumentoObrigatorioRequerimento;
	}

	public boolean isEdicao() {
		return edicao;
	}

	public void setEdicao(boolean edicao) {
		this.edicao = edicao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideFceLancamentoEfluente == null) ? 0
						: ideFceLancamentoEfluente.hashCode());
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
		FceLancamentoEfluente other = (FceLancamentoEfluente) obj;
		if (ideFceLancamentoEfluente == null) {
			if (other.ideFceLancamentoEfluente != null) {
				return false;
			}
		} else if (!ideFceLancamentoEfluente
				.equals(other.ideFceLancamentoEfluente)) {
			return false;
		}
		return true;
	}
	
	public FceLancamentoEfluente clone() throws CloneNotSupportedException {
		return (FceLancamentoEfluente) super.clone();
	}
	
}