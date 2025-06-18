package br.gov.ba.seia.facade;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import br.gov.ba.seia.entity.Acondicionamento;
import br.gov.ba.seia.service.AcondicionamentoService;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class AcondicionamentoFacade {
	

	@EJB
	private AcondicionamentoService acondicionamentoService;
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Acondicionamento> listarTodos() {
		return this.acondicionamentoService.listarTodos();
	}

	
}
