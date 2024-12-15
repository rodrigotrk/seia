package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.EstadoFisicoDAOImpl;
import br.gov.ba.seia.entity.EstadoFisico;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class EstadoFisicoService {
	
	@Inject
	EstadoFisicoDAOImpl estadoFisicoDAOImpl;
	
	public List<EstadoFisico> listarTodos()  {
		return this.estadoFisicoDAOImpl.listarTodos();
	}

	
}
