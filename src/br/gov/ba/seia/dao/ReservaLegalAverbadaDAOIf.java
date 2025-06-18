package br.gov.ba.seia.dao;

import br.gov.ba.seia.entity.ReservaLegal;
import br.gov.ba.seia.entity.ReservaLegalAverbada;

public interface ReservaLegalAverbadaDAOIf {
	public void salvar(ReservaLegalAverbada reserva) ;
	
	public void atualizar(ReservaLegalAverbada pReservaLegalAverbada) ;
	
	public ReservaLegalAverbada carregarReservaLegalAverbada(ReservaLegal reservaLegal) ;
	
	public void remover(ReservaLegalAverbada pReservaLegalAverbada) ;
}
