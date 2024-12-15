package br.gov.ba.seia.service;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.LacErbDAOImpl;
import br.gov.ba.seia.entity.LacErb;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class LacErbService {

	@Inject
	LacErbDAOImpl lacErbDAOImpl;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(LacErb pLacErb)  {
		this.lacErbDAOImpl.salvar(pLacErb);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizar(LacErb pLacErb)  {
		this.lacErbDAOImpl.salvarOuAtualizar(pLacErb);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(LacErb pLacErb)  {
		this.lacErbDAOImpl.atualizar(pLacErb);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizar(LacErb pLacErb)  {
		this.lacErbDAOImpl.atualizar(pLacErb);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public LacErb carregarByIdRequerimento(Integer pIde)  {
		return this.lacErbDAOImpl.carregarByIdRequerimento(pIde);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Boolean hasLac(Integer pIde)  {
		return this.lacErbDAOImpl.hasLac(pIde);
	}

}