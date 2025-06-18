package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.ErbEquipamentoDAOImpl;
import br.gov.ba.seia.entity.ErbEquipamento;

/***
 * 
 * @author luis
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ErbEquipamentoService {

	@Inject
	ErbEquipamentoDAOImpl erbEquipamentoDAOImpl;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(ErbEquipamento pErbEquipamento)  {
		this.erbEquipamentoDAOImpl.salvar(pErbEquipamento);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ErbEquipamento carregarByIde(int pIdeErbEquipamento)  {
		return this.erbEquipamentoDAOImpl.carregarByIde(pIdeErbEquipamento);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizar(ErbEquipamento pErbEquipamento)  {
		this.erbEquipamentoDAOImpl.atualizar(pErbEquipamento);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizar(ErbEquipamento pErbEquipamento)  {
		this.erbEquipamentoDAOImpl.salvarOuAtualizar(pErbEquipamento);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remover(ErbEquipamento pErbEquipamento)  {
		this.erbEquipamentoDAOImpl.remover(pErbEquipamento);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remover(List<ErbEquipamento> pErbEquipamentos)  {
		this.erbEquipamentoDAOImpl.remover(pErbEquipamentos);
	}

}