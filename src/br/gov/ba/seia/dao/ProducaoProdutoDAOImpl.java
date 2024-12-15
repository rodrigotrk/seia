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
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.ProducaoProduto;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

/**
 * @author eduardo.fernandes
 * @since 25/07/2016
 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ProducaoProdutoDAOImpl {

	@Inject
	private IDAO<ProducaoProduto> iDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProducaoProduto> listarProducaoProduto()  {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProducaoProduto.class)
				.createAlias("ideUnidadeMedida", "un", JoinType.LEFT_OUTER_JOIN)
				.setProjection(Projections.projectionList()
						.add(Projections.property("ideProducaoProduto"), "ideProducaoProduto")
						.add(Projections.property("nomProducaoProduto"), "nomProducaoProduto")
						.add(Projections.property("un.ideUnidadeMedida"), "ideUnidadeMedida.ideUnidadeMedida")
						.add(Projections.property("un.codUnidadeMedida"), "ideUnidadeMedida.codUnidadeMedida")
						).setResultTransformer(new AliasToNestedBeanResultTransformer(ProducaoProduto.class));
		return iDAO.listarPorCriteria(criteria, Order.asc("nomProducaoProduto"));
	}

	/**
	 * Método que retornar o {@link ProducaoProduto} Outros.
	 *
	 * @return
	 *
	 * @author eduardo.fernandes
	 * @throws Exception 
	 * @since 28/09/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8085">#8085</a> 
	 */
	public ProducaoProduto carregarOutros()  {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProducaoProduto.class).add(Restrictions.like("nomProducaoProduto", "Outros"));
		return iDAO.buscarPorCriteria(criteria);
	}

}
