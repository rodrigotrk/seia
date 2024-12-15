package br.gov.ba.seia.entity;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.ba.seia.entity.generic.AbstractEntity;

@Entity
@Table(name="fce_outorga_infraestrutura")
public class FceOutorgaInfraestrutura extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fce_outorga_infraestrutura_seq")
	@SequenceGenerator(name = "fce_outorga_infraestrutura_seq", sequenceName = "fce_outorga_infraestrutura_seq", allocationSize = 1)
	@Column(name="ide_fce_outorga_infraestrutura")
	private Integer ideFceOutorgaInfraestrutura;
	
	@Basic(optional = false)
	@Column(name="val_vazao", precision = 10, scale = 2)
	private Double valVazao;
	
	@Column(name="val_tempo_captacao", length = 2)
	private Integer valTempoCaptacao;
	
	@Basic(optional = false)
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_fce", referencedColumnName="ide_fce")
	private Fce ideFce;
	
	@Basic(optional = false)
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_tipo_periodo_derivacao", referencedColumnName="ide_tipo_periodo_derivacao")
	private TipoPeriodoDerivacao ideTipoPeriodoDerivacao;
	
	@OneToMany(mappedBy="ideFceOutorgaInfraestrutura", fetch = FetchType.LAZY)
	private List<FceOutorgaTipoInfraestruturaUtilizada> fceOutorgaTipoInfraestruturaUtilizadaCollection;
	
	public FceOutorgaInfraestrutura() {}

	public FceOutorgaInfraestrutura(Fce ideFce) {
		this.ideFce = ideFce;
	}

	/*********************
	/*					 *
	//XXX: GETS AND SETS *
	/* 					 *
	/*********************/

	public Integer getIdeFceOutorgaInfraestrutura() {
		return ideFceOutorgaInfraestrutura;
	}

	public void setIdeFceOutorgaInfraestrutura(Integer ideFceOutorgaInfraestrutura) {
		this.ideFceOutorgaInfraestrutura = ideFceOutorgaInfraestrutura;
	}

	public Double getValVazao() {
		return valVazao;
	}

	public void setValVazao(Double valVazao) {
		this.valVazao = valVazao;
	}

	public Integer getValTempoCaptacao() {
		return valTempoCaptacao;
	}

	public void setValTempoCaptacao(Integer valTempoCaptacao) {
		this.valTempoCaptacao = valTempoCaptacao;
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

	public List<FceOutorgaTipoInfraestruturaUtilizada> getFceOutorgaTipoInfraestruturaUtilizadaCollection() {
		return fceOutorgaTipoInfraestruturaUtilizadaCollection;
	}

	public void setFceOutorgaTipoInfraestruturaUtilizadaCollection(List<FceOutorgaTipoInfraestruturaUtilizada> fceOutorgaTipoInfraestruturaUtilizadaCollection) {
		this.fceOutorgaTipoInfraestruturaUtilizadaCollection = fceOutorgaTipoInfraestruturaUtilizadaCollection;
	}
}