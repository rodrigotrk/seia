package br.gov.ba.seia.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.entity.TipoAreaConcedida;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

/**
 * @author eduardo.fernandes 
 * @since 06/03/2017
 * @see <a href="http://10.105.17.77/redmine/issues/8592">#8592</a>
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoAreaConcedidaDAOImpl {

	@Inject
	private IDAO<TipoAreaConcedida> dao;
	
	/**
	 * 
	 * @author eduardo.fernandes 
	 * @since 06/03/2017
	 * @see <a href="http://10.105.17.77/redmine/issues/8592">#8592</a>
	 * @param ideTipoAreaConcedida
	 * @return
	 * @throws Exception
	 */
	public TipoAreaConcedida buscarBy(Integer ideTipoAreaConcedida) {
		DetachedCriteria criteria = DetachedCriteria.forClass(TipoAreaConcedida.class)
				.add(Restrictions.eq("ideTipoAreaConcedida", ideTipoAreaConcedida))
				.setProjection(
						Projections.projectionList()
						.add(Projections.property("ideTipoAreaConcedida"), "ideTipoAreaConcedida")
						.add(Projections.property("desTipoAreaConcedida"), "desTipoAreaConcedida")
						)
				.setResultTransformer(new AliasToNestedBeanResultTransformer(TipoAreaConcedida.class));
		return dao.buscarPorCriteria(criteria);
	}
	
	/**
	 * 
	 * @author eduardo.fernandes 
	 * @since 06/03/2017
	 * @see <a href="http://10.105.17.77/redmine/issues/8592">#8592</a>
	 * @param ideProcessoAto
	 * @return
	 * @throws Exception
	 */
	public List<TipoAreaConcedida> listarTipoAreaConcedida() {
		return dao.listarTodos();
	}
	
}
