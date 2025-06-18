package br.gov.ba.seia.facade;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import br.gov.ba.seia.entity.DisposicaoFinalResiduo;
import br.gov.ba.seia.service.DisposicaoFinalResiduoService;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class DisposicaoFinalResiduoFacade {
	

	@EJB
	private DisposicaoFinalResiduoService disposicaoFinalResiduoService;
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<DisposicaoFinalResiduo> listarTodos() throws Exception {
		return (List<DisposicaoFinalResiduo>) this.disposicaoFinalResiduoService.listarTodos();
	}

	
}
