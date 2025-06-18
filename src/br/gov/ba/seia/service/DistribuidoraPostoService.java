package br.gov.ba.seia.service;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.BaseDAO;
import br.gov.ba.seia.dao.DistribuidoraPostoDAOImpl;
import br.gov.ba.seia.entity.DistribuidoraPosto;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class DistribuidoraPostoService extends BaseService<DistribuidoraPosto>{

	@Inject
	DistribuidoraPostoDAOImpl distribuidoraPostoDAOImpl;
	
	@Override
	protected BaseDAO<DistribuidoraPosto> getDaoImpl() {
		return this.distribuidoraPostoDAOImpl;
	}
}
