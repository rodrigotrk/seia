package br.gov.ba.seia.service;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.FormaRealocacaoRl;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FormaRealocacaoRlService {
	
	
	@Inject
	IDAO<FormaRealocacaoRl> formaRealocacaoRlServiceDAO;
	
	
	public Collection<FormaRealocacaoRl> listarTodosFormaRealocacaoRls() {
		return formaRealocacaoRlServiceDAO.listarTodos();	
	}

}