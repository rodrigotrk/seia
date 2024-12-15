package br.gov.ba.seia.service;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.SupressaoVegetacao;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class SupressaoVegetacaoService {

	@Inject
	IDAO<SupressaoVegetacao> dao;

	@Inject
	IDAO<ImovelRural> imovelRuralDAO;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(SupressaoVegetacao pSupressaoVegetacao) throws Exception {
		dao.salvar(pSupressaoVegetacao);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizar(SupressaoVegetacao pSupressaoVegetacao) throws Exception {
		dao.atualizar(pSupressaoVegetacao);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(SupressaoVegetacao pSupressaoVegetacao) throws Exception {
		dao.remover(pSupressaoVegetacao);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirPorImovel(SupressaoVegetacao pSupressaoVegetacao) throws Exception {
	    ImovelRural imovelRural = pSupressaoVegetacao.getIdeImovelRural();
	    imovelRural.setSupressaoVegetacao(null);
	    imovelRuralDAO.atualizar(imovelRural);
	    
	    pSupressaoVegetacao.setIdeImovelRural(null);
	    dao.remover(pSupressaoVegetacao);
	}
}