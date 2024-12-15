package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.FcePesquisaMineralProspeccaoGeofisicaDAOImpl;
import br.gov.ba.seia.entity.FcePesquisaMineral;
import br.gov.ba.seia.entity.FcePesquisaMineralProspeccao;
import br.gov.ba.seia.entity.FcePesquisaMineralProspeccaoGeofisica;
import br.gov.ba.seia.entity.MetodoRecuperacaoIntervencao;

/**
 * Service de {@link MetodoRecuperacaoIntervencao}
 * 
 * @author alexandre.queiroz
 * @since 13/06/2016
 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FcePesquisaMineralProspeccaoGeofisicaService {

	@Inject
	private FcePesquisaMineralProspeccaoGeofisicaDAOImpl fcePesquisaMineralProspeccaoGeofisicaDAO;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarListaFcePesquisaMineralProspeccao(List<FcePesquisaMineralProspeccaoGeofisica> listaFcePesquisaMineralProspeccaoGeofisca) {
		fcePesquisaMineralProspeccaoGeofisicaDAO.salvarListaFcePesquisaMineralProspeccao(listaFcePesquisaMineralProspeccaoGeofisca);
	}
	
		
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirIdeFcePesquisaMineralProspeccaoGeofisicaBy(FcePesquisaMineralProspeccao ideFcePesquisaMineralProspeccao) {
		fcePesquisaMineralProspeccaoGeofisicaDAO.excluirIdeFcePesquisaMineralProspeccaoGeofisicaBy(ideFcePesquisaMineralProspeccao);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirIdeFcePesquisaMineralProspeccaoGeofisicaBy(FcePesquisaMineralProspeccaoGeofisica ideFcePesquisaMineralProspeccaoGeofisica) {
		fcePesquisaMineralProspeccaoGeofisicaDAO.excluirIdeFcePesquisaMineralProspeccaoGeofisicaBy(ideFcePesquisaMineralProspeccaoGeofisica);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FcePesquisaMineralProspeccaoGeofisica> listarFcePesquisaMineralProspeccaoGeofisicaByPesquisaMineral(FcePesquisaMineral fcePesquisaMineral) {
		return fcePesquisaMineralProspeccaoGeofisicaDAO.listarFcePesquisaMineralProspeccaoGeofisicaByPesquisaMineral(fcePesquisaMineral);
	}
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FcePesquisaMineralProspeccaoGeofisica> listarFcePesquisaMineralProspeccaoGeofisicaBy(FcePesquisaMineralProspeccao fcePesquisaMineralProspeccao)  {
		return fcePesquisaMineralProspeccaoGeofisicaDAO.listarFcePesquisaMineralProspeccaoGeofisicaBy(fcePesquisaMineralProspeccao);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FcePesquisaMineralProspeccaoGeofisica> listarFcePesquisaMineralProspeccaoGeofisicaByFcePesquisaMineral(FcePesquisaMineral ideFcePesquisaMineral)  {
		return fcePesquisaMineralProspeccaoGeofisicaDAO.listarFcePesquisaMineralProspeccaoGeofisicaByFcePesquisaMineral(ideFcePesquisaMineral);
	}


}
