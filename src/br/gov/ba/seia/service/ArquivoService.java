package br.gov.ba.seia.service;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.Arquivo;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ArquivoService {
	
	@Inject
	IDAO<Arquivo> arquivoDAO;	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(Arquivo pArquivo) {
		arquivoDAO.remover(pArquivo);
	}
	

}
