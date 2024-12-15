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
import br.gov.ba.seia.entity.TipoAquifero;


@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoAquiferoService {
	
	@Inject
	private IDAO<TipoAquifero> TipoAquiferoIDAO;
	
	/**
	 * listar tipo aquifero
	 * @return
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoAquifero> listarTipoAquifero() throws Exception{
		DetachedCriteria criteria = DetachedCriteria.forClass(TipoAquifero.class);
		criteria.add(Restrictions.eq("indAtivo", true));
		return TipoAquiferoIDAO.listarPorCriteria(criteria, Order.asc("ideTipoAquifero"));
	}
}
