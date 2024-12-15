package br.gov.ba.seia.service;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.FcePesquisaMineralDAOImpl;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.FcePesquisaMineral;
import br.gov.ba.seia.entity.SubstanciaMineral;

/**
 * Service de {@link SubstanciaMineral}
 * 
 * @author eduardo.fernandes, alexandre.queiroz
 * @since 10/06/2016
 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FcePesquisaMineralService {

	@Inject
	private FcePesquisaMineralDAOImpl fcePesquisaMineralDAO;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FcePesquisaMineral getFcePesquisaMineralBy(Fce ideFce)  {
		return fcePesquisaMineralDAO.getFcePesquisaMineralBy(ideFce);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFcePesquisaMineral(FcePesquisaMineral ideFcePesquisaMineral)  {	
		fcePesquisaMineralDAO.salvarFcePesquisaMineral(ideFcePesquisaMineral);
	}
	
	
}

