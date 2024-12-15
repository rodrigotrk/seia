package br.gov.ba.seia.facade;



import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import br.gov.ba.seia.entity.MotivoSuspensaoCadastro;
import br.gov.ba.seia.service.MotivoSuspensaoCadastroService;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class MotivoSuspensaoCadastroFacade {
	
		
	@EJB
	private MotivoSuspensaoCadastroService motivoSuspensaoCadastroService;	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<MotivoSuspensaoCadastro> listarTodosMotivosSuspensao()  {
		return this.motivoSuspensaoCadastroService.listarTodosMotivos();
	}
	
}