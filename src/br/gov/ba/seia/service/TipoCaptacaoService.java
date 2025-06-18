package br.gov.ba.seia.service;
/**
 * 		15/05/14
 * @author eduardo.fernandes
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

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.TipoCaptacao;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoCaptacaoService {

	@Inject
	private IDAO<TipoCaptacao> tipoCaptacaoIDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoCaptacao> listarTipoCaptacaoCompleto() throws Exception{
		DetachedCriteria criteria = DetachedCriteria.forClass(TipoCaptacao.class);
		return tipoCaptacaoIDAO.listarPorCriteria(criteria, Order.asc("ideTipoCaptacao"));
	}
}
