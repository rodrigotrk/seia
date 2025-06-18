package br.gov.ba.seia.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.entity.Acondicionamento;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class AcondicionamentoDAOImpl {

	@Inject
	private IDAO<Acondicionamento> acondicionamentoDAO;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Acondicionamento> listarTodos() {
		return this.acondicionamentoDAO.listarTodos();
	}
}	
