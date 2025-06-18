package br.gov.ba.seia.facade;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import br.gov.ba.seia.entity.EstadoFisico;
import br.gov.ba.seia.service.EstadoFisicoService;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class EstadoFisicoFacade {
	

	@EJB
	private EstadoFisicoService estadoFisicoService;
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<EstadoFisico> listarTodos() throws Exception {
		return this.estadoFisicoService.listarTodos();
	}

	
}
