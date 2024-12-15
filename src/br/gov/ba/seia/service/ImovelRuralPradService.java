package br.gov.ba.seia.service;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.ImovelRuralPrad;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ImovelRuralPradService {

	@Inject
	IDAO<ImovelRuralPrad> dao;

	@Inject
	IDAO<ImovelRural> imovelRuralDAO;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(ImovelRuralPrad pImovelRuralPrad)  {
		dao.salvar(pImovelRuralPrad);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizar(ImovelRuralPrad pImovelRuralPrad)  {
		dao.atualizar(pImovelRuralPrad);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(ImovelRuralPrad pImovelRuralPrad)  {
		dao.remover(pImovelRuralPrad);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirPorImovel(ImovelRuralPrad pImovelRuralPrad)  {
	    ImovelRural imovelRural = pImovelRuralPrad.getIdeImovelRural();
	    imovelRural.setImovelRuralPrad(null);
	    imovelRuralDAO.atualizar(imovelRural);
	    
	    pImovelRuralPrad.setIdeImovelRural(null);
	    dao.remover(pImovelRuralPrad);
	}
}