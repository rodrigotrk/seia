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
import br.gov.ba.seia.entity.Aquifero;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class AquiferoService {

	@Inject
	private IDAO<Aquifero> aquiferoDAO;
	
	/**
	 * listar Aquifero
	 * @return
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Aquifero> listarAquifero() {
		DetachedCriteria criteria = DetachedCriteria.forClass(Aquifero.class);
		criteria.add(Restrictions.eq("indAtivo", true));
		criteria.addOrder(Order.asc("nomAquifero"));
		return aquiferoDAO.listarPorCriteria(criteria);
	}
}
