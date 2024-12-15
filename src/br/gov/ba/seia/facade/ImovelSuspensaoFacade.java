package br.gov.ba.seia.facade;



import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import br.gov.ba.seia.service.ImovelSuspensaoService;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ImovelSuspensaoFacade {
	
	@EJB
	private ImovelSuspensaoService imovelSuspensaoService;	


}