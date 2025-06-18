package br.gov.ba.seia.dao;

import javax.inject.Inject;

import br.gov.ba.seia.entity.TipoSistemaControlePosto;

public class TipoSistemaControlePostoDAOImpl extends BaseDAO<TipoSistemaControlePosto>{

	@Inject
	IDAO<TipoSistemaControlePosto> tipoSistemaControlePostoDAO;
	
	@Override
	protected IDAO<TipoSistemaControlePosto> getDao() {
		return this.tipoSistemaControlePostoDAO;
	}
}
