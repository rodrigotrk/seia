package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.FcePesquisaMineralOrigemEnergiaDAOImpl;
import br.gov.ba.seia.entity.FcePesquisaMineral;
import br.gov.ba.seia.entity.FcePesquisaMineralOrigemEnergia;
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
public class FcePesquisaMineralOrigemEnergiaService {

	@Inject
	private FcePesquisaMineralOrigemEnergiaDAOImpl fcePesquisaMineralOrigemEnergiaDAO;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFcePesquisaMineralOrigemEnergia(List<FcePesquisaMineralOrigemEnergia> ideFcePesquisaMineralOrigemEnergia)  {	
		fcePesquisaMineralOrigemEnergiaDAO.salvarFcePesquisaMineralOrigemEnergia(ideFcePesquisaMineralOrigemEnergia);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FcePesquisaMineralOrigemEnergia> listarFcePesquisaMineralOrigemEnergiaBy(FcePesquisaMineral fcePesquisaMineral)  {
		return fcePesquisaMineralOrigemEnergiaDAO.listarFcePesquisaMineralOrigemEnergiaBy(fcePesquisaMineral);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFceSuprimentoEnergia(FcePesquisaMineral ideFcePesquisaMineral) {
		fcePesquisaMineralOrigemEnergiaDAO.excluirFceSuprimentoEnergia(ideFcePesquisaMineral);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFcePesquisaMineralOrigemEnergiaByNativeQuery(List<FcePesquisaMineralOrigemEnergia> ideFcePesquisaMineralOrigemEnergia,FcePesquisaMineral ideFcePesquisaMineral) throws Exception {	
		fcePesquisaMineralOrigemEnergiaDAO.salvarFcePesquisaMineralOrigemEnergiaByNativeQuery(ideFcePesquisaMineralOrigemEnergia);
	}
}