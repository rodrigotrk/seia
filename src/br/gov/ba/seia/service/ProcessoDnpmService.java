package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.ProcessoDnpmDAOImpl;
import br.gov.ba.seia.entity.FceLicenciamentoMineral;
import br.gov.ba.seia.entity.ProcessoDnpm;

/**
 * @author eduardo.fernandes, alexandre.queiroz
 * @since 08/07/2016
 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ProcessoDnpmService {

	@Inject
	private ProcessoDnpmDAOImpl processoDnpmDAO;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarListaProcessoDnpm(List<ProcessoDnpm> lista)  {
		processoDnpmDAO.salvarProcessoDnpm(lista);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(ProcessoDnpm processo)  {
		processoDnpmDAO.salvar(processo);
	}

	/**
	 * ADICIONAR COMENTÁRIO
	 * 
	 * @author eduardo.fernandes
	 * @since 11/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param object
	 * @return
	 * @
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProcessoDnpm> listarProcessoDnpmBy(Object object)  {
		return processoDnpmDAO.listarProcessoDnpmBy(object);
	}

	/**
	 *  ADICIONAR COMENTÁRIO
	 * 
	 * @author eduardo.fernandes
	 * @since 13/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param fceLicenciamentoMineral
	 * 
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirListaProcessoDnpm(FceLicenciamentoMineral fceLicenciamentoMineral)  {
		processoDnpmDAO.excluirListaProcessoDnpm(fceLicenciamentoMineral);
	}
	
	

	/**
	 * 
	 * @author alexandre.queiroz
	 * @since 13/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7703">#7703</a>
	 * @param fcePesquisaMineral
	 * @
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirProcessoDnpmByIdeProcessoDnpm(ProcessoDnpm ideProcessoDnpm)  {
		processoDnpmDAO.excluirProcessoDnpmByIdeProcessoDnpm(ideProcessoDnpm);
	}
}
