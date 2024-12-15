package br.gov.ba.seia.service;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.ImovelRuralAbastecimento;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ImovelRuralAbastecimentoService {

	@Inject
	IDAO<ImovelRuralAbastecimento> dao;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(ImovelRuralAbastecimento pImovelRuralAbastecimento)  {
		this.dao.salvar(pImovelRuralAbastecimento);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizar(ImovelRuralAbastecimento pImovelRuralAbastecimento)  {
		this.dao.atualizar(pImovelRuralAbastecimento);
	}

}
