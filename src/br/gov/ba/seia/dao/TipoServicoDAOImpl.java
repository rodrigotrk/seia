package br.gov.ba.seia.dao;

import javax.inject.Inject;

import br.gov.ba.seia.entity.TipoServicoPosto;

public class TipoServicoDAOImpl extends BaseDAO<TipoServicoPosto> {

	@Inject
	IDAO<TipoServicoPosto> daoTipoServicoPosto;

	@Override
	protected IDAO<TipoServicoPosto> getDao() {
		return this.daoTipoServicoPosto;
	}
	
	
	
}
