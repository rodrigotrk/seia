package br.gov.ba.seia.service;

import java.util.Collection;

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
import br.gov.ba.seia.entity.SistemaCoordenada;

/**
 * @author mario.junior
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class DatumService {

	@Inject
	private IDAO<SistemaCoordenada> daoDatum;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<SistemaCoordenada> listarDatum() {
		return daoDatum.listarTodos();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<SistemaCoordenada> listarDatumSemUTM() {
		DetachedCriteria criteria = DetachedCriteria.forClass(SistemaCoordenada.class);
		criteria.add(Restrictions.or(Restrictions.eq("ideSistemaCoordenada", 1), Restrictions.eq("ideSistemaCoordenada", 4)));
		return daoDatum.listarPorCriteria(criteria, Order.asc("ideSistemaCoordenada"));
	}
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<SistemaCoordenada> listarDatumSirgas2000() {
		DetachedCriteria criteria = DetachedCriteria.forClass(SistemaCoordenada.class);
		criteria.add(Restrictions.eq("ideSistemaCoordenada", 4));
		return daoDatum.listarPorCriteria(criteria, Order.asc("ideSistemaCoordenada"));
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<SistemaCoordenada> listarDatumSirgas2000comUTM() {
		DetachedCriteria criteria = DetachedCriteria.forClass(SistemaCoordenada.class);
		criteria.add(Restrictions.in("ideSistemaCoordenada", new Integer[] {4,5,6}));
		return daoDatum.listarPorCriteria(criteria, Order.asc("ideSistemaCoordenada"));
	}
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public SistemaCoordenada carregar(Integer ideDatum){
		return daoDatum.carregarGet(ideDatum);
	}

}
