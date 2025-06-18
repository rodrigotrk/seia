package br.gov.ba.seia.dto;

import java.util.Collection;

import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.ReservaLegal;
/**
 * Classe de integração entre as classes
 * @author 
 *
 */
public class AutorizacaoManejoCabrucaDTO {

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
	/**
	 * 
	 * @return lista de poligonal do requerimento
	 */
	public Collection<ReservaLegal> getListaPoligonalRequerimento() {
		return listaPoligonalRequerimento;
	}
	/**
	 * 
	 * @param listaPoligonalRequerimento
	 */
	public void setListaPoligonalRequerimento(
			Collection<ReservaLegal> listaPoligonalRequerimento) {
		this.listaPoligonalRequerimento = listaPoligonalRequerimento;
	}
	/**
	 * 
	 * @return lista da poligonal notificada
	 */
	public Collection<ReservaLegal> getListaPoligonalNotificacao() {
		return listaPoligonalNotificacao;
	}
	/**
	 * 
	 * @param listaPoligonalNotificacao
	 */
	public void setListaPoligonalNotificacao(
			Collection<ReservaLegal> listaPoligonalNotificacao) {
		this.listaPoligonalNotificacao = listaPoligonalNotificacao;
	}
	/**
	 * 
	 * @return
	 */
	public ReservaLegal getReservaLegalPoligonalConcedida() {
		return reservaLegalPoligonalConcedida;
	}
	/**
	 * 
	 * @param reservaLegalPoligonalConcedida
	 */
	public void setReservaLegalPoligonalConcedida(
			ReservaLegal reservaLegalPoligonalConcedida) {
		this.reservaLegalPoligonalConcedida = reservaLegalPoligonalConcedida;
	}
	/**
	 * 
	 * @return
	 */
	public ReservaLegal getReservaLegalPoligonalSelecionada() {
		return reservaLegalPoligonalSelecionada;
	}
	/**
	 * 
	 * @param reservaLegalPoligonalSelecionada
	 */
	public void setReservaLegalPoligonalSelecionada(
			ReservaLegal reservaLegalPoligonalSelecionada) {
		this.reservaLegalPoligonalSelecionada = reservaLegalPoligonalSelecionada;
	}

}