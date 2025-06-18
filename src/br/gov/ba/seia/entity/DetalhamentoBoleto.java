package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Collection;
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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import br.gov.ba.seia.util.Util;

@Entity
@Table(name = "detalhamento_boleto")
@NamedQueries({ @NamedQuery(name = "DetalhamentoBoleto.findAll", query = "SELECT d FROM DetalhamentoBoleto d") })
public class DetalhamentoBoleto implements Serializable, Cloneable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "ide_detalhamento_boleto")
	private Integer ideDetalhamentoBoleto;
	
	@Column(name = "valor")
	private BigDecimal valor;
	
	@Column(name = "ide_ufir")
	private Integer ideUfir;
	
	@Column(name = "num_ufir")
	private BigDecimal numUfir;
	
	@Basic(optional = false)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dtc_criacao")
	private Date dtcCriacao;
	
	@Column(name = "vlr_licenca_anterior")
	private Double valorLicencaAnterior;
	
	@Column(name = "vlr_area_captacao")
	private Double areaCaptacao;
	
	@ManyToOne
	@JoinColumn(name = "ide_tipologia", referencedColumnName = "ide_tipologia")
	private Tipologia ideTipologia;
	
	@ManyToOne
	@JoinColumn(name = "ide_tipo_finalidade_uso_agua", referencedColumnName = "ide_tipo_finalidade_uso_agua")
	private TipoFinalidadeUsoAgua ideTipoFinalidadeUsoAgua;
	
	@ManyToOne
	@JoinColumn(name = "ide_boleto_pagamento_requerimento", referencedColumnName = "ide_boleto_pagamento_requerimento")
	private BoletoPagamentoRequerimento ideBoletoPagamentoRequerimento;
	
	@ManyToOne
	@JoinColumn(name = "ide_boleto_dae_requerimento", referencedColumnName = "ide_boleto_dae_requerimento")
	private BoletoDaeRequerimento ideBoletoDaeRequerimento;
	
	@ManyToOne
	@JoinColumn(name = "ide_ato_ambiental", referencedColumnName = "ide_ato_ambiental")
	private AtoAmbiental ideAtoAmbiental;
	
	@OneToMany(mappedBy = "ideDetalhamentoBoleto", fetch = FetchType.LAZY)
	private Collection<DetalhamentoBoletoFinalidade> detalhamentoBoletoFinalidadeCollection;

	@Transient
	private boolean exigeCalculo;
	
	@Transient
	private boolean dae;
	
	@Transient
	private boolean existeArea;
	
	@Transient
	private String valorLicencaAnteriorString;
	
	@Transient
	private String areaCaptacaoString;
	
	@Transient
	private BigDecimal valorCertificado;
	
	@Transient
	private BigDecimal valorvistoria;
	
	@Transient
	private BigDecimal areaVistoriada;
	
	@Transient
	private BigDecimal numVazao;

	@Transient
	private Collection<ParametroCalculo> parametros;
	
	@Transient
	private Collection<BigDecimal> vazoesCaptacao;
	
	@Transient
	private Collection<TipoCriadouroFauna> tipoCriadouroFaunaCollection;

	public DetalhamentoBoleto() {}

	public DetalhamentoBoleto(AtoAmbiental ideAtoAmbiental, Tipologia ideTipologia) {
		this.ideAtoAmbiental = ideAtoAmbiental;
		this.ideTipologia = ideTipologia;
	}

	public DetalhamentoBoleto(Integer ideDetalhamentoBoleto) {
		this.ideDetalhamentoBoleto = ideDetalhamentoBoleto;
	}

	public DetalhamentoBoleto(Integer ideDetalhamentoBoleto, Date dtcCriacao) {
		this.ideDetalhamentoBoleto = ideDetalhamentoBoleto;
		this.dtcCriacao = dtcCriacao;
	}

	public Integer getIdeDetalhamentoBoleto() {
		return ideDetalhamentoBoleto;
	}

	public void setIdeDetalhamentoBoleto(Integer ideDetalhamentoBoleto) {
		this.ideDetalhamentoBoleto = ideDetalhamentoBoleto;
	}

	public BigDecimal getValor() {
		return valor;
	}
	
	public Boolean getValorIsZeroOuNull() {
		if(!Util.isNullOuVazio(valor)){
			if(valor.doubleValue() == 0)
				return true;
			else
				return false;
		}else
			return true;
	}
	
	public String getValorFormatado() {
		DecimalFormat df = Util.getDecimalFormatPtBr();
		return df.format(valor);
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Integer getIdeUfir() {
		return ideUfir;
	}

	public void setIdeUfir(Integer ideUfir) {
		this.ideUfir = ideUfir;
	}

	public BigDecimal getNumUfir() {
		return numUfir;
	}

	public void setNumUfir(BigDecimal numUfir) {
		this.numUfir = numUfir;
	}

	public Date getDtcCriacao() {
		return dtcCriacao;
	}

	public void setDtcCriacao(Date dtcCriacao) {
		this.dtcCriacao = dtcCriacao;
	}

	public Tipologia getIdeTipologia() {
		return ideTipologia;
	}

	public void setIdeTipologia(Tipologia ideTipologia) {
		this.ideTipologia = ideTipologia;
	}

	public TipoFinalidadeUsoAgua getIdeTipoFinalidadeUsoAgua() {
		return ideTipoFinalidadeUsoAgua;
	}

	public void setIdeTipoFinalidadeUsoAgua(TipoFinalidadeUsoAgua ideTipoFinalidadeUsoAgua) {
		this.ideTipoFinalidadeUsoAgua = ideTipoFinalidadeUsoAgua;
	}

	public BoletoPagamentoRequerimento getIdeBoletoPagamentoRequerimento() {
		return ideBoletoPagamentoRequerimento;
	}

	public void setIdeBoletoPagamentoRequerimento(BoletoPagamentoRequerimento ideBoletoPagamentoRequerimento) {
		this.ideBoletoPagamentoRequerimento = ideBoletoPagamentoRequerimento;
	}

	public BoletoDaeRequerimento getIdeBoletoDaeRequerimento() {
		return ideBoletoDaeRequerimento;
	}

	public void setIdeBoletoDaeRequerimento(BoletoDaeRequerimento ideBoletoDaeRequerimento) {
		this.ideBoletoDaeRequerimento = ideBoletoDaeRequerimento;
	}

	public AtoAmbiental getIdeAtoAmbiental() {
		return ideAtoAmbiental;
	}

	public void setIdeAtoAmbiental(AtoAmbiental ideAtoAmbiental) {
		this.ideAtoAmbiental = ideAtoAmbiental;
	}

	public boolean isExigeCalculo() {
		return exigeCalculo;
	}

	public void setExigeCalculo(boolean exigeCalculo) {
		this.exigeCalculo = exigeCalculo;
	}

	public boolean isDae() {
		return dae;
	}

	public void setDae(boolean dae) {
		this.dae = dae;
	}

	public BigDecimal getValorCertificado() {
		return valorCertificado;
	}
	
	public String getValorCertificadoFormatado() {
		DecimalFormat df = Util.getDecimalFormatPtBr();
		return df.format(valorCertificado);
	}


	public void setValorCertificado(BigDecimal valorCertificado) {
		this.valorCertificado = valorCertificado;
	}

	public BigDecimal getValorvistoria() {
		return valorvistoria;
	}

	public void setValorVistoria(BigDecimal valorvistoria) {
		this.valorvistoria = valorvistoria;
	}

	public BigDecimal getAreaVistoriada() {
		return areaVistoriada;
	}

	public void setAreaVistoriada(BigDecimal areaVistoriada) {
		this.areaVistoriada = areaVistoriada;
	}

	public boolean isExisteArea() {
		return existeArea;
	}

	public void setExisteArea(boolean existeArea) {
		this.existeArea = existeArea;
	}

	public Double getAreaCaptacao() {
		if(Util.isNullOuVazio(areaCaptacaoString))
			areaCaptacao = 0D;
		else 
			areaCaptacao = Util.formatarValor(areaCaptacaoString);
			
		return areaCaptacao;
	}

	public void setAreaCaptacao(Double areaCaptacao) {
		this.areaCaptacao = areaCaptacao;
	}

	public Double getValorLicencaAnterior() {
		if(Util.isNullOuVazio(valorLicencaAnteriorString))
			valorLicencaAnterior = 0D;
		else 
			valorLicencaAnterior = Util.formatarValor(valorLicencaAnteriorString);
			
		return valorLicencaAnterior;
	}

	public void setValorLicencaAnterior(Double valorLicencaAnterior) {
		this.valorLicencaAnterior = valorLicencaAnterior;
	}
	
	public Collection<ParametroCalculo> getParametros() {
		return parametros;
	}

	public void setParametros(Collection<ParametroCalculo> parametros) {
		this.parametros = parametros;
	}

	public void setValorvistoria(BigDecimal valorvistoria) {
		this.valorvistoria = valorvistoria;
	}

	public String getValorLicencaAnteriorString() {
		return valorLicencaAnteriorString;
	}

	public void setValorLicencaAnteriorString(String valorLicencaAnteriorString) {
		this.valorLicencaAnteriorString = valorLicencaAnteriorString;
	}

	public String getAreaCaptacaoString() {
		return areaCaptacaoString;
	}

	public void setAreaCaptacaoString(String areaCaptacaoString) {
		this.areaCaptacaoString = areaCaptacaoString;
	}

	@Override
	public String toString() {
		return ideDetalhamentoBoleto.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideAtoAmbiental == null) ? 0 : ideAtoAmbiental.hashCode());
		result = prime * result + ((ideTipoFinalidadeUsoAgua == null) ? 0 : ideTipoFinalidadeUsoAgua.hashCode());
		result = prime * result + ((ideTipologia == null) ? 0 : ideTipologia.hashCode());
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
		DetalhamentoBoleto other = (DetalhamentoBoleto) obj;
		if (ideAtoAmbiental == null) {
			if (other.ideAtoAmbiental != null)
				return false;
		} else if (!ideAtoAmbiental.equals(other.ideAtoAmbiental))
			return false;
		if (ideTipoFinalidadeUsoAgua == null) {
			if (other.ideTipoFinalidadeUsoAgua != null)
				return false;
		} else if (!ideTipoFinalidadeUsoAgua.equals(other.ideTipoFinalidadeUsoAgua))
			return false;
		if (ideTipologia == null) {
			if (other.ideTipologia != null)
				return false;
		} else if (!ideTipologia.equals(other.ideTipologia))
			return false;
		return true;
	}
	
	public DetalhamentoBoleto clone() throws CloneNotSupportedException {
		return (DetalhamentoBoleto) super.clone();
	}

	public BigDecimal getNumVazao() {
		return numVazao;
	}

	public void setNumVazao(BigDecimal numVazao) {
		this.numVazao = numVazao;
	}

	public Collection<BigDecimal> getVazoesCaptacao() {
		return vazoesCaptacao;
	}

	public void setVazoesCaptacao(Collection<BigDecimal> vazoesCaptacao) {
		this.vazoesCaptacao = vazoesCaptacao;
	}

	public Collection<TipoCriadouroFauna> getTipoCriadouroFaunaCollection() {
		return tipoCriadouroFaunaCollection;
	}

	public void setTipoCriadouroFaunaCollection(
			Collection<TipoCriadouroFauna> tipoCriadouroFaunaCollection) {
		this.tipoCriadouroFaunaCollection = tipoCriadouroFaunaCollection;
	}

	public Collection<DetalhamentoBoletoFinalidade> getDetalhamentoBoletoFinalidadeCollection() {
		return detalhamentoBoletoFinalidadeCollection;
	}

	public void setDetalhamentoBoletoFinalidadeCollection(
			Collection<DetalhamentoBoletoFinalidade> detalhamentoBoletoFinalidadeCollection) {
		this.detalhamentoBoletoFinalidadeCollection = detalhamentoBoletoFinalidadeCollection;
	}
}