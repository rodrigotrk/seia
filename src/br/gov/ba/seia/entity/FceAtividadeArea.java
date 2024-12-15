package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.util.Util;


/**
 * The persistent class for the fce_atividade_area database table.
 * 
 */
@Entity
@Table(name="fce_atividade_area")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "FceAtividadeArea.removeByIdeFce", query = "DELETE FROM FceAtividadeArea f WHERE f.ideFce.ideFce = :ideFce")})
public class FceAtividadeArea implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="FCE_ATIVIDADE_AREA_IDE_FCE_ATIVIDADE_AREA_SEQ", sequenceName="FCE_ATIVIDADE_AREA_IDE_FCE_ATIVIDADE_AREA_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="FCE_ATIVIDADE_AREA_IDE_FCE_ATIVIDADE_AREA_SEQ")
	@Basic(optional = false)
	@NotNull
	@Column(name="ide_fce_atividade_area")
	private Integer ideFceAtividadeArea;

	@NotNull
	@JoinColumn(name="ide_fce",referencedColumnName = "ide_fce", nullable = false)
	@OneToOne(fetch = FetchType.LAZY)
	private Fce ideFce;

	@NotNull
	@JoinColumn(name="ide_tipologia_atividade",referencedColumnName = "ide_tipologia_atividade", nullable = false)
	@OneToOne(fetch = FetchType.LAZY)
	private TipologiaAtividade ideTipologiaAtividade;

	@Column(name="num_area")
	private BigDecimal numArea;

	@Transient
	private boolean confirmado;
	@Transient
	private boolean edicao;
	@Transient
	private boolean isToPulverizacao;
	@Transient
	private boolean isToIrrigacao;

	// Ajustes Homologação #5048
	@JoinColumn(name="ide_metodo_irrigacao",referencedColumnName = "ide_metodo_irrigacao")
	@ManyToOne(fetch=FetchType.LAZY)
	private MetodoIrrigacao ideMetodoIrrigacao;

	@JoinColumn(name="ide_tipo_periodo_derivacao",referencedColumnName = "ide_tipo_periodo_derivacao")
	@ManyToOne(fetch=FetchType.LAZY)
	private TipoPeriodoDerivacao ideTipoPeriodoDerivacao;

	@Column(name="num_volume_derivar")
	private BigDecimal numVolumeDerivar;

	@Column(name="num_tempo_captacao")
	private Integer numTempoCaptacao;

	@Column(name="ind_outros_metodo")
	private Boolean indOutrosMetodo;

	public FceAtividadeArea() {
	}

	public FceAtividadeArea(TipologiaAtividade tipologiaAtividade) {
		this.ideTipologiaAtividade = tipologiaAtividade;
	}

	public FceAtividadeArea(MetodoIrrigacao metodoIrrigacao) {
		this.ideMetodoIrrigacao = metodoIrrigacao;
	}

	public Integer getIdeFceAtividadeArea() {
		return this.ideFceAtividadeArea;
	}

	public void setIdeFceAtividadeArea(Integer ideFceAtividadeArea) {
		this.ideFceAtividadeArea = ideFceAtividadeArea;
	}

	public BigDecimal getNumArea() {
		return this.numArea;
	}

	public void setNumArea(BigDecimal numArea) {
		this.numArea = numArea;
	}

	public Fce getIdeFce() {
		return ideFce;
	}

	public void setIdeFce(Fce ideFce) {
		this.ideFce = ideFce;
	}

	public TipologiaAtividade getIdeTipologiaAtividade() {
		return ideTipologiaAtividade;
	}

	public void setIdeTipologiaAtividade(TipologiaAtividade ideTipologiaAtividade) {
		this.ideTipologiaAtividade = ideTipologiaAtividade;
	}

	public boolean isConfirmado() {
		return confirmado;
	}

	public void setConfirmado(boolean confirmado) {
		this.confirmado = confirmado;
	}

	public boolean isEdicao() {
		return edicao;
	}

	public void setEdicao(boolean edicao) {
		this.edicao = edicao;
	}

	public boolean isToPulverizacao() {
		return isToPulverizacao;
	}

	public void setToPulverizacao(boolean isToPulverizacao) {
		this.isToPulverizacao = isToPulverizacao;
	}

	public boolean isToIrrigacao() {
		return isToIrrigacao;
	}

	public void setToIrrigacao(boolean isToIrrigacao) {
		this.isToIrrigacao = isToIrrigacao;
	}

	public MetodoIrrigacao getIdeMetodoIrrigacao() {
		return ideMetodoIrrigacao;
	}

	public void setIdeMetodoIrrigacao(MetodoIrrigacao ideMetodoIrrigacao) {
		this.ideMetodoIrrigacao = ideMetodoIrrigacao;
	}

	public TipoPeriodoDerivacao getIdeTipoPeriodoDerivacao() {
		return ideTipoPeriodoDerivacao;
	}

	public void setIdeTipoPeriodoDerivacao(TipoPeriodoDerivacao ideTipoPeriodoDerivacao) {
		this.ideTipoPeriodoDerivacao = ideTipoPeriodoDerivacao;
	}

	public BigDecimal getNumVolumeDerivar() {
		return numVolumeDerivar;
	}

	public void setNumVolumeDerivar(BigDecimal numVolumeDerivar) {
		this.numVolumeDerivar = numVolumeDerivar;
	}

	public Integer getNumTempoCaptacao() {
		return numTempoCaptacao;
	}

	public void setNumTempoCaptacao(Integer numTempoCaptacao) {
		this.numTempoCaptacao = numTempoCaptacao;
	}

	public Boolean getIndOutrosMetodo() {
		return indOutrosMetodo;
	}

	public void setIndOutrosMetodo(Boolean indOutrosMetodo) {
		this.indOutrosMetodo = indOutrosMetodo;
	}

	// Controlador de Tela
	public boolean isTempoCaptacaoHabilitado(){
		return !Util.isNullOuVazio(this.ideTipoPeriodoDerivacao) && this.ideTipoPeriodoDerivacao.isIntermitente();
	}
	
	public FceAtividadeArea clone() throws CloneNotSupportedException {
		return (FceAtividadeArea) super.clone();
	}
}