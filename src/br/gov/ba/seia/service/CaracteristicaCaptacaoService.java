/**
 * 
 * @author rodrigo.santos
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
import br.gov.ba.seia.entity.CaracteristicaCaptacao;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CaracteristicaCaptacaoService {

	@Inject
	private IDAO<CaracteristicaCaptacao> caracteristicaCaptacaoIDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CaracteristicaCaptacao> buscarListaCaracteristicaCaptacao() {
		DetachedCriteria criteria = DetachedCriteria.forClass(CaracteristicaCaptacao.class);
		criteria.add(Restrictions.eq("indAtivo", true));
		return caracteristicaCaptacaoIDAO.listarPorCriteria(criteria, Order.asc("ideCaracteristicaCaptacao"));
	}
}