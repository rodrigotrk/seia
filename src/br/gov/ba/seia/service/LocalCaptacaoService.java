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
import br.gov.ba.seia.entity.LocalCaptacao;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class LocalCaptacaoService {

	@Inject
	private IDAO<LocalCaptacao> localCaptacaoIDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<LocalCaptacao> buscarListaLocalCaptacao() {
		DetachedCriteria criteria = DetachedCriteria.forClass(LocalCaptacao.class);
		criteria.add(Restrictions.eq("indAtivo", true));
		return localCaptacaoIDAO.listarPorCriteria(criteria, Order.asc("ideLocalCaptacao"));
	}
}