package br.gov.ba.seia.service;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.BaseDAO;
import br.gov.ba.seia.dao.TipoSistemaControlePostoDAOImpl;
import br.gov.ba.seia.entity.TipoSistemaControlePosto;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoSistemaControlePostoService extends BaseService<TipoSistemaControlePosto>{

	@Inject
	TipoSistemaControlePostoDAOImpl tipoSistemaControlePostoDAOImpl;
	
	@Override
	protected BaseDAO<TipoSistemaControlePosto> getDaoImpl() {
		return this.tipoSistemaControlePostoDAOImpl;
	}

}
