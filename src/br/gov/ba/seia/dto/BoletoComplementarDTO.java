package br.gov.ba.seia.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;

import br.gov.ba.seia.controller.BoletoComplementarController;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.util.Util;

/**
 * TO usado na grid do {@link BoletoComplementarController}
 * 
 * @author Vitor Alexis de Almeida Leitao (vitor.leitao@zcr.com.br)
 * @since 07/11/2013
 */
public class BoletoComplementarDTO implements Serializable{
	
	private static final long serialVersionUID = 8407384437636377479L;

	private Integer id;

	private String requerente;

	private String numBoleto;

	private Date dtGeracao;

	private Date dtVencimento;

	private String status;

	private Date dtPagamentoCancelamento;

	private Date dtValidacao;

	private BigDecimal valor;
	
	private Requerimento requerimento;
	
	private Processo processo;
	
	private BoletoComplementarDetalhadoDTO detalhado;
	
	private String tipoBoleto;
	
	private BigDecimal vlrTotalCertificado;
	
	private BigDecimal vlrTotalVistoria;
	
	private Integer ideProcessoReenquadramento;
	private Boolean indBoletoRegistrado;
	/**
	 * Construtor utilizado na consulta do boleto complementar.
	 *
	 * @author Vitor Alexis de Almeida Leitao (vitor.leitao@zcr.com.br)
	 */
	
	public BoletoComplementarDTO() { }
	
	public BoletoComplementarDTO(Object[] obj) {
		
		id = ((Integer) obj[0]);
		requerente = ((String) obj[1]);
		numBoleto = ((String) obj[2]);
		dtGeracao = ((Date) obj[3]);
		dtVencimento = ((Date) obj[4]);
		status = ((String) obj[5]);
		dtPagamentoCancelamento = ((Date) obj[6]);
		dtValidacao = ((Date) obj[7]);
		valor = ((BigDecimal) obj[8]);
		
		requerimento = new Requerimento(((Integer) obj[9]));
		requerimento.setNumRequerimento(((String) obj[10]));
		
		processo = new Processo((((Integer) obj [11])));
		processo.setNumProcesso(((String) obj[12]));
		
		tipoBoleto = ((String) obj[13]);
		vlrTotalCertificado = ((BigDecimal) obj[14]);
		vlrTotalVistoria = ((BigDecimal) obj[15]);
		
		ideProcessoReenquadramento = ((Integer) obj[16]);
		indBoletoRegistrado = ((Boolean) obj[17]);
		
		detalhado = new BoletoComplementarDetalhadoDTO(dtGeracao, dtPagamentoCancelamento, dtValidacao, valor, tipoBoleto, vlrTotalCertificado, vlrTotalVistoria);
	}
	
	/**
	 * Método que retorna o valor monetário já formatado
	 *
	 * @author Vitor Alexis de Almeida Leitao (vitor.leitao@zcr.com.br)
	 * @return {@link String} já formatada com pontos e vírgula
	 */
	public String getValBoletoFormatado(){
		DecimalFormat df = Util.getDecimalFormatPtBr();
		return df.format(valor);
	}
	
	/**
	 * Método que retorna o valor monetário já formatado
	 *
	 */
	public String getVlrTotalCertificadoFormatado(){
		DecimalFormat df = Util.getDecimalFormatPtBr();
		return df.format(vlrTotalCertificado);
	}
	
	/**
	 * Método que retorna o valor monetário já formatado
	 *
	 */
	public String getVlrTotalVistoriaFormatado(){
		DecimalFormat df = Util.getDecimalFormatPtBr();
		return df.format(vlrTotalVistoria);
	}
	
	/*
	 * 
	 * 
	 * GETS AND SETS
	 * 
	 * 
	 */

	/**
	 * @return the requerente
	 */
	public String getRequerente() {

		return requerente;
	}

	/**
	 * @param requerente the requerente to set
	 */
	public void setRequerente(String requerente) {

		this.requerente = requerente;
	}

	/**
	 * @return the numBoleto
	 */
	public String getNumBoleto() {

		return numBoleto;
	}

	/**
	 * @param numBoleto the numBoleto to set
	 */
	public void setNumBoleto(String numBoleto) {

		this.numBoleto = numBoleto;
	}

	/**
	 * @return the dtGeracao
	 */
	public Date getDtGeracao() {

		return dtGeracao;
	}

	/**
	 * @param dtGeracao the dtGeracao to set
	 */
	public void setDtGeracao(Date dtGeracao) {

		this.dtGeracao = dtGeracao;
	}

	/**
	 * @return the dtVencimento
	 */
	public Date getDtVencimento() {

		return dtVencimento;
	}

	/**
	 * @param dtVencimento the dtVencimento to set
	 */
	public void setDtVencimento(Date dtVencimento) {

		this.dtVencimento = dtVencimento;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {

		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {

		this.status = status;
	}

	/**
	 * @return the dtPagamentoCancelamento
	 */
	public Date getDtPagamentoCancelamento() {

		return dtPagamentoCancelamento;
	}

	/**
	 * @param dtPagamentoCancelamento the dtPagamentoCancelamento to set
	 */
	public void setDtPagamentoCancelamento(Date dtPagamentoCancelamento) {

		this.dtPagamentoCancelamento = dtPagamentoCancelamento;
	}

	/**
	 * @return the dtValidacao
	 */
	public Date getDtValidacao() {

		return dtValidacao;
	}

	/**
	 * @param dtValidacao the dtValidacao to set
	 */
	public void setDtValidacao(Date dtValidacao) {

		this.dtValidacao = dtValidacao;
	}

	/**
	 * @return the valor
	 */
	public BigDecimal getValor() {

		return valor;
	}

	/**
	 * @param valor the valor to set
	 */
	public void setValor(BigDecimal valor) {

		this.valor = valor;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {

		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {

		this.id = id;
	}

	/**
	 * @return the detalhado
	 */
	public BoletoComplementarDetalhadoDTO getDetalhado() {
		if (detalhado == null){
			detalhado = new BoletoComplementarDetalhadoDTO(dtGeracao, dtPagamentoCancelamento, dtValidacao, valor, tipoBoleto, vlrTotalCertificado, vlrTotalVistoria);
		}
		return detalhado;
	}

	/**
	 * @param detalhado the detalhado to set
	 */
	public void setDetalhado(BoletoComplementarDetalhadoDTO detalhado) {
		
		this.detalhado = detalhado;
	}

	/**
	 * @return the requerimento
	 */
	public Requerimento getRequerimento() {
	
		return requerimento;
	}

	/**
	 * @param requerimento the requerimento to set
	 */
	public void setRequerimento(Requerimento requerimento) {
	
		this.requerimento = requerimento;
	}

	/**
	 * @return the processo
	 */
	public Processo getProcesso() {
	
		return processo;
	}

	/**
	 * @param processo the processo to set
	 */
	public void setProcesso(Processo processo) {
	
		this.processo = processo;
	}

	
	/**
	 * @return the tipoBoleto
	 */
	public String getTipoBoleto() {
	
		return tipoBoleto;
	}

	
	/**
	 * @param tipoBoleto the tipoBoleto to set
	 */
	public void setTipoBoleto(String tipoBoleto) {
	
		this.tipoBoleto = tipoBoleto;
	}

	public BigDecimal getVlrTotalCertificado() {
		return vlrTotalCertificado;
	}

	public void setVlrTotalCertificado(BigDecimal vlrTotalCertificado) {
		this.vlrTotalCertificado = vlrTotalCertificado;
	}

	public BigDecimal getVlrTotalVistoria() {
		return vlrTotalVistoria;
	}

	public void setVlrTotalVistoria(BigDecimal vlrTotalVistoria) {
		this.vlrTotalVistoria = vlrTotalVistoria;
	}

	public Integer getIdeProcessoReenquadramento() {
		return ideProcessoReenquadramento;
	}

	public void setIdeProcessoReenquadramento(Integer ideProcessoReenquadramento) {
		this.ideProcessoReenquadramento = ideProcessoReenquadramento;
	}

	public Boolean getIndBoletoRegistrado() {
		return indBoletoRegistrado;
	}

	public void setIndBoletoRegistrado(Boolean indBoletoRegistrado) {
		this.indBoletoRegistrado = indBoletoRegistrado;
	}
}