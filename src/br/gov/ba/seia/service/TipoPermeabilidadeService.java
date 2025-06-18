package br.gov.ba.seia.service;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.BaseDAO;
import br.gov.ba.seia.dao.TipoPermeabilidadeDAOImpl;
import br.gov.ba.seia.entity.TipoPermeabilidade;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoPermeabilidadeService extends BaseService<TipoPermeabilidade>{

	@Inject
	TipoPermeabilidadeDAOImpl tipoPermeabilidadeDAOImpl;
	
	@Override
	protected BaseDAO<TipoPermeabilidade> getDaoImpl() {
		return this.tipoPermeabilidadeDAOImpl;
	}

}
