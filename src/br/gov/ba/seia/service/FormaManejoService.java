package br.gov.ba.seia.service;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.FormaManejoDAOImpl;
import br.gov.ba.seia.entity.FormaManejo;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FormaManejoService {
	

	@Inject
	FormaManejoDAOImpl formaManejoDAOImpl;
	
	public void salvarFormaManejo(FormaManejo formaManejo)  {
		formaManejoDAOImpl.salvarFormaManejo(formaManejo);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<FormaManejo> listaFormaManejo()  {
		return formaManejoDAOImpl.listaFormaManejo();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FormaManejo carregarFormaManejo(Integer id) {
		return formaManejoDAOImpl.carregarFormaManejo(id);
	}
	
	

}
