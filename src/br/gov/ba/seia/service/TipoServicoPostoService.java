package br.gov.ba.seia.service;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.BaseDAO;
import br.gov.ba.seia.dao.TipoServicoDAOImpl;
import br.gov.ba.seia.entity.TipoServicoPosto;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoServicoPostoService extends BaseService<TipoServicoPosto> {

	@Inject
	TipoServicoDAOImpl tipoServicoDAOImpl;

	@Override
	protected BaseDAO<TipoServicoPosto> getDaoImpl() {
		return this.tipoServicoDAOImpl;
	}

}
