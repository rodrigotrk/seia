package br.gov.ba.seia.service;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.ImovelRuralTac;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ImovelRuralTacService {

	@Inject
	IDAO<ImovelRuralTac> dao;

	@Inject
	IDAO<ImovelRural> imovelRuralDAO;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(ImovelRuralTac pImovelRuralTac)  {
		dao.salvar(pImovelRuralTac);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizar(ImovelRuralTac pImovelRuralTac)  {
		dao.atualizar(pImovelRuralTac);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(ImovelRuralTac pImovelRuralTac)  {
		dao.remover(pImovelRuralTac);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirPorImovel(ImovelRuralTac pImovelRuralTac)  {
	    ImovelRural imovelRural = pImovelRuralTac.getIdeImovelRural();
	    imovelRural.setImovelRuralTac(null);
	    imovelRuralDAO.atualizar(imovelRural);
	    
	    pImovelRuralTac.setIdeImovelRural(null);
	    dao.remover(pImovelRuralTac);
	}
}