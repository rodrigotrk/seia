package br.gov.ba.seia.service;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.ReservaLegalAverbadaDAOIf;
import br.gov.ba.seia.entity.ReservaLegal;
import br.gov.ba.seia.entity.ReservaLegalAverbada;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ReservaLegalAverbadaService { 
	
	@Inject
	ReservaLegalAverbadaDAOIf daoReservaLegalAverbadaDAO;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(ReservaLegalAverbada reserva)  {
		daoReservaLegalAverbadaDAO.salvar(reserva);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizar(ReservaLegalAverbada pReservaLegalAverbada)  {
		daoReservaLegalAverbadaDAO.atualizar(pReservaLegalAverbada);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remover(ReservaLegalAverbada pReservaLegalAverbada)  {
		daoReservaLegalAverbadaDAO.remover(pReservaLegalAverbada);
	}

	public ReservaLegalAverbada carregarReservaLegalAverbada(ReservaLegal reservaLegal)  {
		return daoReservaLegalAverbadaDAO.carregarReservaLegalAverbada(reservaLegal);
	}

	
}
