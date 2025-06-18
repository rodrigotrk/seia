package br.gov.ba.seia.dao;

import javax.inject.Inject;

import br.gov.ba.seia.entity.TipoPermeabilidade;

public class TipoPermeabilidadeDAOImpl extends BaseDAO<TipoPermeabilidade>{

	@Inject
	IDAO<TipoPermeabilidade> tipoPermeabilidadeDAO;
	
	@Override
	protected IDAO<TipoPermeabilidade> getDao() {
		return this.tipoPermeabilidadeDAO;
	}
	
}
