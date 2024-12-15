package br.gov.ba.seia.service;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.BaseDAO;
import br.gov.ba.seia.dao.TipoBandeiraPostoCombustivelDAOImpl;
import br.gov.ba.seia.entity.TipoBandeiraPostoCombustivel;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoBandeiraPostoCombustivelService extends BaseService<TipoBandeiraPostoCombustivel>{

	@Inject
	TipoBandeiraPostoCombustivelDAOImpl tipoBandeiraPostoCombustivelDAOImpl;
	
	@Override
	protected BaseDAO<TipoBandeiraPostoCombustivel> getDaoImpl() {
		return this.tipoBandeiraPostoCombustivelDAOImpl;
	}

}
