package br.gov.ba.seia.dao;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import br.gov.ba.seia.entity.CronogramaRecuperacao;

public class CronogramaRecuperacaoDAOImpl implements CronogramaRecuperacaoDAOIf{

	@Inject
	IDAO<CronogramaRecuperacao> dao;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CronogramaRecuperacao filtrarById(CronogramaRecuperacao pCronogramaRecuperacao) {
		return dao.buscarEntidadePorExemplo(pCronogramaRecuperacao);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(CronogramaRecuperacao pCronogramaRecuperacao)  {
		dao.salvar(pCronogramaRecuperacao);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizar(CronogramaRecuperacao pCronogramaRecuperacao) {
		dao.atualizar(pCronogramaRecuperacao);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizar(CronogramaRecuperacao pCronogramaRecuperacao){
		dao.salvarOuAtualizar(pCronogramaRecuperacao);
	}
	
}
