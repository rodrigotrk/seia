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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;


/**
 * The persistent class for the fce_lancamento_efluente_caracteristica database table.
 * 
 */
@Entity
@Table(name="fce_lancamento_efluente_caracteristica")
public class FceLancamentoEfluenteCaracteristica implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="FCE_LANCAMENTO_EFLUENTE_CARACTERISTICA_IDEFCELANCAMENTOEFLUENTECARACTERISTICA_GENERATOR", sequenceName="FCE_LANC_EFLU_IDE_FCE_LANC_EFLU_CARACTERISTICA_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="FCE_LANCAMENTO_EFLUENTE_CARACTERISTICA_IDEFCELANCAMENTOEFLUENTECARACTERISTICA_GENERATOR")
	@Column(name="ide_fce_lancamento_efluente_caracteristica")
	private Integer ideFceLancamentoEfluenteCaracteristica;

	@NotNull
	@JoinColumn(name="ide_caracteristica_efluente",referencedColumnName = "ide_caracteristica_efluente", nullable = false)
	@OneToOne(fetch = FetchType.LAZY)
	private CaracteristicaEfluente ideCaracteristicaEfluente;

	@NotNull
	@JoinColumn(name="ide_fce_lancamento_efluente",referencedColumnName = "ide_fce_lancamento_efluente", nullable = false)
	@OneToOne(fetch = FetchType.LAZY)
	private FceLancamentoEfluente ideFceLancamentoEfluente;

	@Column(name="num_bruto")
	private BigDecimal numBruto;

	@Column(name="num_eficiencia_remocao")
	private BigDecimal numEficienciaRemocao;

	@Column(name="num_tratado")
	private BigDecimal numTratado;

	@Transient
	private boolean confirmado;

	public FceLancamentoEfluenteCaracteristica() {
	}

	public FceLancamentoEfluenteCaracteristica(CaracteristicaEfluente caracteristicaEfluente, FceLancamentoEfluente fceLancamentoEfluente) {
		this.ideCaracteristicaEfluente = caracteristicaEfluente;
		this.ideFceLancamentoEfluente = fceLancamentoEfluente;
	}

	public Integer getIdeFceLancamentoEfluenteCaracteristica() {
		return this.ideFceLancamentoEfluenteCaracteristica;
	}

	public void setIdeFceLancamentoEfluenteCaracteristica(Integer ideFceLancamentoEfluenteCaracteristica) {
		this.ideFceLancamentoEfluenteCaracteristica = ideFceLancamentoEfluenteCaracteristica;
	}

	public BigDecimal getNumBruto() {
		return this.numBruto;
	}

	public void setNumBruto(BigDecimal numBruto) {
		this.numBruto = numBruto;
	}

	public BigDecimal getNumEficienciaRemocao() {
		return this.numEficienciaRemocao;
	}

	public void setNumEficienciaRemocao(BigDecimal numEficienciaRemocao) {
		this.numEficienciaRemocao = numEficienciaRemocao;
	}

	public BigDecimal getNumTratado() {
		return this.numTratado;
	}

	public void setNumTratado(BigDecimal numTratado) {
		this.numTratado = numTratado;
	}

	public CaracteristicaEfluente getIdeCaracteristicaEfluente() {
		return ideCaracteristicaEfluente;
	}

	public void setIdeCaracteristicaEfluente(CaracteristicaEfluente ideCaracteristicaEfluente) {
		this.ideCaracteristicaEfluente = ideCaracteristicaEfluente;
	}

	public FceLancamentoEfluente getIdeFceLancamentoEfluente() {
		return ideFceLancamentoEfluente;
	}

	public void setIdeFceLancamentoEfluente(FceLancamentoEfluente ideFceLancamentoEfluente) {
		this.ideFceLancamentoEfluente = ideFceLancamentoEfluente;
	}

	public boolean isConfirmado() {
		return confirmado;
	}

	public void setConfirmado(boolean confirmado) {
		this.confirmado = confirmado;
	}
}