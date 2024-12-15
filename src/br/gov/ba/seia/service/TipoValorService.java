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
import br.gov.ba.seia.entity.TipoValor;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoValorService {

	@Inject
	private IDAO<TipoValor> tipoValorIDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoValor> buscarListaTipoValor() throws Exception{
		DetachedCriteria criteria = DetachedCriteria.forClass(TipoValor.class);
		criteria.add(Restrictions.eq("indAtivo", true));
		return tipoValorIDAO.listarPorCriteria(criteria, Order.asc("ideTipoValor"));
	}
}