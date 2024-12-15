package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.ProducaoProdutoDAOImpl;
import br.gov.ba.seia.entity.ProducaoProduto;

/**
 * @author eduardo.fernandes
 * @since 25/07/2016
 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ProducaoProdutoService {

	@Inject
	private ProducaoProdutoDAOImpl iDAO;

	/**
	 *
	 * @author eduardo.fernandes
	 * @since 25/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @return
	 * @
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProducaoProduto> listarProducaoProduto()  {
		return iDAO.listarProducaoProduto();
	}
	
	/**
	 * Método que retornar o {@link ProducaoProduto} Outros.
	 *
	 * @return
	 *
	 * @author eduardo.fernandes
	 * @ 
	 * @since 28/09/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8085">#8085</a> 
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ProducaoProduto carregarProducaoProdutoOutros()  {
		return iDAO.carregarOutros();
	}

}
