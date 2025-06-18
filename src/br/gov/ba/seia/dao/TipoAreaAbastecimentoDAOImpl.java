package br.gov.ba.seia.dao;

import javax.inject.Inject;

import br.gov.ba.seia.entity.TipoAreaAbastecimento;

public class TipoAreaAbastecimentoDAOImpl extends BaseDAO<TipoAreaAbastecimento> {

	@Inject
	IDAO<TipoAreaAbastecimento> tipoAreaAbastecimento;
	
	@Override
	protected IDAO<br.gov.ba.seia.entity.TipoAreaAbastecimento> getDao() {
		return this.tipoAreaAbastecimento;
	}
	
}
