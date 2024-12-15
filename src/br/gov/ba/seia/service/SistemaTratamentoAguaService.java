package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.SistemaTratamentoAgua;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class SistemaTratamentoAguaService {

	@Inject
	private IDAO<SistemaTratamentoAgua> idao;
	
	public List<SistemaTratamentoAgua> listarSistemaTratamentoAgua() throws Exception{
		
		DetachedCriteria criteria = DetachedCriteria.forClass(SistemaTratamentoAgua.class);
		
		criteria.add(Restrictions.eq("indAtivo", true));
		
		return idao.listarPorCriteria(criteria, Order.asc("ideSistemaTratamentoAgua"));
	}
	
}
