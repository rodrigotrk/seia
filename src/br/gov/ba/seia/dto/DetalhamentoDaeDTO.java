package br.gov.ba.seia.dto;

import java.text.DecimalFormat;
import java.util.List;

import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.CumprimentoReposicaoFlorestal;
import br.gov.ba.seia.entity.PagamentoReposicaoFlorestal;
import br.gov.ba.seia.util.Util;

public class DetalhamentoDaeDTO {

	private AtoAmbiental atoAmbiental;
	
	private Double valor;
	
	private String nomTipoCumprimento;
	
	private List<MemoriaCalculoDTO> listaMemoriaCalculoDTO;
	
	private List<String> listaLegenda;
	
	private Double valorOld;
	
	private PagamentoReposicaoFlorestal pagamentoReposicaoFlorestal;
	
	private CumprimentoReposicaoFlorestal cumprimentoReposicaoFlorestal;

	public AtoAmbiental getAtoAmbiental() {
		return atoAmbiental;
	}

	public void setAtoAmbiental(AtoAmbiental atoAmbiental) {
		this.atoAmbiental = atoAmbiental;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public String getValorFormatado() {
		DecimalFormat df = Util.getDecimalFormatPtBr();
		return df.format(valor);
	}

	public List<MemoriaCalculoDTO> getListaMemoriaCalculoDTO() {
		return listaMemoriaCalculoDTO;
	}

	public void setListaMemoriaCalculoDTO(
			List<MemoriaCalculoDTO> listaMemoriaCalculoDTO) {
		this.listaMemoriaCalculoDTO = listaMemoriaCalculoDTO;
	}

	public List<String> getListaLegenda() {
		return listaLegenda;
	}

	public void setListaLegenda(List<String> listaLegenda) {
		this.listaLegenda = listaLegenda;
	}

	public String getNomTipoCumprimento() {
		return nomTipoCumprimento;
	}

	public void setNomTipoCumprimento(String nomTipoCumprimento) {
		this.nomTipoCumprimento = nomTipoCumprimento;
	}

	public Double getValorOld() {
		return valorOld;
	}

	public void setValorOld(Double valorOld) {
		this.valorOld = valorOld;
	}

	public PagamentoReposicaoFlorestal getPagamentoReposicaoFlorestal() {
		return pagamentoReposicaoFlorestal;
	}

	public void setPagamentoReposicaoFlorestal(
			PagamentoReposicaoFlorestal pagamentoReposicaoFlorestal) {
		this.pagamentoReposicaoFlorestal = pagamentoReposicaoFlorestal;
	}

	public CumprimentoReposicaoFlorestal getCumprimentoReposicaoFlorestal() {
		return cumprimentoReposicaoFlorestal;
	}

	public void setCumprimentoReposicaoFlorestal(
			CumprimentoReposicaoFlorestal cumprimentoReposicaoFlorestal) {
		this.cumprimentoReposicaoFlorestal = cumprimentoReposicaoFlorestal;
	}
	
}
