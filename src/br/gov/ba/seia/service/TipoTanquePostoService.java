package br.gov.ba.seia.service;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.BaseDAO;
import br.gov.ba.seia.entity.TipoTanquePosto;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoTanquePostoService extends BaseService<TipoTanquePosto>{

	@Inject
	TipoTanquePostoDAOImpl tipoTanquePostoDAOImpl;
	
	@Override
	protected BaseDAO<TipoTanquePosto> getDaoImpl() {
		return this.tipoTanquePostoDAOImpl;
	}

}
