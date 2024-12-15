package br.gov.ba.seia.facade;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.FceEnergia;
import br.gov.ba.seia.service.FceGeracaoEnergiaService;


@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceGeracaoEnergiaFacade {
	
	@EJB
	private FceGeracaoEnergiaService fceGeracaoEnergiaService;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FceEnergia getFceGeracaoEnergiaBy(Fce ideFce) throws Exception{
		return fceGeracaoEnergiaService.getFceGeracaoEnergiaBy(ideFce);
	}

		
}
