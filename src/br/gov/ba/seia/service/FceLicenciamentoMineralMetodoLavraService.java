package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.FceLicenciamentoMineralMetodoLavraDAOImpl;
import br.gov.ba.seia.entity.FceLicenciamentoMineral;
import br.gov.ba.seia.entity.FceLicenciamentoMineralMetodoLavra;

/**
 * @author eduardo.fernandes
 * @since 08/07/2016
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceLicenciamentoMineralMetodoLavraService {

	@Inject
	private FceLicenciamentoMineralMetodoLavraDAOImpl iDAO;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarListaFceLicenciamentoMineralMetodoLavra(List<FceLicenciamentoMineralMetodoLavra> lista)  {
		for(FceLicenciamentoMineralMetodoLavra metodoLavra : lista){
			iDAO.salvarFceLicenciamentoMineralMetodoLavra(metodoLavra);
		}
	}

	/**
	 *
	 * @author eduardo.fernandes
	 * @since 13/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param fceLicenciamentoMineral
	 * @
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirListaFceLicenciamentoMineralMetodoLavra(FceLicenciamentoMineral fceLicenciamentoMineral)  {
		iDAO.excluirListaFceLicenciamentoMineralMetodoLavra(fceLicenciamentoMineral);
	}

}
