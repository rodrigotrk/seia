package br.gov.ba.seia.middleware.seia.model;

import java.math.BigDecimal;
import java.util.Date;
/**
 * Classe modelo para pagamento de boleto requerimento 
 * @author 
 *
 */
public class BoletoPagamentoRequerimento {

	private Integer ideBoletoPagamentoRequerimento;

	private Date dtcVencimento;

	private BigDecimal valBoleto;

	private String numBoleto;

	private Date dtcEmissao;

	private BigDecimal boletoPagamentoRequerimento;

	private BigDecimal valBoletoOutorga;

	private Boolean indIsento;

	private Boolean indBoletoGeradoManualmente;

	private String desCaminhoBoleto;

	private Boolean indBoletoRegistrado;

	private Date dtcPagamento;

	private Requerimento ideRequerimento;

	private Pessoa idePessoa;

	private Processo ideProcesso;

	private TipoBoletoPagamento ideTipoBoletoPagamento;

	private LoteRemessaBoleto ideLoteRemessaBoleto;

	private ProcessoReenquadramento ideProcessoReenquadramento;

	public BoletoPagamentoRequerimento() {
		super();
	}

	public Integer getIdeBoletoPagamentoRequerimento() {
		return ideBoletoPagamentoRequerimento;
	}

	public void setIdeBoletoPagamentoRequerimento(Integer ideBoletoPagamentoRequerimento) {
		this.ideBoletoPagamentoRequerimento = ideBoletoPagamentoRequerimento;
	}

	public Date getDtcVencimento() {
		return dtcVencimento;
	}

	public void setDtcVencimento(Date dtcVencimento) {
		this.dtcVencimento = dtcVencimento;
	}

	public BigDecimal getValBoleto() {
		return valBoleto;
	}

	public void setValBoleto(BigDecimal valBoleto) {
		this.valBoleto = valBoleto;
	}

	public String getNumBoleto() {
		return numBoleto;
	}

	public void setNumBoleto(String numBoleto) {
		this.numBoleto = numBoleto;
	}

	public Date getDtcEmissao() {
		return dtcEmissao;
	}

	public void setDtcEmissao(Date dtcEmissao) {
		this.dtcEmissao = dtcEmissao;
	}

	public BigDecimal getBoletoPagamentoRequerimento() {
		return boletoPagamentoRequerimento;
	}

	public void setBoletoPagamentoRequerimento(BigDecimal boletoPagamentoRequerimento) {
		this.boletoPagamentoRequerimento = boletoPagamentoRequerimento;
	}

	public BigDecimal getValBoletoOutorga() {
		return valBoletoOutorga;
	}

	public void setValBoletoOutorga(BigDecimal valBoletoOutorga) {
		this.valBoletoOutorga = valBoletoOutorga;
	}

	public Boolean getIndIsento() {
		return indIsento;
	}

	public void setIndIsento(Boolean indIsento) {
		this.indIsento = indIsento;
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

	public Requerimento getIdeRequerimento() {
		return ideRequerimento;
	}

	public void setIdeRequerimento(Requerimento ideRequerimento) {
		this.ideRequerimento = ideRequerimento;
	}

	public Pessoa getIdePessoa() {
		return idePessoa;
	}

	public void setIdePessoa(Pessoa idePessoa) {
		this.idePessoa = idePessoa;
	}

	public Processo getIdeProcesso() {
		return ideProcesso;
	}

	public void setIdeProcesso(Processo ideProcesso) {
		this.ideProcesso = ideProcesso;
	}

	public TipoBoletoPagamento getIdeTipoBoletoPagamento() {
		return ideTipoBoletoPagamento;
	}

	public void setIdeTipoBoletoPagamento(TipoBoletoPagamento ideTipoBoletoPagamento) {
		this.ideTipoBoletoPagamento = ideTipoBoletoPagamento;
	}

	public LoteRemessaBoleto getIdeLoteRemessaBoleto() {
		return ideLoteRemessaBoleto;
	}

	public void setIdeLoteRemessaBoleto(LoteRemessaBoleto ideLoteRemessaBoleto) {
		this.ideLoteRemessaBoleto = ideLoteRemessaBoleto;
	}

	public ProcessoReenquadramento getIdeProcessoReenquadramento() {
		return ideProcessoReenquadramento;
	}

	public void setIdeProcessoReenquadramento(ProcessoReenquadramento ideProcessoReenquadramento) {
		this.ideProcessoReenquadramento = ideProcessoReenquadramento;
	}

}
