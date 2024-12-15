package br.gov.ba.seia.dto;

import java.util.Date;
import java.util.List;

import br.gov.ba.seia.controller.BoletoComplementarController;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.PessoaJuridica;

/**
 * Objeto filtrante do {@link BoletoComplementarController}
 * 
 * @author Vitor Alexis de Almeida Leitao (vitor.leitao@zcr.com.br)
 * @since 08/11/2013
 */
public class BoletoComplementarFilter {

	private Integer ideProcessoReenquadramento;
	
	private Pessoa requerente;
	private List<PessoaFisica> listPF;
	private List<PessoaJuridica> listPJ;

	private String numRequerimento;

	private String numProcesso;

	private String numBoleto;
	
	private boolean emProcessamento;
	private Date dtEmProcessamentoInicial;
	private Date dtEmProcessamentoFinal;

	private boolean emitido;
	private Date dtEmitidoInicial;
	private Date dtEmitidoFinal;

	private boolean cancelamento;
	private Date dtCancelamentoInicial;
	private Date dtCancelamentoFinal;

	private boolean comprovante;
	private Date dtComprovanteInicial;
	private Date dtComprovanteFinal;

	private boolean pago;
	private Date dtPagoInicial;
	private Date dtPagoFinal;

	private boolean cancelado;
	private Date dtCanceladoInicial;
	private Date dtCanceladoFinal;

	private boolean vencido;
	private Date dtVencidoInicial;
	private Date dtVencidoFinal;
	
	private boolean pendenciaValidacaoComprovante;
	
	/**
	 * Metodo que verifica se algum status foi selecionado na tela de filtro do boleto complementar.
	 * Caso QUALQUER status tenha sido selecionado, o metodo retornara TRUE.
	 *
	 * @author Vitor Alexis de Almeida Leitao (vitor.leitao@zcr.com.br)
	 * @return TRUE para algum status selecionado, FALSE para nenhum status selecionado.
	 */
	public boolean isStatusSelecionado() {
		
		if(isEmProcessamento()) return true;
		if(isEmitido()) return true;
		if(isCancelamento()) return true;
		if(isComprovante()) return true;
		if(isPago()) return true;
		if(isCancelado()) return true;
		if(isVencido()) return true;
		if(isPendenciaValidacaoComprovante()) return true;
		
		return false;
	}

	/**
	 * 
	 * 
	 * GET'S AND SET'S
	 * 
	 * 
	 */

	/**
	 * @return the requerente
	 */
	public Pessoa getRequerente() {

		return requerente;
	}

	public Integer getIdeProcessoReenquadramento() {
		return ideProcessoReenquadramento;
	}

	public void setIdeProcessoReenquadramento(Integer ideProcessoReenquadramento) {
		this.ideProcessoReenquadramento = ideProcessoReenquadramento;
	}

	/**
	 * @param requerente the requerente to set
	 */
	public void setRequerente(Pessoa requerente) {

		this.requerente = requerente;
	}
	
	/**
	 * @return the listPF
	 */
	public List<PessoaFisica> getListPF() {
	
		return listPF;
	}

	/**
	 * @param listPF the listPF to set
	 */
	public void setListPF(List<PessoaFisica> listPF) {
	
		this.listPF = listPF;
	}

	/**
	 * @return the listPJ
	 */
	public List<PessoaJuridica> getListPJ() {
	
		return listPJ;
	}

	/**
	 * @param listPJ the listPJ to set
	 */
	public void setListPJ(List<PessoaJuridica> listPJ) {
	
		this.listPJ = listPJ;
	}

	/**
	 * @return the numRequerimento
	 */
	public String getNumRequerimento() {

		return numRequerimento;
	}

	/**
	 * @param numRequerimento the numRequerimento to set
	 */
	public void setNumRequerimento(String numRequerimento) {

		this.numRequerimento = numRequerimento;
	}

	/**
	 * @return the numProcesso
	 */
	public String getNumProcesso() {

		return numProcesso;
	}

	/**
	 * @param numProcesso the numProcesso to set
	 */
	public void setNumProcesso(String numProcesso) {

		this.numProcesso = numProcesso;
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
	 * @return the dtEmitidoInicial
	 */
	public Date getDtEmitidoInicial() {

		return dtEmitidoInicial;
	}

	/**
	 * @param dtEmitidoInicial the dtEmitidoInicial to set
	 */
	public void setDtEmitidoInicial(Date dtEmitidoInicial) {

		this.dtEmitidoInicial = dtEmitidoInicial;
	}

	/**
	 * @return the dtEmitidoFinal
	 */
	public Date getDtEmitidoFinal() {

		return dtEmitidoFinal;
	}

	/**
	 * @param dtEmitidoFinal the dtEmitidoFinal to set
	 */
	public void setDtEmitidoFinal(Date dtEmitidoFinal) {

		this.dtEmitidoFinal = dtEmitidoFinal;
	}

	/**
	 * @return the dtCancelamentoInicial
	 */
	public Date getDtCancelamentoInicial() {

		return dtCancelamentoInicial;
	}

	/**
	 * @param dtCancelamentoInicial the dtCancelamentoInicial to set
	 */
	public void setDtCancelamentoInicial(Date dtCancelamentoInicial) {

		this.dtCancelamentoInicial = dtCancelamentoInicial;
	}

	/**
	 * @return the dtCancelamentoFinal
	 */
	public Date getDtCancelamentoFinal() {

		return dtCancelamentoFinal;
	}

	/**
	 * @param dtCancelamentoFinal the dtCancelamentoFinal to set
	 */
	public void setDtCancelamentoFinal(Date dtCancelamentoFinal) {

		this.dtCancelamentoFinal = dtCancelamentoFinal;
	}

	/**
	 * @return the dtComprovanteInicial
	 */
	public Date getDtComprovanteInicial() {

		return dtComprovanteInicial;
	}

	/**
	 * @param dtComprovanteInicial the dtComprovanteInicial to set
	 */
	public void setDtComprovanteInicial(Date dtComprovanteInicial) {

		this.dtComprovanteInicial = dtComprovanteInicial;
	}

	/**
	 * @return the dtComprovanteFinal
	 */
	public Date getDtComprovanteFinal() {

		return dtComprovanteFinal;
	}

	/**
	 * @param dtComprovanteFinal the dtComprovanteFinal to set
	 */
	public void setDtComprovanteFinal(Date dtComprovanteFinal) {

		this.dtComprovanteFinal = dtComprovanteFinal;
	}

	/**
	 * @return the dtPagoInicial
	 */
	public Date getDtPagoInicial() {

		return dtPagoInicial;
	}

	/**
	 * @param dtPagoInicial the dtPagoInicial to set
	 */
	public void setDtPagoInicial(Date dtPagoInicial) {

		this.dtPagoInicial = dtPagoInicial;
	}

	/**
	 * @return the dtPagoFinal
	 */
	public Date getDtPagoFinal() {

		return dtPagoFinal;
	}

	/**
	 * @param dtPagoFinal the dtPagoFinal to set
	 */
	public void setDtPagoFinal(Date dtPagoFinal) {

		this.dtPagoFinal = dtPagoFinal;
	}

	/**
	 * @return the dtCanceladoInicial
	 */
	public Date getDtCanceladoInicial() {

		return dtCanceladoInicial;
	}

	/**
	 * @param dtCanceladoInicial the dtCanceladoInicial to set
	 */
	public void setDtCanceladoInicial(Date dtCanceladoInicial) {

		this.dtCanceladoInicial = dtCanceladoInicial;
	}

	/**
	 * @return the dtCanceladoFinal
	 */
	public Date getDtCanceladoFinal() {

		return dtCanceladoFinal;
	}

	/**
	 * @param dtCanceladoFinal the dtCanceladoFinal to set
	 */
	public void setDtCanceladoFinal(Date dtCanceladoFinal) {

		this.dtCanceladoFinal = dtCanceladoFinal;
	}

	/**
	 * @return the dtVencidoInicial
	 */
	public Date getDtVencidoInicial() {

		return dtVencidoInicial;
	}

	/**
	 * @param dtVencidoInicial the dtVencidoInicial to set
	 */
	public void setDtVencidoInicial(Date dtVencidoInicial) {

		this.dtVencidoInicial = dtVencidoInicial;
	}

	/**
	 * @return the dtVencidoFinal
	 */
	public Date getDtVencidoFinal() {

		return dtVencidoFinal;
	}

	/**
	 * @param dtVencidoFinal the dtVencidoFinal to set
	 */
	public void setDtVencidoFinal(Date dtVencidoFinal) {

		this.dtVencidoFinal = dtVencidoFinal;
	}

	/**
	 * @return the emitido
	 */
	public boolean isEmitido() {

		return emitido;
	}

	/**
	 * @param emitido the emitido to set
	 */
	public void setEmitido(boolean emitido) {

		this.emitido = emitido;
	}

	/**
	 * @return the cancelamento
	 */
	public boolean isCancelamento() {

		return cancelamento;
	}

	/**
	 * @param cancelamento the cancelamento to set
	 */
	public void setCancelamento(boolean cancelamento) {

		this.cancelamento = cancelamento;
	}

	/**
	 * @return the comprovante
	 */
	public boolean isComprovante() {

		return comprovante;
	}

	/**
	 * @param comprovante the comprovante to set
	 */
	public void setComprovante(boolean comprovante) {

		this.comprovante = comprovante;
	}

	/**
	 * @return the pago
	 */
	public boolean isPago() {

		return pago;
	}

	/**
	 * @param pago the pago to set
	 */
	public void setPago(boolean pago) {

		this.pago = pago;
	}

	/**
	 * @return the cancelado
	 */
	public boolean isCancelado() {

		return cancelado;
	}

	/**
	 * @param cancelado the cancelado to set
	 */
	public void setCancelado(boolean cancelado) {

		this.cancelado = cancelado;
	}

	/**
	 * @return the vencido
	 */
	public boolean isVencido() {

		return vencido;
	}

	/**
	 * @param vencido the vencido to set
	 */
	public void setVencido(boolean vencido) {

		this.vencido = vencido;
	}

	public boolean isEmProcessamento() {
		return emProcessamento;
	}

	public void setEmProcessamento(boolean emProcessamento) {
		this.emProcessamento = emProcessamento;
	}

	public Date getDtEmProcessamentoInicial() {
		return dtEmProcessamentoInicial;
	}

	public void setDtEmProcessamentoInicial(Date dtEmProcessamentoInicial) {
		this.dtEmProcessamentoInicial = dtEmProcessamentoInicial;
	}

	public Date getDtEmProcessamentoFinal() {
		return dtEmProcessamentoFinal;
	}

	public void setDtEmProcessamentoFinal(Date dtEmProcessamentoFinal) {
		this.dtEmProcessamentoFinal = dtEmProcessamentoFinal;
	}
	
	public boolean isPendenciaValidacaoComprovante() {
		return pendenciaValidacaoComprovante;
	}
	
	public void setPendenciaValidacaoComprovante(boolean pendenciaValidacaoComprovante) {
		this.pendenciaValidacaoComprovante = pendenciaValidacaoComprovante;
	}
}