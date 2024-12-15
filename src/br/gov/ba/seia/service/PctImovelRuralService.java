package br.gov.ba.seia.service;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import br.gov.ba.seia.dao.PctImovelRuralDAOImpl;
import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.PctImovelRural;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PctImovelRuralService {

	@EJB
	private PctImovelRuralDAOImpl pctImovelRuralDAOImpl;
	
	public PctImovelRural buscarPctImovelRural(ImovelRural imovelRural) throws Exception{
		return pctImovelRuralDAOImpl.buscarPctImovelRural(imovelRural);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarPctImovelRural(PctImovelRural pctImovelRural) throws Exception{
		pctImovelRuralDAOImpl.salvarPctImovelRural(pctImovelRural);
	}
	
	public boolean isPCT(ImovelRural imovelRural) throws Exception{
		return pctImovelRuralDAOImpl.isPCT(imovelRural);
	}
}
