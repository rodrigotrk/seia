package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.Mes;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class MesService {

	@Inject
	IDAO<Mes> mesIDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Mes> listarMes() {
		return mesIDAO.listarTodos();
	}
	
}