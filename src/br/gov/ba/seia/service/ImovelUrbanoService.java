package br.gov.ba.seia.service;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.ImovelUrbano;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ImovelUrbanoService {

	@Inject
	IDAO<ImovelUrbano> imovelUrbanoDAO;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarAtualizarImovelUrbano(ImovelUrbano pImovelUrbano){
		imovelUrbanoDAO.salvarOuAtualizar(pImovelUrbano);
	}
}