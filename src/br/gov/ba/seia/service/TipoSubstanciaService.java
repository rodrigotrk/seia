package br.gov.ba.seia.service;
/**
 * 		09/05/14
 * @author marcelo.brito
 */

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
import br.gov.ba.seia.entity.TipoSubstancia;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoSubstanciaService {

	@Inject
	private IDAO<TipoSubstancia> tipoSubstanciaIDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoSubstancia> buscarListaTipoSubstancia() throws Exception{
		DetachedCriteria criteria = DetachedCriteria.forClass(TipoSubstancia.class);
		criteria.add(Restrictions.eq("indAtivo", true));
		return tipoSubstanciaIDAO.listarPorCriteria(criteria, Order.asc("ideTipoSubstancia"));
	}
}
