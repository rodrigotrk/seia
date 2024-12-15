package br.gov.ba.seia.service;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.BaseDAO;
import br.gov.ba.seia.dao.TipoEstruturaTanqueDAOImpl;
import br.gov.ba.seia.entity.TipoEstruturaTanque;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoEstruturaTanqueService extends BaseService<TipoEstruturaTanque>{

	@Inject
	TipoEstruturaTanqueDAOImpl tipoEstruturaTanqueDAOImpl; 
	
	@Override
	protected BaseDAO<TipoEstruturaTanque> getDaoImpl() {
		return this.tipoEstruturaTanqueDAOImpl;
	}

}
