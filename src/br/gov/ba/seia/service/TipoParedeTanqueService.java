package br.gov.ba.seia.service;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.BaseDAO;
import br.gov.ba.seia.dao.TipoParedeTanqueDAOImpl;
import br.gov.ba.seia.entity.TipoParedeTanque;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoParedeTanqueService extends BaseService<TipoParedeTanque>{

	@Inject
	TipoParedeTanqueDAOImpl tipoParedeTanqueDAOImpl;
	
	@Override
	protected BaseDAO<TipoParedeTanque> getDaoImpl() {
		return this.tipoParedeTanqueDAOImpl;
	}
	
	
}
