package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "consumidor_reposicao_florestal")
@XmlRootElement
public class ConsumidorReposicaoFlorestal implements Serializable {

	private static final long serialVersionUID = -3180441801649223428L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CONSUMIDOR_REPOSICAO_FLORESTAL_SEQ")
	@SequenceGenerator(name = "CONSUMIDOR_REPOSICAO_FLORESTAL_SEQ", sequenceName = "CONSUMIDOR_REPOSICAO_FLORESTAL_SEQ", allocationSize = 1)
	@NotNull
	@Column(name = "ide_consumidor_reposicao_florestal")
	private Integer ideConsumidorReposicaoFlorestal;

	@JoinColumn(name = "ide_cumprimento_reposicao_florestal", referencedColumnName = "ide_cumprimento_reposicao_florestal", nullable = false)
	@ManyToOne(optional = false)
	private CumprimentoReposicaoFlorestal ideCumprimentoReposicaoFlorestal;

	@Column(name = "vlm_material_lenhoso_consumido", nullable = false, precision = 12, scale = 4)
	private BigDecimal vlmMaterialLenhosoConsumido;

	@Column(name = "num_portaria_ato_adquirido", length = 100, nullable = false)
	private String numPortariaAtoAdquirido;

	@Column(name = "data_publicacao_portaria", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataPublicacaoPortaria;

	@Column(name = "num_processo_original", length = 100, nullable = false)
	private String numProcessoOriginal;

	@Column(name = "ind_possui_numero_car", nullable = false)
	private Boolean indPossuiNumeroCAR;
	
	@JoinColumn(name = "ide_unidade_medida", referencedColumnName = "ide_unidade_medida", nullable = false)
	@ManyToOne(optional = false)
	private UnidadeMedida ideUnidadeMedida;

	@OneToMany(mappedBy = "ideConsumidorReposicaoFlorestal", fetch = FetchType.LAZY)
	private Collection<NumeroCarReposicaoFlorestal> numeroCarReposicaoFlorestalCollection;

	@Transient
	private Collection<NumeroCarReposicaoFlorestal> numeroCarReposicaoFlorestalExcluido;
	
	public ConsumidorReposicaoFlorestal() {}
	
	public ConsumidorReposicaoFlorestal(Integer ideConsumidorReposicaoFlorestal) {
		this.ideConsumidorReposicaoFlorestal = ideConsumidorReposicaoFlorestal;
	}
	
	public Integer getIdeConsumidorReposicaoFlorestal() {
		return ideConsumidorReposicaoFlorestal;
	}

	public void setIdeConsumidorReposicaoFlorestal(
			Integer ideConsumidorReposicaoFlorestal) {
		this.ideConsumidorReposicaoFlorestal = ideConsumidorReposicaoFlorestal;
	}

	public CumprimentoReposicaoFlorestal getIdeCumprimentoReposicaoFlorestal() {
		return ideCumprimentoReposicaoFlorestal;
	}

	public void setIdeCumprimentoReposicaoFlorestal(
			CumprimentoReposicaoFlorestal ideCumprimentoReposicaoFlorestal) {
		this.ideCumprimentoReposicaoFlorestal = ideCumprimentoReposicaoFlorestal;
	}

	public BigDecimal getVlmMaterialLenhosoConsumido() {
		return vlmMaterialLenhosoConsumido;
	}

	public void setVlmMaterialLenhosoConsumido(
			BigDecimal vlmMaterialLenhosoConsumido) {
		this.vlmMaterialLenhosoConsumido = vlmMaterialLenhosoConsumido;
	}

	public String getNumPortariaAtoAdquirido() {
		return numPortariaAtoAdquirido;
	}

	public void setNumPortariaAtoAdquirido(String numPortariaAtoAdquirido) {
		this.numPortariaAtoAdquirido = numPortariaAtoAdquirido;
	}

	public Date getDataPublicacaoPortaria() {
		return dataPublicacaoPortaria;
	}

	public void setDataPublicacaoPortaria(Date dataPublicacaoPortaria) {
		this.dataPublicacaoPortaria = dataPublicacaoPortaria;
	}

	public String getNumProcessoOriginal() {
		return numProcessoOriginal;
	}

	public void setNumProcessoOriginal(String numProcessoOriginal) {
		this.numProcessoOriginal = numProcessoOriginal;
	}

	public Boolean getIndPossuiNumeroCAR() {
		return indPossuiNumeroCAR;
	}

	public void setIndPossuiNumeroCAR(Boolean indPossuiNumeroCAR) {
		this.indPossuiNumeroCAR = indPossuiNumeroCAR;
	}

	public Collection<NumeroCarReposicaoFlorestal> getNumeroCarReposicaoFlorestalCollection() {
		return numeroCarReposicaoFlorestalCollection;
	}

	public void setNumeroCarReposicaoFlorestalCollection(
			Collection<NumeroCarReposicaoFlorestal> numeroCarReposicaoFlorestalCollection) {
		this.numeroCarReposicaoFlorestalCollection = numeroCarReposicaoFlorestalCollection;
	}

	public Collection<NumeroCarReposicaoFlorestal> getNumeroCarReposicaoFlorestalExcluido() {
		return numeroCarReposicaoFlorestalExcluido;
	}

	public void setNumeroCarReposicaoFlorestalExcluido(
			Collection<NumeroCarReposicaoFlorestal> numeroCarReposicaoFlorestalExcluido) {
		this.numeroCarReposicaoFlorestalExcluido = numeroCarReposicaoFlorestalExcluido;
	}

	public UnidadeMedida getIdeUnidadeMedida() {
		return ideUnidadeMedida;
	}

	public void setIdeUnidadeMedida(UnidadeMedida ideUnidadeMedida) {
		this.ideUnidadeMedida = ideUnidadeMedida;
	}
	
}
