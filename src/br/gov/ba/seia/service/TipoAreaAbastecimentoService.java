package br.gov.ba.seia.service;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.BaseDAO;
import br.gov.ba.seia.dao.TipoAreaAbastecimentoDAOImpl;
import br.gov.ba.seia.entity.TipoAreaAbastecimento;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoAreaAbastecimentoService extends BaseService<TipoAreaAbastecimento>{
	
	@Inject
	TipoAreaAbastecimentoDAOImpl tipoAreaAbastecimentoDAOImpl;
	
	@Override
	protected BaseDAO<TipoAreaAbastecimento> getDaoImpl() {
		return this.tipoAreaAbastecimentoDAOImpl;
	}

}
