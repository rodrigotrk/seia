package br.gov.ba.seia.service;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.FceLicenciamentoMineralDAOImpl;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.FceLicenciamentoMineral;

/**
 * @author eduardo.fernandes
 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceLicenciamentoMineralService {

	@Inject
	private FceLicenciamentoMineralDAOImpl fceLicenciamentoMineralIDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FceLicenciamentoMineral buscarFceLicenciamentoMineralByFce(Fce fce)  {
		return fceLicenciamentoMineralIDAO.buscarFceLicenciamentoMineralByFce(fce);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceLicenciamentoMineral(FceLicenciamentoMineral fceLicenciamentoMineral)  {
		fceLicenciamentoMineralIDAO.salvarFceLicenciamentoMineral(fceLicenciamentoMineral);
	}
}