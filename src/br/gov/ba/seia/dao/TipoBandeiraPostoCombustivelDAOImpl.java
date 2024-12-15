package br.gov.ba.seia.dao;

import javax.inject.Inject;

import br.gov.ba.seia.entity.TipoBandeiraPostoCombustivel;

public class TipoBandeiraPostoCombustivelDAOImpl extends BaseDAO<TipoBandeiraPostoCombustivel> {

	@Inject
	IDAO<TipoBandeiraPostoCombustivel> tipoBandeiraPostoCombustivelDAO;
	
	@Override
	protected IDAO<TipoBandeiraPostoCombustivel> getDao() {
		return this.tipoBandeiraPostoCombustivelDAO;
	}

}
