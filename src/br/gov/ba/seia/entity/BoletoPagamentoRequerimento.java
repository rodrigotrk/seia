package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import br.gov.ba.seia.enumerator.StatusBoletoEnum;
import br.gov.ba.seia.util.FileUploadUtil;
import br.gov.ba.seia.util.Util;
import flexjson.JSON;

@Entity
@Table(name = "boleto_pagamento_requerimento")
@NamedQueries({ 
	@NamedQuery(name = "BoletoPagamentoRequerimento.findAll", query = "SELECT b FROM BoletoPagamentoRequerimento b"),
	@NamedQuery(name = "BoletoPagamentoRequerimento.findById", query = "SELECT b FROM BoletoPagamentoRequerimento b WHERE b.ideBoletoPagamentoRequerimento = :ideBoletoPagamentoRequerimento"),
	@NamedQuery(name = "BoletoPagamentoRequerimento.findByIdeProcesso", query = "SELECT b FROM BoletoPagamentoRequerimento b WHERE b.ideProcesso.ideProcesso = :ideProcesso"),
	@NamedQuery(name = "BoletoPagamentoRequerimento.findByNumeroBoleto", query = "SELECT b FROM BoletoPagamentoRequerimento b WHERE b.numBoleto = :numBoleto") })
public class BoletoPagamentoRequerimento implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ide_boleto_pagamento_requerimento_seq")
	@SequenceGenerator(name = "ide_boleto_pagamento_requerimento_seq", sequenceName = "ide_boleto_pagamento_requerimento_seq", allocationSize = 1)
	@Column(name = "ide_boleto_pagamento_requerimento")
	private Integer ideBoletoPagamentoRequerimento;

	@Column(name = "dtc_vencimento", nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcVencimento;
	
	@Column(name = "val_boleto", nullable = false, precision = 15, scale = 4)
	private BigDecimal valBoleto;
	
	@Column(name = "num_boleto")
	private String numBoleto;
	
	@Column(name = "dtc_emissao", nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcEmissao;
	
	@Column(name = "boleto_pagamento_requerimento")
	private BigDecimal boletoPagamentoRequerimento;
	
	@Column(name = "val_boleto_outorga")
	private BigDecimal valBoletoOutorga;
	
	@Column(name = "ind_isento")
	private Boolean indIsento;
	
	@Column(name = "ind_boleto_gerado_manualmente")
	private Boolean indBoletoGeradoManualmente;
	
	@Column(name = "des_caminho_boleto")
	private String desCaminhoBoleto;

	@Column(name = "ind_boleto_registrado")
	private Boolean indBoletoRegistrado;

	@Column(name = "dtc_pagamento", nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcPagamento;
	
	@JoinColumn(name = "ide_requerimento", referencedColumnName = "ide_requerimento")
	@OneToOne(optional = true)
	private Requerimento ideRequerimento;
	
	@JoinColumn(name = "ide_pessoa", referencedColumnName = "ide_pessoa")
	@ManyToOne(optional = false)
	private Pessoa idePessoa;
	
	@JoinColumn(name = "ide_motivo_isencao_boleto", referencedColumnName = "ide_motivo_isencao_boleto")
	@ManyToOne
	private MotivoIsencaoBoleto ideMotivoIsencaoBoleto;
	
	@JoinColumn(name = "ide_processo", referencedColumnName = "ide_processo")
	@OneToOne(optional = true)
	private Processo ideProcesso;
	
	@JoinColumn(name = "ide_tipo_boleto_pagamento", referencedColumnName = "ide_tipo_boleto_pagamento")
	@ManyToOne(optional = false)
	private TipoBoletoPagamento ideTipoBoletoPagamento;
	
	@JoinColumn(name = "ide_lote_remessa_boleto", referencedColumnName = "ide_lote_remessa_boleto")
	@ManyToOne
	private LoteRemessaBoleto ideLoteRemessaBoleto;
	
	@JoinColumn(name = "ide_lote_retorno_boleto", referencedColumnName = "ide_lote_retorno_boleto")
	@ManyToOne
	private LoteRetornoBoleto ideLoteRetornoBoleto;
	
	@JoinColumn(name = "ide_processo_reenquadramento", referencedColumnName = "ide_processo_reenquadramento")
	@ManyToOne(optional = true)
	private ProcessoReenquadramento ideProcessoReenquadramento;
	
	@OneToMany(mappedBy = "ideBoletoPagamentoRequerimento")
	private Collection<DetalhamentoBoleto> detalhamentoBoletoCollection;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "ideBoletoPagamentoRequerimento")
	private Collection<ComprovantePagamento> comprovantePagamentoCollection;
	
	@OneToMany(mappedBy = "ideBoletoPagamento")
	private Collection<BoletoPagamentoHistorico> boletoPagamentoHistoricoCollection;

	@Transient
	private String pathComprovante;
	
	@Transient
	private StatusBoletoPagamento statusBoletoPagamento;
	
	@Transient
	private BigDecimal valTotalBoleto;
	
	@Transient
	private boolean indComprovanteValidado;
	
	public BoletoPagamentoRequerimento() {
		super();
	}

	public BoletoPagamentoRequerimento(Integer ideBoletoPagamentoRequerimento) {
		this.ideBoletoPagamentoRequerimento = ideBoletoPagamentoRequerimento;
	}

	public BoletoPagamentoRequerimento(Integer ideBoletoPagamentoRequerimento, Date dtcVencimento, BigDecimal valBoleto, Date dtcEmissao) {
		this.ideBoletoPagamentoRequerimento = ideBoletoPagamentoRequerimento;
		this.dtcVencimento = dtcVencimento;
		this.valBoleto = valBoleto;
		this.dtcEmissao = dtcEmissao;
	}
	
	@JSON(include = false)
	public String getDtcVencimentoFormatada() {
		if(!Util.isNullOuVazio(dtcVencimento)){
			SimpleDateFormat spf = new SimpleDateFormat("dd/MM/yyyy");  
			return spf.format(dtcVencimento);
		}else
			return new String("");
	}
	
	@JSON(include = false)
	public String getValBoletoFormatado(){
		DecimalFormat df = Util.getDecimalFormatPtBr();
		return df.format(valBoleto);
	}

	@JSON(include = false)
	public String getDtcEmissaoFormatada() {
		if(!Util.isNullOuVazio(dtcEmissao)){
			SimpleDateFormat spf = new SimpleDateFormat("dd/MM/yyyy");  
			return spf.format(dtcEmissao);
		}else
			return new String("");
	}

	@JSON(include = false)
	public boolean isZerado() {
		return !Util.isNull(valBoletoOutorga) && valBoletoOutorga == new BigDecimal(0) && !Util.isNull(valBoleto)
				&& valBoleto == new BigDecimal(0);
	}
	
	@JSON(include = false)
	public String getNomeArquivo() {
		return FileUploadUtil.getFileName(pathComprovante);
	}

	@JSON(include = false)
	public BigDecimal getValTotalBoleto() {
		if(Util.isNullOuVazio(valBoletoOutorga))
			valTotalBoleto = valBoleto;
		else
			valTotalBoleto = valBoleto.add(valBoletoOutorga);
		return valTotalBoleto;
	}
	
	@JSON(include = false)
	public Boolean getCancelado() {
		return statusBoletoPagamento.getNomStatusBoletoPagamento().equalsIgnoreCase(StatusBoletoEnum.CANCELADO.getValue());
	}
	
	@JSON(include = false)
	public Boolean getEmitido() {
		return statusBoletoPagamento.getNomStatusBoletoPagamento().equalsIgnoreCase(StatusBoletoEnum.EMITIDO.getValue());
	}
	
	@JSON(include = false)
	public Boolean getCancelamentoSolicitado() {
		return statusBoletoPagamento.getNomStatusBoletoPagamento().equalsIgnoreCase(StatusBoletoEnum.CANCELAMENTO_SOLICITADO.getValue());
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (ideBoletoPagamentoRequerimento != null ? ideBoletoPagamentoRequerimento.hashCode() : 0);
		return hash;
	}
	
	@Override
	public boolean equals(Object object) {
		
		
		if (!(object instanceof BoletoPagamentoRequerimento)) {
			return false;
		}
		BoletoPagamentoRequerimento other = (BoletoPagamentoRequerimento) object;
		if ((this.ideBoletoPagamentoRequerimento == null && other.ideBoletoPagamentoRequerimento != null)
				|| (this.ideBoletoPagamentoRequerimento != null && !this.ideBoletoPagamentoRequerimento
						.equals(other.ideBoletoPagamentoRequerimento))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "br.gov.ba.seia.entity.BoletoPagamentoRequerimento[ ideBoletoPagamentoRequerimento="
				+ ideBoletoPagamentoRequerimento + " ]";
	}
	
	/************************
	 *						* 
	 * XXX: GETS AND SETS	*
	 * 				 		*
	 ************************/
	
	@JSON(include = false)
	public Integer getIdeBoletoPagamentoRequerimento() {
		return ideBoletoPagamentoRequerimento;
	}

	public void setIdeBoletoPagamentoRequerimento(Integer ideBoletoPagamentoRequerimento) {
		this.ideBoletoPagamentoRequerimento = ideBoletoPagamentoRequerimento;
	}

	@JSON(include = false)
	public Date getDtcVencimento() {
		return dtcVencimento;
	}

	public void setDtcVencimento(Date dtcVencimento) {
		this.dtcVencimento = dtcVencimento;
	}

	@JSON(include = false)
	public BigDecimal getValBoleto() {
		return valBoleto;
	}
	
	public void setValBoleto(BigDecimal valBoleto) {
		this.valBoleto = valBoleto;
	}

	public void setValBoletoFormatado(BigDecimal valBoleto) {
		this.valBoleto = valBoleto;
	}

	public String getNumBoleto() {
		return numBoleto;
	}

	public void setNumBoleto(String numBoleto) {
		this.numBoleto = numBoleto;
	}

	@JSON(include = false)
	public Date getDtcEmissao() {
		return dtcEmissao;
	}

	public void setDtcEmissao(Date dtcEmissao) {
		this.dtcEmissao = dtcEmissao;
	}

	@JSON(include = false)
	public BigDecimal getBoletoPagamentoRequerimento() {
		return boletoPagamentoRequerimento;
	}

	public void setBoletoPagamentoRequerimento(BigDecimal boletoPagamentoRequerimento) {
		this.boletoPagamentoRequerimento = boletoPagamentoRequerimento;
	}

	@JSON(include = false)
	public BigDecimal getValBoletoOutorga() {
		return valBoletoOutorga;
	}

	public void setValBoletoOutorga(BigDecimal valBoletoOutorga) {
		this.valBoletoOutorga = valBoletoOutorga;
	}

	@JSON(include = false)
	public Boolean getIndIsento() {
		return indIsento;
	}

	public void setIndIsento(Boolean indIsento) {
		this.indIsento = indIsento;
	}

	@JSON(include = false)
	public Collection<DetalhamentoBoleto> getDetalhamentoBoletoCollection() {
		return detalhamentoBoletoCollection;
	}

	public void setDetalhamentoBoletoCollection(Collection<DetalhamentoBoleto> detalhamentoBoletoCollection) {
		this.detalhamentoBoletoCollection = detalhamentoBoletoCollection;
	}

	@JSON(include = false)
	public Collection<ComprovantePagamento> getComprovantePagamentoCollection() {
		return comprovantePagamentoCollection;
	}

	public void setComprovantePagamentoCollection(Collection<ComprovantePagamento> comprovantePagamentoCollection) {
		this.comprovantePagamentoCollection = comprovantePagamentoCollection;
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
	public MotivoIsencaoBoleto getIdeMotivoIsencaoBoleto() {
		return ideMotivoIsencaoBoleto;
	}

	public void setIdeMotivoIsencaoBoleto(MotivoIsencaoBoleto ideMotivoIsencaoBoleto) {
		this.ideMotivoIsencaoBoleto = ideMotivoIsencaoBoleto;
	}

	@JSON(include = false)
	public String getPathComprovante() {
		return pathComprovante;
	}

	public void setPathComprovante(String pathComprovante) {
		this.pathComprovante = pathComprovante;
	}
	
	public String getValTotalBoletoFormatado() {
		DecimalFormat df = Util.getDecimalFormatPtBr();
		return df.format(getValTotalBoleto());
	}

	public void setValTotalBoleto(BigDecimal valTotalBoleto) {
		this.valTotalBoleto = valTotalBoleto;
	}

	@JSON(include = false)
	public Processo getIdeProcesso() {
		return ideProcesso;
	}

	public void setIdeProcesso(Processo ideProcesso) {
		this.ideProcesso = ideProcesso;
	}

	@JSON(include = false)
	public TipoBoletoPagamento getIdeTipoBoletoPagamento() {
		return ideTipoBoletoPagamento;
	}

	public void setIdeTipoBoletoPagamento(TipoBoletoPagamento ideTipoBoletoPagamento) {
		this.ideTipoBoletoPagamento = ideTipoBoletoPagamento;
	}

	@JSON(include = false)
	public Collection<BoletoPagamentoHistorico> getBoletoPagamentoHistoricoCollection() {
		return boletoPagamentoHistoricoCollection;
	}
	
	@JSON(include = false)
	public List<BoletoPagamentoHistorico> getBoletoPagamentoHistoricoCollectionAtList() {
		return (List<BoletoPagamentoHistorico>)boletoPagamentoHistoricoCollection;
	}

	public void setBoletoPagamentoHistoricoCollection(Collection<BoletoPagamentoHistorico> boletoPagamentoHistoricoCollection) {
		this.boletoPagamentoHistoricoCollection = boletoPagamentoHistoricoCollection;
	}

	public ProcessoReenquadramento getIdeProcessoReenquadramento() {
		return ideProcessoReenquadramento;
	}

	public void setIdeProcessoReenquadramento(ProcessoReenquadramento ideProcessoReenquadramento) {
		this.ideProcessoReenquadramento = ideProcessoReenquadramento;
	}

	@JSON(include = false)
	public StatusBoletoPagamento getStatusBoletoPagamento() {
		return statusBoletoPagamento;
	}
	
	public void setStatusBoletoPagamento(StatusBoletoPagamento statusBoletoPagamento) {
		this.statusBoletoPagamento = statusBoletoPagamento;
	}

	public Boolean getIndBoletoGeradoManualmente() {
		return indBoletoGeradoManualmente;
	}

	public void setIndBoletoGeradoManualmente(Boolean indBoletoGeradoManualmente) {
		this.indBoletoGeradoManualmente = indBoletoGeradoManualmente;
	}

	public String getDesCaminhoBoleto() {
		return desCaminhoBoleto;
	}

	public void setDesCaminhoBoleto(String desCaminhoBoleto) {
		this.desCaminhoBoleto = desCaminhoBoleto;
	}

	public LoteRemessaBoleto getIdeLoteRemessaBoleto() {
		return ideLoteRemessaBoleto;
	}

	public void setIdeLoteRemessaBoleto(LoteRemessaBoleto ideLoteRemessaBoleto) {
		this.ideLoteRemessaBoleto = ideLoteRemessaBoleto;
	}

	public LoteRetornoBoleto getIdeLoteRetornoBoleto() {
		return ideLoteRetornoBoleto;
	}

	public void setIdeLoteRetornoBoleto(LoteRetornoBoleto ideLoteRetornoBoleto) {
		this.ideLoteRetornoBoleto = ideLoteRetornoBoleto;
	}

	public Boolean getIndBoletoRegistrado() {
		return indBoletoRegistrado;
	}

	public void setIndBoletoRegistrado(Boolean indBoletoRegistrado) {
		this.indBoletoRegistrado = indBoletoRegistrado;
	}

	public Date getDtcPagamento() {
		return dtcPagamento;
	}

	public void setDtcPagamento(Date dtcPagamento) {
		this.dtcPagamento = dtcPagamento;
	}
	
	public boolean isIndComprovanteValidado() {
		return indComprovanteValidado;
	}

	public void setIndComprovanteValidado(boolean indComprovanteValidado) {
		this.indComprovanteValidado = indComprovanteValidado;
	}
}
