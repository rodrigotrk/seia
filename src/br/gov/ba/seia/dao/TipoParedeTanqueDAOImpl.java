package br.gov.ba.seia.dao;

import javax.inject.Inject;

import br.gov.ba.seia.entity.TipoParedeTanque;

public class TipoParedeTanqueDAOImpl extends BaseDAO<TipoParedeTanque>{

	@Inject
	IDAO<TipoParedeTanque> tipoParedeTanqueDAO;
	
	@Override
	protected IDAO<TipoParedeTanque> getDao() {
		return this.tipoParedeTanqueDAO;
	}

}
