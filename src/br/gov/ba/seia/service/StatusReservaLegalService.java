package br.gov.ba.seia.service;

import java.util.Collection;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.StatusReservaLegal;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class StatusReservaLegalService {

	@Inject
	IDAO<StatusReservaLegal> daoStatusReservaLegal;
	@EJB
	TramitacaoRequerimentoService tramitacaoRequerimento;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<StatusReservaLegal> listarStatusReservaLegal() {
		return daoStatusReservaLegal.listarTodos();
	}

}