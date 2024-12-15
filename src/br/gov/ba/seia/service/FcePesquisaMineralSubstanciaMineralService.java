package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.FcePesquisaMineralSubstanciaMineralDAOImpl;
import br.gov.ba.seia.entity.FcePesquisaMineral;
import br.gov.ba.seia.entity.FcePesquisaMineralSubstanciaMineralTipologia;
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
public class FcePesquisaMineralSubstanciaMineralService {

	@Inject
	private FcePesquisaMineralSubstanciaMineralDAOImpl fcePesquisaMineralSubstanciaMineralDAO;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFcePesquisaMineralSubstanciaMineral(List<FcePesquisaMineralSubstanciaMineralTipologia> ideFcePesquisaMineralSubstanciaMineral)  {	
		fcePesquisaMineralSubstanciaMineralDAO.salvarFcePesquisaMineralSubstanciaMineral(ideFcePesquisaMineralSubstanciaMineral);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FcePesquisaMineralSubstanciaMineralTipologia> listarFcePesquisaMineralSubstanciaMineral(FcePesquisaMineral ideFcePesquisaMineral)  {	
		return fcePesquisaMineralSubstanciaMineralDAO.listarFcePesquisaMineralSubstanciaMineral(ideFcePesquisaMineral);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFcePesquisaMineralSubstanciaMineral(FcePesquisaMineral ideFcePesquisaMineral)  {	
		fcePesquisaMineralSubstanciaMineralDAO.excluirFcePesquisaMineral(ideFcePesquisaMineral);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFcePesquisaMineralSubstanciaMineralByNativeQuery(List<FcePesquisaMineralSubstanciaMineralTipologia> ideFcePesquisaMineralSubstanciaMineral) throws Exception {	
		fcePesquisaMineralSubstanciaMineralDAO.salvarFcePesquisaMineralSubstanciaMineralByNativeQuery(ideFcePesquisaMineralSubstanciaMineral);
	}
	

}
