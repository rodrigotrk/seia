package br.gov.ba.seia.dao;

import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.entity.ReservaLegal;
import br.gov.ba.seia.entity.ReservaLegalAverbada;

public class ReservaLegalAverbadaDAOImpl implements ReservaLegalAverbadaDAOIf{
	
	@Inject
	IDAO<ReservaLegalAverbada> daoReservaLegalAverbada;
	
	
	public void salvar(ReservaLegalAverbada reserva) {
		 daoReservaLegalAverbada.salvar(reserva);
	}
	
	public void atualizar(ReservaLegalAverbada pReservaLegalAverbada) {
		daoReservaLegalAverbada.atualizar(pReservaLegalAverbada);
	}

	public ReservaLegalAverbada carregarReservaLegalAverbada(ReservaLegal reservaLegal) {
		DetachedCriteria criteria =  DetachedCriteria.forClass(ReservaLegalAverbada.class);
		criteria.add(Restrictions.eq("ideReservaLegal.ideReservaLegal", reservaLegal.getIdeReservaLegal()));
		return daoReservaLegalAverbada.buscarPorCriteria(criteria);
	}
	
	public void remover(ReservaLegalAverbada pReservaLegalAverbada) {
		daoReservaLegalAverbada.remover(pReservaLegalAverbada);
	}
	
}
