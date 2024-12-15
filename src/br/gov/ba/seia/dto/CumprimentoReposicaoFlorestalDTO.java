package br.gov.ba.seia.dto;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import br.gov.ba.seia.entity.ConsumidorReposicaoFlorestal;
import br.gov.ba.seia.entity.CumprimentoReposicaoFlorestal;
import br.gov.ba.seia.entity.DetentorReposicaoFlorestal;
import br.gov.ba.seia.entity.DevedorReposicaoFlorestal;
import br.gov.ba.seia.entity.PagamentoReposicaoFlorestal;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.TipoVolumeFlorestalRemanescente;
import br.gov.ba.seia.util.Util;

public class CumprimentoReposicaoFlorestalDTO {
	
	private CumprimentoReposicaoFlorestal cumprimentoReposicaoFlorestal;
	
	private Requerimento requerimento;

	private PagamentoReposicaoFlorestal pagamentoReposicaoFlorestal;
	
	private PagamentoReposicaoFlorestal pagamentoReposicaoFlorestalFilho;
	
	private TipoVolumeFlorestalRemanescente tipoVolumeFlorestalRemanescente;
	
	private DetentorReposicaoFlorestal detentorReposicaoFlorestal;
	
	private ConsumidorReposicaoFlorestal consumidorReposicaoFlorestal;
	
	private DevedorReposicaoFlorestal devedorReposicaoFlorestal;
	
	private Boolean indCiente;
	
	private Double valorPecuniario;
	
	private List<UnidadeMedidaCalculoDTO> unidadeMedidaCalculoDTO;
	
	private PagamentoReposicaoFlorestal pagamentoReposicaoFlorestalAnterior;

	public CumprimentoReposicaoFlorestalDTO() {
		this.cumprimentoReposicaoFlorestal = new CumprimentoReposicaoFlorestal();
		this.detentorReposicaoFlorestal = new DetentorReposicaoFlorestal();
		this.consumidorReposicaoFlorestal = new ConsumidorReposicaoFlorestal();
		this.devedorReposicaoFlorestal = new DevedorReposicaoFlorestal();
		this.unidadeMedidaCalculoDTO = new ArrayList<UnidadeMedidaCalculoDTO>();
	}
	
	public CumprimentoReposicaoFlorestalDTO(CumprimentoReposicaoFlorestal cumprimentoReposicaoFlorestal, PagamentoReposicaoFlorestal pagamentoReposicaoFlorestal, PagamentoReposicaoFlorestal pagamentoReposicaoFlorestalAnterior) {
		this.pagamentoReposicaoFlorestal = pagamentoReposicaoFlorestal;
		this.cumprimentoReposicaoFlorestal = new CumprimentoReposicaoFlorestal(cumprimentoReposicaoFlorestal.getIdeCumprimentoReposicaoFlorestal());
		this.detentorReposicaoFlorestal = new DetentorReposicaoFlorestal();
		this.consumidorReposicaoFlorestal = new ConsumidorReposicaoFlorestal();
		this.devedorReposicaoFlorestal = new DevedorReposicaoFlorestal();
		this.unidadeMedidaCalculoDTO = new ArrayList<UnidadeMedidaCalculoDTO>();
		this.pagamentoReposicaoFlorestalAnterior = pagamentoReposicaoFlorestalAnterior;
	}

	public CumprimentoReposicaoFlorestal getCumprimentoReposicaoFlorestal() {
		return cumprimentoReposicaoFlorestal;
	}

	public void setCumprimentoReposicaoFlorestal(
			CumprimentoReposicaoFlorestal cumprimentoReposicaoFlorestal) {
		this.cumprimentoReposicaoFlorestal = cumprimentoReposicaoFlorestal;
	}

	public Requerimento getRequerimento() {
		return requerimento;
	}

	public void setRequerimento(Requerimento requerimento) {
		this.requerimento = requerimento;
	}

	public PagamentoReposicaoFlorestal getPagamentoReposicaoFlorestal() {
		return pagamentoReposicaoFlorestal;
	}

	public void setPagamentoReposicaoFlorestal(
			PagamentoReposicaoFlorestal pagamentoReposicaoFlorestal) {
		this.pagamentoReposicaoFlorestal = pagamentoReposicaoFlorestal;
	}

	public PagamentoReposicaoFlorestal getPagamentoReposicaoFlorestalFilho() {
		return pagamentoReposicaoFlorestalFilho;
	}

	public void setPagamentoReposicaoFlorestalFilho(
			PagamentoReposicaoFlorestal pagamentoReposicaoFlorestalFilho) {
		this.pagamentoReposicaoFlorestalFilho = pagamentoReposicaoFlorestalFilho;
	}

	public TipoVolumeFlorestalRemanescente getTipoVolumeFlorestalRemanescente() {
		return tipoVolumeFlorestalRemanescente;
	}

	public void setTipoVolumeFlorestalRemanescente(
			TipoVolumeFlorestalRemanescente tipoVolumeFlorestalRemanescente) {
		this.tipoVolumeFlorestalRemanescente = tipoVolumeFlorestalRemanescente;
	}

	public DetentorReposicaoFlorestal getDetentorReposicaoFlorestal() {
		return detentorReposicaoFlorestal;
	}

	public void setDetentorReposicaoFlorestal(
			DetentorReposicaoFlorestal detentorReposicaoFlorestal) {
		this.detentorReposicaoFlorestal = detentorReposicaoFlorestal;
	}

	public ConsumidorReposicaoFlorestal getConsumidorReposicaoFlorestal() {
		return consumidorReposicaoFlorestal;
	}

	public void setConsumidorReposicaoFlorestal(
			ConsumidorReposicaoFlorestal consumidorReposicaoFloresta) {
		this.consumidorReposicaoFlorestal = consumidorReposicaoFloresta;
	}

	public DevedorReposicaoFlorestal getDevedorReposicaoFlorestal() {
		return devedorReposicaoFlorestal;
	}

	public void setDevedorReposicaoFlorestal(
			DevedorReposicaoFlorestal devedorReposicaoFlorestal) {
		this.devedorReposicaoFlorestal = devedorReposicaoFlorestal;
	}

	public Boolean getIndCiente() {
		return indCiente;
	}

	public void setIndCiente(Boolean indCiente) {
		this.indCiente = indCiente;
	}

	public Double getValorPecuniario() {
		return valorPecuniario;
	}
	
	public String getValorPecuniarioFormatado() throws ParseException {
		if (!Util.isNullOuVazio(valorPecuniario)) {
			DecimalFormat df = Util.getDecimalFormatPtBr(); 
			return df.format(valorPecuniario);
		}
		return "0,00";
	}
	
	public void setValorPecuniario(Double valorPecuniario) {
		this.valorPecuniario = valorPecuniario;
	}

	public List<UnidadeMedidaCalculoDTO> getUnidadeMedidaCalculoDTO() {
		return unidadeMedidaCalculoDTO;
	}

	public void setUnidadeMedidaCalculoDTO(
			List<UnidadeMedidaCalculoDTO> unidadeMedidaCalculoDTO) {
		this.unidadeMedidaCalculoDTO = unidadeMedidaCalculoDTO;
	}

	public PagamentoReposicaoFlorestal getPagamentoReposicaoFlorestalAnterior() {
		return pagamentoReposicaoFlorestalAnterior;
	}

	public void setPagamentoReposicaoFlorestalAnterior(
			PagamentoReposicaoFlorestal pagamentoReposicaoFlorestalAnterior) {
		this.pagamentoReposicaoFlorestalAnterior = pagamentoReposicaoFlorestalAnterior;
	}

}
