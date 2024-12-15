/**
 * 		07/01/14
 * @author eduardo.fernandes
 */
package br.gov.ba.seia.service;

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

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.DominioBarramento;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class DominioBarramentoService {

	@Inject
	private IDAO<DominioBarramento> dominioBarramentoIDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<DominioBarramento> buscarListaDominioBarramento() {
		DetachedCriteria criteria = DetachedCriteria.forClass(DominioBarramento.class);
		criteria.add(Restrictions.eq("indAtivo", true));
		return dominioBarramentoIDAO.listarPorCriteria(criteria, Order.asc("ideDominioBarramento"));
	}
}