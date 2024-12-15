package br.gov.ba.seia.dto;

import java.util.Collection;

import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.ReservaLegal;

public class RelocacaoReservaLegalDTO {

	private ImovelRural imovelRural;
	private ReservaLegal reservaLegalPoligonalConcedida;
	private ReservaLegal reservaLegalPoligonalSelecionada;
	private Collection<ReservaLegal> listaPoligonalRequerimento;
	private Collection<ReservaLegal> listaPoligonalNotificacao;

	public ImovelRural getImovelRural() {
		return imovelRural;
	}

	public void setImovelRural(ImovelRural imovelRural) {
		this.imovelRural = imovelRural;
	}

	public Collection<ReservaLegal> getListaPoligonalRequerimento() {
		return listaPoligonalRequerimento;
	}

	public void setListaPoligonalRequerimento(
			Collection<ReservaLegal> listaPoligonalRequerimento) {
		this.listaPoligonalRequerimento = listaPoligonalRequerimento;
	}

	public Collection<ReservaLegal> getListaPoligonalNotificacao() {
		return listaPoligonalNotificacao;
	}

	public void setListaPoligonalNotificacao(
			Collection<ReservaLegal> listaPoligonalNotificacao) {
		this.listaPoligonalNotificacao = listaPoligonalNotificacao;
	}
	
	public ReservaLegal getReservaLegalPoligonalConcedida() {
		return reservaLegalPoligonalConcedida;
	}

	public void setReservaLegalPoligonalConcedida(
			ReservaLegal reservaLegalPoligonalConcedida) {
		this.reservaLegalPoligonalConcedida = reservaLegalPoligonalConcedida;
	}

	public ReservaLegal getReservaLegalPoligonalSelecionada() {
		return reservaLegalPoligonalSelecionada;
	}

	public void setReservaLegalPoligonalSelecionada(
			ReservaLegal reservaLegalPoligonalSelecionada) {
		this.reservaLegalPoligonalSelecionada = reservaLegalPoligonalSelecionada;
	}

}