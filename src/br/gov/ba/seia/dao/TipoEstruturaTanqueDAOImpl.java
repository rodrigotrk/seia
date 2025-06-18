package br.gov.ba.seia.dao;

import javax.inject.Inject;

import br.gov.ba.seia.entity.TipoEstruturaTanque;

public class TipoEstruturaTanqueDAOImpl extends BaseDAO<TipoEstruturaTanque> {
	
	@Inject
	private IDAO<TipoEstruturaTanque> tipoEstruturaTanqueDAO;
	
	@Override
	protected IDAO<TipoEstruturaTanque> getDao() {
		return this.tipoEstruturaTanqueDAO;
	}

}
