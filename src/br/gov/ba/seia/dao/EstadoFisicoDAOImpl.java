package br.gov.ba.seia.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.entity.EstadoFisico;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class EstadoFisicoDAOImpl{

	@Inject
	private IDAO<EstadoFisico> estadoFisicoDAO;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<EstadoFisico> listarTodos() {
		return this.estadoFisicoDAO.listarTodos();
	}
}	
