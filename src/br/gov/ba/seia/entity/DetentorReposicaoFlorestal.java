package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
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

@Entity
@Table(name = "detentor_reposicao_florestal")
@XmlRootElement
public class DetentorReposicaoFlorestal implements Serializable {

	private static final long serialVersionUID = 8551967613844767243L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DETENTOR_REPOSICAO_FLORESTAL_SEQ")
	@SequenceGenerator(name = "DETENTOR_REPOSICAO_FLORESTAL_SEQ", sequenceName = "DETENTOR_REPOSICAO_FLORESTAL_SEQ", allocationSize = 1)
	@NotNull
	@Column(name = "ide_detentor_reposicao_florestal")
	private Integer ideDetentorReposicaoFlorestal;

	@JoinColumn(name = "ide_cumprimento_reposicao_florestal", referencedColumnName = "ide_cumprimento_reposicao_florestal", nullable = false)
	@ManyToOne(optional = false)
	private CumprimentoReposicaoFlorestal ideCumprimentoReposicaoFlorestal;

	@Column(name = "num_portaria", length = 100, nullable = false)
	private String numPortaria;

	@Column(name = "data_publicacao_portaria", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataPublicacaoPortaria;

	@Column(name = "num_processo", length = 100, nullable = false)
	private String numProcesso;

	@Column(name = "volume_autorizado", nullable = false, precision = 12, scale = 4)
	private BigDecimal volumeAutorizado;

	@JoinColumn(name = "ide_tipo_volume_florestal_remanescente", referencedColumnName = "ide_tipo_volume_florestal_remanescente", nullable = true)
	@ManyToOne(optional = false)
	private TipoVolumeFlorestalRemanescente ideTipoVolumeFlorestalRemanescente;

	@Column(name = "num_processo_asv_aml", length = 100, nullable = true)
	private String numProcessoAsvAml;
	
	@JoinColumn(name = "ide_unidade_medida", referencedColumnName = "ide_unidade_medida", nullable = false)
	@ManyToOne(optional = false)
	private UnidadeMedida ideUnidadeMedida;

	public DetentorReposicaoFlorestal() {}
	
	public DetentorReposicaoFlorestal(Integer ideDetentorReposicaoFlorestal) {
		this.ideDetentorReposicaoFlorestal = ideDetentorReposicaoFlorestal;
	}
	
	public Integer getIdeDetentorReposicaoFlorestal() {
		return ideDetentorReposicaoFlorestal;
	}

	public void setIdeDetentorReposicaoFlorestal(
			Integer ideDetentorReposicaoFlorestal) {
		this.ideDetentorReposicaoFlorestal = ideDetentorReposicaoFlorestal;
	}

	public CumprimentoReposicaoFlorestal getIdeCumprimentoReposicaoFlorestal() {
		return ideCumprimentoReposicaoFlorestal;
	}

	public void setIdeCumprimentoReposicaoFlorestal(
			CumprimentoReposicaoFlorestal ideCumprimentoReposicaoFlorestal) {
		this.ideCumprimentoReposicaoFlorestal = ideCumprimentoReposicaoFlorestal;
	}

	public String getNumPortaria() {
		return numPortaria;
	}

	public void setNumPortaria(String numPortaria) {
		this.numPortaria = numPortaria;
	}

	public Date getDataPublicacaoPortaria() {
		return dataPublicacaoPortaria;
	}

	public void setDataPublicacaoPortaria(Date dataPublicacaoPortaria) {
		this.dataPublicacaoPortaria = dataPublicacaoPortaria;
	}

	public String getNumProcesso() {
		return numProcesso;
	}

	public void setNumProcesso(String numProcesso) {
		this.numProcesso = numProcesso;
	}

	public BigDecimal getVolumeAutorizado() {
		return volumeAutorizado;
	}

	public void setVolumeAutorizado(BigDecimal volumeAutorizado) {
		this.volumeAutorizado = volumeAutorizado;
	}

	public TipoVolumeFlorestalRemanescente getIdeTipoVolumeFlorestalRemanescente() {
		return ideTipoVolumeFlorestalRemanescente;
	}

	public void setIdeTipoVolumeFlorestalRemanescente(
			TipoVolumeFlorestalRemanescente ideTipoVolumeFlorestalRemanescente) {
		this.ideTipoVolumeFlorestalRemanescente = ideTipoVolumeFlorestalRemanescente;
	}

	public String getNumProcessoAsvAml() {
		return numProcessoAsvAml;
	}

	public void setNumProcessoAsvAml(String numProcessoAsvAml) {
		this.numProcessoAsvAml = numProcessoAsvAml;
	}

	public UnidadeMedida getIdeUnidadeMedida() {
		return ideUnidadeMedida;
	}

	public void setIdeUnidadeMedida(UnidadeMedida ideUnidadeMedida) {
		this.ideUnidadeMedida = ideUnidadeMedida;
	}
}
