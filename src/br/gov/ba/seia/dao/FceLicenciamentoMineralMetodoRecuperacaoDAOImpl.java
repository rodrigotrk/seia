package br.gov.ba.seia.dao;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.entity.FceLicenciamentoMineral;
import br.gov.ba.seia.entity.FceLicenciamentoMineralMetodoRecuperacao;

/**
 * @author eduardo.fernandes
 * @since 08/07/2016
 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceLicenciamentoMineralMetodoRecuperacaoDAOImpl {

	@Inject
	private IDAO<FceLicenciamentoMineralMetodoRecuperacao> iDAO;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceLicenciamentoMineralMetodoRecuperacao(FceLicenciamentoMineralMetodoRecuperacao fceLicenciamentoMineralMetodoRecuperacao)  {
		iDAO.salvarOuAtualizar(fceLicenciamentoMineralMetodoRecuperacao);
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
	public void excluirListaFceLicenciamentoMineralMetodoRecuperacao(FceLicenciamentoMineral fceLicenciamentoMineral)  {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideFceLicenciamentoMineral", fceLicenciamentoMineral.getIdeFceLicenciamentoMineral());
		iDAO.executarNamedQuery("FceLicenciamentoMineralMetodoRecuperacao.removeByIdeFceLicenciamentoMineral", parameters);
	}

}
