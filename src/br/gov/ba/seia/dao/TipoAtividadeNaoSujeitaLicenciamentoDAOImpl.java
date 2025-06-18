package br.gov.ba.seia.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.entity.TipoAtividadeNaoSujeitaLicenciamento;

/**
 * 
 * @author eduardo.fernandes 
 * @since 07/11/2016
 * @see <a href="http://10.105.17.77/redmine/issues/8187">#8187</a>
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoAtividadeNaoSujeitaLicenciamentoDAOImpl {

	@Inject
	private IDAO<TipoAtividadeNaoSujeitaLicenciamento> dao;
	
	/**
	 * 
	 * Método que vai retornar todos os {@link TipoAtividadeNaoSujeitaLicenciamento} inseridos no banco. 
	 * 
	 * @author eduardo.fernandes 
	 * @since 07/11/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8187">#8187</a>
	 * @return
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoAtividadeNaoSujeitaLicenciamento> listarTodosTipoAtividadeNaoSujeitaLicenciamento() {
		return dao.listarTodos();
	}
	
	/**
	 * 
	 * Método que irá listar os {@link TipoAtividadeNaoSujeitaLicenciamento} com <code>ind_ativo = true </code>.
	 * 
	 * @author eduardo.fernandes 
	 * @since 27/12/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/">#</a>
	 * @return
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoAtividadeNaoSujeitaLicenciamento> listarTipoAtividadeNaoSujeitaLicenciamentoAtivos() {
		DetachedCriteria criteria = DetachedCriteria.forClass(TipoAtividadeNaoSujeitaLicenciamento.class)
				.add(Restrictions.eq("indAtivo", true));
		return dao.listarPorCriteria(criteria, Order.asc("nomAtividade"));
	}
}
