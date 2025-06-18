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
import br.gov.ba.seia.entity.TipoBarragem;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoBarragemService {

	@Inject
	private IDAO<TipoBarragem> tipoBarragemDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoBarragem> listarTipoBarragemByIndAtivo() throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(TipoBarragem.class);
		criteria.add(Restrictions.eq("indAtivo", true));
		return tipoBarragemDAO.listarPorCriteria(criteria, Order.asc("ideTipoBarragem"));
	}
}