package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.FceLicenciamentoMineralSistemaTratamentoDAOImpl;
import br.gov.ba.seia.entity.FceLicenciamentoMineral;
import br.gov.ba.seia.entity.FceLicenciamentoMineralSistemaTratamento;

/**
 * @author eduardo.fernandes
 * @since 08/07/2016
 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceLicenciamentoMineralSistemaTratamentoService {

	@Inject
	private FceLicenciamentoMineralSistemaTratamentoDAOImpl iDAO;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarListaFceLicenciamentoMineralSistemaTratamento(List<FceLicenciamentoMineralSistemaTratamento> lista) {
		for(FceLicenciamentoMineralSistemaTratamento sistemaTratamento : lista){
			iDAO.salvarFceLicenciamentoMineralSistemaTratamento(sistemaTratamento);
		}
	}

	/**
	 *
	 * @author eduardo.fernandes
	 * @since 13/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param fceLicenciamentoMineral
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirListaFceLicenciamentoMineralSistemaTratamento(FceLicenciamentoMineral fceLicenciamentoMineral)  {
		iDAO.excluirListaFceLicenciamentoMineralSistemaTratamento(fceLicenciamentoMineral);
	}

}
