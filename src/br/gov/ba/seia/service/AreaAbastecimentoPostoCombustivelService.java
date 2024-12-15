package br.gov.ba.seia.service;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.AreaAbastecimentoPostoCombustivelDAOImpl;
import br.gov.ba.seia.dao.BaseDAO;
import br.gov.ba.seia.entity.AreaAbastecimentoPostoCombustivel;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class AreaAbastecimentoPostoCombustivelService extends BaseService<AreaAbastecimentoPostoCombustivel> {

	@Inject
	AreaAbastecimentoPostoCombustivelDAOImpl areaAbastecimentoPostoCombustivelDAOImpl;
	
	@Override
	protected BaseDAO<AreaAbastecimentoPostoCombustivel> getDaoImpl() {
		return this.areaAbastecimentoPostoCombustivelDAOImpl;
	}

	public Collection<AreaAbastecimentoPostoCombustivel> carregarByIdeLac(Integer ideLac)  {
		return this.areaAbastecimentoPostoCombustivelDAOImpl.carregarByIdeLac(ideLac);
	}
	
}
