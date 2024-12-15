package br.gov.ba.seia.dto;

import br.gov.ba.seia.entity.BoletoDaeRequerimento;
import br.gov.ba.seia.entity.BoletoPagamentoRequerimento;
import br.gov.ba.seia.entity.ComprovantePagamento;
import br.gov.ba.seia.entity.ComprovantePagamentoDae;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.TramitacaoRequerimento;

public class EnviarComprovantePagamentoDTO {

	private Requerimento requerimento;
	
	private TramitacaoRequerimento tramitacaoRequerimento;
	
	private BoletoPagamentoRequerimento boleto;
	
	private BoletoDaeRequerimento vistoria;
	
	private BoletoDaeRequerimento certificado;
	
	private ComprovantePagamento comprovanteBoleto;
	
	private ComprovantePagamentoDae comprovanteVistoria;
	
	private ComprovantePagamentoDae comprovanteCertificado;

	private BoletoComplementarDTO boletoComplementarDTO;
	
	private Boolean isReenquadramento;
	
	public Requerimento getRequerimento() {
		return requerimento;
	}

	public void setRequerimento(Requerimento requerimento) {
		this.requerimento = requerimento;
	}

	public TramitacaoRequerimento getTramitacaoRequerimento() {
		return tramitacaoRequerimento;
	}

	public void setTramitacaoRequerimento(
			TramitacaoRequerimento tramitacaoRequerimento) {
		this.tramitacaoRequerimento = tramitacaoRequerimento;
	}

	public BoletoPagamentoRequerimento getBoleto() {
		return boleto;
	}

	public void setBoleto(BoletoPagamentoRequerimento boleto) {
		this.boleto = boleto;
	}

	public BoletoDaeRequerimento getVistoria() {
		return vistoria;
	}

	public void setVistoria(BoletoDaeRequerimento vistoria) {
		this.vistoria = vistoria;
	}

	public BoletoDaeRequerimento getCertificado() {
		return certificado;
	}

	public void setCertificado(BoletoDaeRequerimento certificado) {
		this.certificado = certificado;
	}

	public ComprovantePagamento getComprovanteBoleto() {
		return comprovanteBoleto;
	}

	public void setComprovanteBoleto(ComprovantePagamento comprovanteBoleto) {
		this.comprovanteBoleto = comprovanteBoleto;
	}

	public ComprovantePagamentoDae getComprovanteVistoria() {
		return comprovanteVistoria;
	}

	public void setComprovanteVistoria(
			ComprovantePagamentoDae comprovanteVistoria) {
		this.comprovanteVistoria = comprovanteVistoria;
	}

	public ComprovantePagamentoDae getComprovanteCertificado() {
		return comprovanteCertificado;
	}

	public void setComprovanteCertificado(
			ComprovantePagamentoDae comprovanteCertificado) {
		this.comprovanteCertificado = comprovanteCertificado;
	}

	public BoletoComplementarDTO getBoletoComplementarDTO() {
		return boletoComplementarDTO;
	}

	public void setBoletoComplementarDTO(BoletoComplementarDTO boletoComplementarDTO) {
		this.boletoComplementarDTO = boletoComplementarDTO;
	}

	public Boolean getIsReenquadramento() {
		return isReenquadramento;
	}

	public void setIsReenquadramento(Boolean isReenquadramento) {
		this.isReenquadramento = isReenquadramento;
	}
}
