package br.gov.ba.seia.dao;

import javax.inject.Inject;

import br.gov.ba.seia.entity.DistribuidoraPosto;

public class DistribuidoraPostoDAOImpl extends BaseDAO<DistribuidoraPosto>{
	
	@Inject
	IDAO<DistribuidoraPosto> distribuidoraPostoDAO; 
	
	@Override
	protected IDAO<DistribuidoraPosto> getDao() {
		return distribuidoraPostoDAO;
	}

}
