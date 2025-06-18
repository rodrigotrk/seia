package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.FcePesquisaMineralMetodoRecuperacaoDAOImpl;
import br.gov.ba.seia.entity.FcePesquisaMineral;
import br.gov.ba.seia.entity.FcePesquisaMineralMetodoRecuperacao;
import br.gov.ba.seia.entity.MetodoRecuperacaoIntervencao;

/**
 * Service de {@link MetodoRecuperacaoIntervencao}
 * 
 * @author alexandre.queiroz
 * @since 20/07/2016
 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
 */

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FcePesquisaMineralMetodoRecuperacaoService {
	
	@Inject
	private FcePesquisaMineralMetodoRecuperacaoDAOImpl fcePesquisaMineralProspeccaoGeofisicaDAO;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarListaFcePesquisaMineralMetodoRecuperacao(List<FcePesquisaMineralMetodoRecuperacao> listaFcePesquisaMineralMetodoRecuperacao) {
		fcePesquisaMineralProspeccaoGeofisicaDAO.salvarListaFcePesquisaMineralMetodoRecuperacao(listaFcePesquisaMineralMetodoRecuperacao);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFcePesquisaMineralMetodoRecuperacaoBy(FcePesquisaMineral ideFcePesquisaMineral) {
		fcePesquisaMineralProspeccaoGeofisicaDAO.excluirFcePesquisaMineralMetodoRecuperacaoBy(ideFcePesquisaMineral);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FcePesquisaMineralMetodoRecuperacao> listarFcePesquisaMineralMetodoRecuperacao(FcePesquisaMineral ideFcePesquisaMineral)  {
		return fcePesquisaMineralProspeccaoGeofisicaDAO.listarFcePesquisaMineralMetodoRecuperacao(ideFcePesquisaMineral);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarListaFcePesquisaMineralMetodoRecuperacaoNativeQuery(List<FcePesquisaMineralMetodoRecuperacao> listaFcePesquisaMineralMetodoRecuperacao) throws Exception{
			
		fcePesquisaMineralProspeccaoGeofisicaDAO.salvarListaFcePesquisaMineralMetodoRecuperacaoByNativeQuery(listaFcePesquisaMineralMetodoRecuperacao);
	}
}
