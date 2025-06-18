package br.gov.ba.seia.facade;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import br.gov.ba.seia.entity.Cnae;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.service.CnaeService;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CnaeServiceFacede {

		
	@EJB
	CnaeService cnaeService;
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarTipologiaCnae(Cnae cnae, Tipologia tipologia)  {
		cnae = cnaeService.carregarCnae(cnae.getIdeCnae());
		tipologia.getCnaeCollection().add(cnae);
		cnae.getTipologiaCollection().add(tipologia);
		cnaeService.salvarCnae(cnae);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirTipologiaCnae(Cnae cnae, Tipologia tipologia) {
		cnaeService.excluirTipologiaCnae(cnae, tipologia);
		
	}
	
}