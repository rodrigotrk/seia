package br.gov.ba.seia.dto;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;

import br.gov.ba.seia.util.Util;

/**
 * Classe utilizada na grid do boleto complementar, para expandir um registro.
 * 
 * @author Vitor Alexis de Almeida Leitao (vitor.leitao@zcr.com.br)
 * @since 24/01/2014
 */
public class BoletoComplementarDetalhadoDTO {
	
	private Date dtGeracao;
	
	private Date dtPagamentoCancelamento;

	private Date dtValidacao;
	
	private BigDecimal valor;
	
	private String tipoBoleto;
	
	private BigDecimal vlrTotalCertificado;
	
	private BigDecimal vlrTotalVistoria;
	
	/**
	 * Construtor padrao.
	 */
	public BoletoComplementarDetalhadoDTO() {

		super();
	}
	
	/**
	 * Construtor padrao com parametros.
	 * 
	 * @param dtGeracao
	 * @param dtPagamentoCancelamento
	 * @param dtValidacao
	 * @param valor
	 */
	public BoletoComplementarDetalhadoDTO(Date dtGeracao, Date dtPagamentoCancelamento, Date dtValidacao, BigDecimal valor, String tipoBoleto,
			BigDecimal vlrTotalCertificado, BigDecimal vlrTotalVistoria) {

		super();
		this.dtGeracao = dtGeracao;
		this.dtPagamentoCancelamento = dtPagamentoCancelamento;
		this.dtValidacao = dtValidacao;
		this.valor = valor;
		this.tipoBoleto = tipoBoleto;
		this.vlrTotalCertificado = vlrTotalCertificado;
		this.vlrTotalVistoria = vlrTotalVistoria;
		
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

}
