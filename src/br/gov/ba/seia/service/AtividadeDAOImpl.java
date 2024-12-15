package br.gov.ba.seia.service;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.BaseDAO;
import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.AtividadeInexigivel;

public class AtividadeDAOImpl extends BaseDAO<AtividadeInexigivel>{

	@Inject
	IDAO<AtividadeInexigivel> atividadeImplDAO;

	@Override
	protected IDAO<AtividadeInexigivel> getDao() {
		return this.atividadeImplDAO;
	}
		
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(AtividadeInexigivel pAtividade) {

		atividadeImplDAO.salvar(pAtividade);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizar(AtividadeInexigivel pAtividade) {
		atividadeImplDAO.atualizar(pAtividade);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(AtividadeInexigivel pAtividade)  {

		atividadeImplDAO.atualizar(pAtividade);
	}	
}
