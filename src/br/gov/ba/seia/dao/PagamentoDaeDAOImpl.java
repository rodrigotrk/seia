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
import br.gov.ba.seia.entity.HistSituacaoDae;
import br.gov.ba.seia.entity.PagamentoDae;

public class PagamentoDaeDAOImpl {

	@Inject
	IDAO<PagamentoDae> cerhPagamentoDAO;
	
	@Inject
	IDAO<Dae> daeDAO;
	
	@Inject
	IDAO<HistSituacaoDae> histSituacaoDae;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarPagamentoDae(PagamentoDae pagamentoDae) {
		cerhPagamentoDAO.salvar(pagamentoDae);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Dae obterDae(String nossoNumero) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Dae.class);
		criteria.add(Restrictions.like("dscNossoNumero",nossoNumero));
		return this.daeDAO.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Dae> listarDaeByDataVencimento(Date dataFinal) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Dae.class);
				
		criteria.add(Restrictions.le("dtVencimento", dataFinal));
			
		return daeDAO.listarPorCriteria(criteria, Order.asc("dtVencimento"));
	}
	

	
}
