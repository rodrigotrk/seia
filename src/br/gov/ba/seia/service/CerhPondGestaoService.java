/**
 * 
 */
package br.gov.ba.seia.service;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.CerhPondGestao;

/**
 * @author lesantos
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhPondGestaoService {

	@Inject
	IDAO<CerhPondGestao> idao;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CerhPondGestao getCerhPondGestao() {
		DetachedCriteria criteria = DetachedCriteria.forClass(CerhPondGestao.class, "a")
				.add(Restrictions.eq("a.indExcluido", false))
				.addOrder(Order.asc("a.dtCadastro"));
		return idao.buscarPorCriteriaMaxResult(criteria);
	}
	
}
