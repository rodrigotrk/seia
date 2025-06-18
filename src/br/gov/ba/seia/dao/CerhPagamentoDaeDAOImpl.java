package br.gov.ba.seia.dao;

import java.util.Date;
import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.entity.Dae;
import br.gov.ba.seia.entity.PagamentoDae;

public class CerhPagamentoDaeDAOImpl {

	@Inject
	IDAO<PagamentoDae> cerhPagamentoDAO;
	
	@Inject
	IDAO<Dae> cerhDaeDAO;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarCerhPagamentoDae(PagamentoDae cerhPagamentoDae){
		cerhPagamentoDAO.salvar(cerhPagamentoDae);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Dae obterCerhCnae(String nossoNumero) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Dae.class);
		criteria.add(Restrictions.like("dscNossoNumero",nossoNumero));
		return this.cerhDaeDAO.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Dae> listarCerhCnaeByDataVencimento(Date dataInicial, Date dataFinal){
		DetachedCriteria criteria = DetachedCriteria.forClass(Dae.class);
		criteria.add(Restrictions.between("dtVencimento", dataInicial, dataFinal));
		return this.cerhDaeDAO.listarPorCriteria(criteria, Order.asc("dtVencimento"));
	}
	
}
