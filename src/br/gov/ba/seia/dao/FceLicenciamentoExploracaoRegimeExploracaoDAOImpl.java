package br.gov.ba.seia.dao;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.entity.FceLicenciamentoExploracaoRegimeExploracao;
import br.gov.ba.seia.entity.FceLicenciamentoMineral;

/**
 * @author eduardo.fernandes
 * @since 08/07/2016
 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceLicenciamentoExploracaoRegimeExploracaoDAOImpl {

	@Inject
	private IDAO<FceLicenciamentoExploracaoRegimeExploracao> iDAO;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceLicenciamentoExploracaoRegimeExploracao(FceLicenciamentoExploracaoRegimeExploracao fceLicenciamentoExploracaoRegimeExploracao)  {
		iDAO.salvarOuAtualizar(fceLicenciamentoExploracaoRegimeExploracao);
	}

	/**
	 *
	 * @author eduardo.fernandes
	 * @since 13/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param fceLicenciamentoMineral
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirListaFceLicenciamentoExploracaoRegimeExploracao(FceLicenciamentoMineral fceLicenciamentoMineral) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideFceLicenciamentoMineral", fceLicenciamentoMineral.getIdeFceLicenciamentoMineral());
		iDAO.executarNamedQuery("FceLicenciamentoExploracaoRegimeExploracao.removeByIdeFceLicenciamentoMineral", parameters);
	}
}
