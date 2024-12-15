package br.gov.ba.seia.dao;

import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import br.gov.ba.seia.entity.ErbEquipamento;

/***
 * 
 * @author luis
 *
 */
public class ErbEquipamentoDAOImpl {
	
	@Inject
	IDAO<ErbEquipamento> daoErbEquipamento;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(ErbEquipamento pErbEquipamento)  {
		this.daoErbEquipamento.salvar(pErbEquipamento);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ErbEquipamento carregarByIde(int pIdeErbEquipamento){
		return this.daoErbEquipamento.carregarGet(pIdeErbEquipamento);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizar(ErbEquipamento pErbEquipamento)  {
		this.daoErbEquipamento.atualizar(pErbEquipamento);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizar(ErbEquipamento pErbEquipamento)  {
		this.daoErbEquipamento.salvarOuAtualizar(pErbEquipamento);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remover(ErbEquipamento pErbEquipamento)  {
		this.daoErbEquipamento.remover(pErbEquipamento);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remover(List<ErbEquipamento> pErbEquipamentos)  {
		for (ErbEquipamento erbEquipamento : pErbEquipamentos) {
			this.remover(erbEquipamento);
		}
	}

}
