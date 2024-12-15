/**
 * 		04/02/14
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
import br.gov.ba.seia.entity.TipologiaIrrigacao;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipologiaIrrigacaoService {

	@Inject
	private IDAO<TipologiaIrrigacao> tipologiaIrrigacaoIDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipologiaIrrigacao> listarTipologiaIrrigacaoByIndAtivo() throws Exception{
		DetachedCriteria criteria = DetachedCriteria.forClass(TipologiaIrrigacao.class);
		criteria.add(Restrictions.eq("indAtivo", true));
		return tipologiaIrrigacaoIDAO.listarPorCriteria(criteria, Order.asc("ideTipologiaIrrigacao"));
	}
}