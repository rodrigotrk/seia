package br.gov.ba.seia.service;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.BaseDAO;
import br.gov.ba.seia.dao.SistemaControlePostoDAOImpl;
import br.gov.ba.seia.entity.SistemaControlePosto;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class SistemaControlePostoService extends BaseService<SistemaControlePosto>{

	@Inject
	SistemaControlePostoDAOImpl sistemaControlePostoDAOImpl; 
	
	public Collection<SistemaControlePosto> carregarByIdeLac(Integer ideLac) throws Exception {
		return this.sistemaControlePostoDAOImpl.carregarByIdeLac(ideLac);
	}
	
	@Override
	public void remover(SistemaControlePosto sistemaControlePosto) {
		this.sistemaControlePostoDAOImpl.remover(sistemaControlePosto.getIdeSistemaControlePosto());
	}
	
	@Override
	protected BaseDAO<SistemaControlePosto> getDaoImpl() {
		return this.sistemaControlePostoDAOImpl;
	}


}
