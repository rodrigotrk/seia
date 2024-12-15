package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.interfaces.BaseEntity;

/**
 * The persistent class for the fce_agrossilvopastoril database table.
 * 
 */
@Entity
@XmlRootElement
@Table(name="fce_agrossilvopastoril")
public class FceAgrossilvopastoril implements Serializable,BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fce_agrossilvopastoril_ide_fce_agrossilvopastoril_generator")
	@SequenceGenerator(name = "fce_agrossilvopastoril_ide_fce_agrossilvopastoril_generator", sequenceName = "fce_agrossilvopastoril_ide_fce_agrossilvopastoril_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name="ide_fce_agrossilvopastoril")
	private Integer ideFceAgrossilvopastoril;

	@Basic(optional = false)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="dtc_prevista")
	private Date dtcPrevista;

	@Column(name="ind_adubacao")
	private Boolean indAdubacao;

	@Column(name="ind_defensivo_agricola")
	private Boolean indDefensivoAgricola;

	@Column(name="num_area_ampliacao",precision = 10, scale = 2, nullable = true)
	private Double numAreaAmpliacao;

	@Column(name="num_area_construida",precision = 10, scale = 2, nullable = true)
	private Double numAreaConstruida;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_fce",referencedColumnName = "ide_fce", nullable = false)
	private Fce ideFce;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_classificacao_adubacao", referencedColumnName = "ide_classificacao_adubacao", nullable = true)
	private ClassificacaoAdubacao ideClassificacaoAdubacao;

	@NotNull
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_tipo_caracterizacao_projeto", referencedColumnName = "ide_tipo_caracterizacao_projeto", nullable = false)
	private TipoCaracterizacaoProjeto ideTipoCaracterizacaoProjeto;

	public FceAgrossilvopastoril() {
	}

	public FceAgrossilvopastoril(TipoCaracterizacaoProjeto ideTipoCaracterizacaoProjeto) {
		this.ideTipoCaracterizacaoProjeto = ideTipoCaracterizacaoProjeto;
	}

	public Integer getIdeFceAgrossilvopastoril() {
		return this.ideFceAgrossilvopastoril;
	}

	public void setIdeFceAgrossilvopastoril(Integer ideFceAgrossilvopastoril) {
		this.ideFceAgrossilvopastoril = ideFceAgrossilvopastoril;
	}


	public Fce getIdeFce() {
		return ideFce;
	}

	public void setIdeFce(Fce ideFce) {
		this.ideFce = ideFce;
	}

	public Boolean getIndAdubacao() {
		return this.indAdubacao;
	}

	public void setIndAdubacao(Boolean indAdubacao) {
		this.indAdubacao = indAdubacao;
	}

	public Boolean getIndDefensivoAgricola() {
		return this.indDefensivoAgricola;
	}

	public void setIndDefensivoAgricola(Boolean indDefensivoAgricola) {
		this.indDefensivoAgricola = indDefensivoAgricola;
	}

	public Date getDtcPrevista() {
		return dtcPrevista;
	}

	public void setDtcPrevista(Date dtcPrevista) {
		this.dtcPrevista = dtcPrevista;
	}

	public Double getNumAreaAmpliacao() {
		return numAreaAmpliacao;
	}

	public void setNumAreaAmpliacao(Double numAreaAmpliacao) {
		this.numAreaAmpliacao = numAreaAmpliacao;
	}

	public Double getNumAreaConstruida() {
		return numAreaConstruida;
	}

	public void setNumAreaConstruida(Double numAreaConstruida) {
		this.numAreaConstruida = numAreaConstruida;
	}

	public TipoCaracterizacaoProjeto getIdeTipoCaracterizacaoProjeto() {
		return ideTipoCaracterizacaoProjeto;
	}

	public void setIdeTipoCaracterizacaoProjeto(TipoCaracterizacaoProjeto ideTipoCaracterizacaoProjeto) {
		this.ideTipoCaracterizacaoProjeto = ideTipoCaracterizacaoProjeto;
	}

	@Override
	public Long getId() {
		return new Long(ideFceAgrossilvopastoril);
	}

	public ClassificacaoAdubacao getIdeClassificacaoAdubacao() {
		return ideClassificacaoAdubacao;
	}

	public void setIdeClassificacaoAdubacao(
			ClassificacaoAdubacao ideClassificacaoAdubacao) {
		this.ideClassificacaoAdubacao = ideClassificacaoAdubacao;
	}


}