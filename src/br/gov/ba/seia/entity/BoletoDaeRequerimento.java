package br.gov.ba.seia.entity;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.gov.ba.seia.entity.generic.AbstractEntity;
import br.gov.ba.seia.util.FileUploadUtil;
import br.gov.ba.seia.util.Util;
import flexjson.JSON;

@Entity
@Table(name = "boleto_dae_requerimento")
@NamedQueries({ 
	@NamedQuery(name = "BoletoDaeRequerimento.findAll", query = "SELECT b FROM BoletoDaeRequerimento b"),
	@NamedQuery(name = "BoletoDaeRequerimento.findByIdeRequerimento", query = "SELECT b FROM BoletoDaeRequerimento b WHERE b.ideRequerimento.ideRequerimento = :ideRequerimento"),
	@NamedQuery(name = "BoletoDaeRequerimento.findByIdeProcesso", query = "SELECT b FROM BoletoDaeRequerimento b WHERE b.ideProcesso.ideProcesso = :ideProcesso")})
public class BoletoDaeRequerimento extends AbstractEntity implements Cloneable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "ide_boleto_dae_requerimento")
	private Integer ideBoletoDaeRequerimento;
	
	@Column(name = "vlr_area_vistoria")
	private BigDecimal vlrAreaVistoria;
	
	@Basic(optional = false)
	@Column(name = "vlr_total_certificado")
	private BigDecimal vlrTotalCertificado;
	
	@Basic(optional = false)
	@Column(name = "vlr_total_vistoria")
	private BigDecimal vlrTotalVistoria;
	
	@Basic(optional = false)
	@Column(name = "ind_isento")
	private boolean indIsento;
	
	@JoinColumn(name = "ide_motivo_isencao_boleto", referencedColumnName = "ide_motivo_isencao_boleto")
	@ManyToOne
	private MotivoIsencaoBoleto ideMotivoIsencaoBoleto;
	
	@JoinColumn(name = "ide_requerimento", referencedColumnName = "ide_requerimento")
	@OneToOne(optional = false)
	private Requerimento ideRequerimento;
	
	@JoinColumn(name = "ide_pessoa", referencedColumnName = "ide_pessoa")
	@ManyToOne(optional = false)
	private Pessoa idePessoa;
	
	@JoinColumn(name = "ide_tipo_boleto_pagamento", referencedColumnName = "ide_tipo_boleto_pagamento", nullable=false)
	@ManyToOne(optional = false)
	private TipoBoletoPagamento ideTipoBoletoPagamento;
	
	@JoinColumn(name = "ide_processo", referencedColumnName = "ide_processo", nullable=false)
	@ManyToOne(optional = false)
	private Processo ideProcesso;
	
	@JoinColumn(name = "ide_processo_reenquadramento", referencedColumnName = "ide_processo_reenquadramento", nullable=false)
	@ManyToOne(optional = false)
	private ProcessoReenquadramento ideProcessoReenquadramento;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "ideBoletoDaeRequerimento")
	private Collection<ComprovantePagamentoDae> comprovantePagamentoDaeCollection;
	
	@OneToMany(mappedBy = "ideBoletoDaeRequerimento")
	private Collection<DetalhamentoBoleto> detalhamentoBoletoCollection;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "ideBoletoDaeRequerimento")
	private Collection<BoletoDaeHistorico> boletoDaeHistorico;
	
	@Transient
	private String vlrTotalVistoriaFormatado;
	
	@Transient
	private String vlrTotalCertificadoFormatado;
	
	@Transient 
	private String pathComprovante;
	
	@Transient
	private boolean indComprovanteValidado;
	
	public BoletoDaeRequerimento() {}

	public BoletoDaeRequerimento(Integer ideBoletoDaeRequerimento) {
		this.ideBoletoDaeRequerimento = ideBoletoDaeRequerimento;
	}

	public BoletoDaeRequerimento(Integer ideBoletoDaeRequerimento, BigDecimal vlrTotalCertificado, BigDecimal vlrTotalVistoria) {
		this.ideBoletoDaeRequerimento = ideBoletoDaeRequerimento;
		this.vlrTotalCertificado = vlrTotalCertificado;
		this.vlrTotalVistoria = vlrTotalVistoria;
	}
	
	public String getVlrTotalVistoriaFormatado() {
		DecimalFormat df = Util.getDecimalFormatPtBr();
		if(!Util.isNullOuVazio(vlrTotalVistoria)){
			vlrTotalVistoriaFormatado = df.format(vlrTotalVistoria);
		}
		return vlrTotalVistoriaFormatado;
	}
	
	public String getVlrTotalCertificadoFormatado() {
		DecimalFormat df = Util.getDecimalFormatPtBr();
		vlrTotalCertificadoFormatado = df.format(vlrTotalCertificado);
		return vlrTotalCertificadoFormatado;
	}

	@JSON(include = false)
	public String getNomeArquivo(){
		return FileUploadUtil.getFileName(pathComprovante);
	}
	
	public BoletoDaeRequerimento clone() throws CloneNotSupportedException{
		return (BoletoDaeRequerimento) super.clone();
	}
	
	/************************
	 *						* 
	 * XXX: GETS AND SETS	*
	 * 				 		*
	 ************************/

	@JSON(include = false)
	public Integer getIdeBoletoDaeRequerimento() {
		return ideBoletoDaeRequerimento;
	}

	public void setIdeBoletoDaeRequerimento(Integer ideBoletoDaeRequerimento) {
		this.ideBoletoDaeRequerimento = ideBoletoDaeRequerimento;
	}

	@JSON(include = false)
	public BigDecimal getVlrAreaVistoria() {
		return vlrAreaVistoria;
	}

	public void setVlrAreaVistoria(BigDecimal vlrAreaVistoria) {
		this.vlrAreaVistoria = vlrAreaVistoria;
	}

	@JSON(include = false)
	public BigDecimal getVlrTotalCertificado() {
		return vlrTotalCertificado;
	}

	public void setVlrTotalCertificado(BigDecimal vlrTotalCertificado) {
		this.vlrTotalCertificado = vlrTotalCertificado;
	}

	@JSON(include = false)
	public BigDecimal getVlrTotalVistoria() {
		return vlrTotalVistoria;
	}

	public void setVlrTotalVistoria(BigDecimal vlrTotalVistoria) {
		this.vlrTotalVistoria = vlrTotalVistoria;
	}

	@JSON(include = false)
	public Collection<ComprovantePagamentoDae> getComprovantePagamentoDaeCollection() {
		return comprovantePagamentoDaeCollection;
	}

	public void setComprovantePagamentoDaeCollection(Collection<ComprovantePagamentoDae> comprovantePagamentoDaeCollection) {
		this.comprovantePagamentoDaeCollection = comprovantePagamentoDaeCollection;
	}

	@JSON(include = false)
	public Requerimento getIdeRequerimento() {
		return ideRequerimento;
	}

	public void setIdeRequerimento(Requerimento ideRequerimento) {
		this.ideRequerimento = ideRequerimento;
	}

	@JSON(include = false)
	public Pessoa getIdePessoa() {
		return idePessoa;
	}

	public void setIdePessoa(Pessoa idePessoa) {
		this.idePessoa = idePessoa;
	}

	@JSON(include = false)
	public Collection<DetalhamentoBoleto> getDetalhamentoBoletoCollection() {
		return detalhamentoBoletoCollection;
	}

	public void setDetalhamentoBoletoCollection(Collection<DetalhamentoBoleto> detalhamentoBoletoCollection) {
		this.detalhamentoBoletoCollection = detalhamentoBoletoCollection;
	}

	public void setVlrTotalVistoriaFormatado(String vlrTotalVistoriaFormatado) {
		this.vlrTotalVistoriaFormatado = vlrTotalVistoriaFormatado;
	}

	public void setVlrTotalCertificadoFormatado(String vlrTotalCertificadoFormatado) {
		this.vlrTotalCertificadoFormatado = vlrTotalCertificadoFormatado;
	}
	
	public TipoBoletoPagamento getIdeTipoBoletoPagamento() {
		return ideTipoBoletoPagamento;
	}

	public void setIdeTipoBoletoPagamento(TipoBoletoPagamento ideTipoBoletoPagamento) {
		this.ideTipoBoletoPagamento = ideTipoBoletoPagamento;
	}

	public Collection<BoletoDaeHistorico> getBoletoDaeHistorico() {
		return boletoDaeHistorico;
	}

	public void setBoletoDaeHistorico(Collection<BoletoDaeHistorico> boletoDaeHistorico) {
		this.boletoDaeHistorico = boletoDaeHistorico;
	}

	@JSON(include = false)
	public String getPathComprovante() {
		return pathComprovante;
	}

	public void setPathComprovante(String pathComprovante) {
		this.pathComprovante = pathComprovante;
	}

	public Processo getIdeProcesso() {
		return ideProcesso;
	}

	public void setIdeProcesso(Processo ideProcesso) {
		this.ideProcesso = ideProcesso;
	}

	public ProcessoReenquadramento getIdeProcessoReenquadramento() {
		return ideProcessoReenquadramento;
	}

	public void setIdeProcessoReenquadramento(ProcessoReenquadramento ideProcessoReenquadramento) {
		this.ideProcessoReenquadramento = ideProcessoReenquadramento;
	}

	public boolean isIndComprovanteValidado() {
		return indComprovanteValidado;
	}

	public void setIndComprovanteValidado(boolean indComprovanteValidado) {
		this.indComprovanteValidado = indComprovanteValidado;
	}

	public boolean isIndIsento() {
		return indIsento;
	}

	public void setIndIsento(boolean indIsento) {
		this.indIsento = indIsento;
	}

	public MotivoIsencaoBoleto getIdeMotivoIsencaoBoleto() {
		return ideMotivoIsencaoBoleto;
	}

	public void setIdeMotivoIsencaoBoleto(MotivoIsencaoBoleto ideMotivoIsencaoBoleto) {
		this.ideMotivoIsencaoBoleto = ideMotivoIsencaoBoleto;
	}
}
