package br.gov.ba.seia.service;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.FceGeracaoEnergiaDAOImpl;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.FceEnergia;


@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceGeracaoEnergiaService {

	@Inject
	private FceGeracaoEnergiaDAOImpl fceGeracaoEnergiaDAO;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FceEnergia getFceGeracaoEnergiaBy(Fce ideFce)  {
		return fceGeracaoEnergiaDAO.getFceGeracaoEnergiaBy(ideFce);
	}
	
}

