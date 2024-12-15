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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


/**
 * The persistent class for the fce_industria database table.
 * 
 */
@Entity
@Table(name="fce_industria")
public class FceIndustria implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="FCE_INDUSTRIA_IDEFCEINDUSTRIA_GENERATOR", sequenceName="FCE_INDUSTRIA_IDE_FCE_INDUSTRIA_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="FCE_INDUSTRIA_IDEFCEINDUSTRIA_GENERATOR")
	@Basic(optional = false)
	@NotNull
	@Column(name="ide_fce_industria")
	private Integer ideFceIndustria;

	@NotNull
	@JoinColumn(name="ide_fce",referencedColumnName = "ide_fce", nullable = false)
	@OneToOne(fetch = FetchType.LAZY)
	private Fce ideFce;

	@Column(name="ind_captacao_outorga")
	private Boolean indCaptacaoOutorga;

	@Column(name="ind_lancamento_outorgado")
	private Boolean indLancamentoOutorgado;

	@Column(name="ind_lancamento_regularizado")
	private Boolean indLancamentoRegularizado;

	@Column(name="num_area_construida")
	private BigDecimal numAreaConstruida;

	@Column(name="num_area_ser_construida")
	private BigDecimal numAreaSerConstruida;

	@Column(name="num_area_total")
	private BigDecimal numAreaTotal;

	@Column(name="ind_outros")
	private Boolean indOutros;
	
	@Column(name="ind_app")
	private Boolean indApp;

	public FceIndustria() {
	}

	public FceIndustria(Fce ideFce) {
		this.ideFce = ideFce;
	}

	public Integer getIdeFceIndustria() {
		return this.ideFceIndustria;
	}

	public void setIdeFceIndustria(Integer ideFceIndustria) {
		this.ideFceIndustria = ideFceIndustria;
	}

	public Boolean getIndCaptacaoOutorga() {
		return this.indCaptacaoOutorga;
	}

	public void setIndCaptacaoOutorga(Boolean indCaptacaoOutorga) {
		this.indCaptacaoOutorga = indCaptacaoOutorga;
	}

	public Boolean getIndLancamentoOutorgado() {
		return this.indLancamentoOutorgado;
	}

	public void setIndLancamentoOutorgado(Boolean indLancamentoOutorgado) {
		this.indLancamentoOutorgado = indLancamentoOutorgado;
	}

	public Boolean getIndLancamentoRegularizado() {
		return this.indLancamentoRegularizado;
	}

	public void setIndLancamentoRegularizado(Boolean indLancamentoRegularizado) {
		this.indLancamentoRegularizado = indLancamentoRegularizado;
	}

	public BigDecimal getNumAreaConstruida() {
		return this.numAreaConstruida;
	}

	public void setNumAreaConstruida(BigDecimal numAreaConstruida) {
		this.numAreaConstruida = numAreaConstruida;
	}

	public BigDecimal getNumAreaSerConstruida() {
		return this.numAreaSerConstruida;
	}

	public void setNumAreaSerConstruida(BigDecimal numAreaSerConstruida) {
		this.numAreaSerConstruida = numAreaSerConstruida;
	}

	public BigDecimal getNumAreaTotal() {
		return this.numAreaTotal;
	}

	public void setNumAreaTotal(BigDecimal numAreaTotal) {
		this.numAreaTotal = numAreaTotal;
	}

	public Boolean getIndOutros() {
		return indOutros;
	}

	public void setIndOutros(Boolean indOutros) {
		this.indOutros = indOutros;
	}

	public Fce getIdeFce() {
		return ideFce;
	}

	public void setIdeFce(Fce ideFce) {
		this.ideFce = ideFce;
	}

	public Boolean getIndApp() {
		return indApp;
	}

	public void setIndApp(Boolean indApp) {
		this.indApp = indApp;
	}
}