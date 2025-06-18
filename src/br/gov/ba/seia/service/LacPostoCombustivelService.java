package br.gov.ba.seia.service;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.BaseDAO;
import br.gov.ba.seia.dao.LacPostoCombustivelDAOImpl;
import br.gov.ba.seia.entity.LacPostoCombustivel;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class LacPostoCombustivelService extends BaseService<LacPostoCombustivel> {

	@Inject
	LacPostoCombustivelDAOImpl lacPostoCombustivelDAOImpl;
	
	public LacPostoCombustivel carregarByIdeRequerimento(Integer ideRequerimento)  {
		return this.lacPostoCombustivelDAOImpl.carregarByIdeRequerimento(ideRequerimento);
	}
	
	public Boolean hasLac(Integer ideRequerimento)  {
		return this.lacPostoCombustivelDAOImpl.hasLac(ideRequerimento);
	}
	
	@Override
	protected BaseDAO<LacPostoCombustivel> getDaoImpl() {
		return this.lacPostoCombustivelDAOImpl;
	}


}
