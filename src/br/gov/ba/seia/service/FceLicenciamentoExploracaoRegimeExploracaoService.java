package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.FceLicenciamentoExploracaoRegimeExploracaoDAOImpl;
import br.gov.ba.seia.entity.FceLicenciamentoExploracaoRegimeExploracao;
import br.gov.ba.seia.entity.FceLicenciamentoMineral;

/**
 * @author eduardo.fernandes
 * @since 07/07/2016
 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceLicenciamentoExploracaoRegimeExploracaoService {

	@Inject
	private FceLicenciamentoExploracaoRegimeExploracaoDAOImpl iDAO;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarListaFceLicenciamentoExploracaoRegimeExploracao(List<FceLicenciamentoExploracaoRegimeExploracao> lista)  {
		for(FceLicenciamentoExploracaoRegimeExploracao exploracao : lista){
			iDAO.salvarFceLicenciamentoExploracaoRegimeExploracao(exploracao);
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
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void excluirListaFceLicenciamentoExploracaoRegimeExploracao(FceLicenciamentoMineral fceLicenciamentoMineral)  {
		iDAO.excluirListaFceLicenciamentoExploracaoRegimeExploracao(fceLicenciamentoMineral);
	}

}
