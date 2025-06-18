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

import br.gov.ba.seia.entity.CaracteristicaIntervencaoApp;

/**
 * DAO da classe {@link CaracteristicaIntervencaoApp}.
 * 
 * @author eduardo.fernandes 
 * @since 11/01/2017
 * @see <a href="http://10.105.17.77/redmine/issues/8263">#8263</a>
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CaracteristicaIntervencaoAppDAOImpl {

	@Inject
	private IDAO<CaracteristicaIntervencaoApp> dao;

	/**
	 * Método que retorna uma lista de {@link CaracteristicaIntervencaoApp} com <blockquote> ind_excluido = false </blockquote>
	 * 
	 * @author eduardo.fernandes 
	 * @since 11/01/2017
	 * @see <a href="http://10.105.17.77/redmine/issues/8263">#8263</a>
	 * @return
	 * @throws Exception 
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CaracteristicaIntervencaoApp> listarAtivos() {
		DetachedCriteria criteria = DetachedCriteria.forClass(CaracteristicaIntervencaoApp.class)
				.add(Restrictions.eq("indExcluido", false));
		return dao.listarPorCriteria(criteria, Order.asc("ideCaracteristicaIntervencaoApp"));
	}
	
}
