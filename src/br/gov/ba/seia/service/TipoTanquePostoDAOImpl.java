package br.gov.ba.seia.service;

import javax.inject.Inject;

import br.gov.ba.seia.dao.BaseDAO;
import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.TipoTanquePosto;

public class TipoTanquePostoDAOImpl extends BaseDAO<TipoTanquePosto>{
	
	@Inject
	IDAO<TipoTanquePosto> tipoTanquePostoDAO;
	
	@Override
	protected IDAO<TipoTanquePosto> getDao() {
		return this.tipoTanquePostoDAO;
	}

}
