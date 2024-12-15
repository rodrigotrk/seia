package br.gov.ba.seia.service;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.BaseDAO;
import br.gov.ba.seia.dao.TipoEquipamentoEntornoPostoDAOImpl;
import br.gov.ba.seia.entity.TipoEquipamentoEntornoPosto;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoEquipamentoEntornoPostoService extends BaseService<TipoEquipamentoEntornoPosto>{
	
	@Inject
	TipoEquipamentoEntornoPostoDAOImpl tipoEquipamentoEntornoPostoDAOImpl;
	
	@Override
	protected BaseDAO<TipoEquipamentoEntornoPosto> getDaoImpl() {
		return this.tipoEquipamentoEntornoPostoDAOImpl;
	}

	public Collection<TipoEquipamentoEntornoPosto> carregarByIdeLac(Integer ideLac) throws Exception {
		return this.tipoEquipamentoEntornoPostoDAOImpl.carregarByIdeLac(ideLac);
	}

	public void removerByIdeLac(Integer ideLac, Integer idEquipamentoEntorno) throws Exception {
		this.tipoEquipamentoEntornoPostoDAOImpl.remover(ideLac,idEquipamentoEntorno);
	}

}
