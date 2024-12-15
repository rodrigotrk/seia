package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.FcePesquisaMineralProspeccaoDAOImpl;
import br.gov.ba.seia.entity.FcePesquisaMineral;
import br.gov.ba.seia.entity.FcePesquisaMineralProspeccao;
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
public class FcePesquisaMineralProspeccaoService {

	@Inject
	private FcePesquisaMineralProspeccaoDAOImpl fcePesquisaMineralProspeccaoDAO;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FcePesquisaMineralProspeccao> listarFcePesquisaMineralProspeccaoBy(FcePesquisaMineral ideFcePesquisaMineral) {
		return fcePesquisaMineralProspeccaoDAO.listarFcePesquisaMineralProspeccaoBy(ideFcePesquisaMineral);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFcePesquisaMineralProspeccao(List<FcePesquisaMineralProspeccao> ideFcePesquisaMineralProspeccao) {
		 fcePesquisaMineralProspeccaoDAO.salvarFcePesquisaMineralProspeccao(ideFcePesquisaMineralProspeccao);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarListaFcePesquisaMineralProspeccao(List<FcePesquisaMineralProspeccao> listaFcePesquisaMineralProspeccao) {
		 fcePesquisaMineralProspeccaoDAO.salvarListaFcePesquisaMineralProspeccao(listaFcePesquisaMineralProspeccao);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFcePesquisaMineralProspeccao(FcePesquisaMineral ideFcePesquisaMineral){
		fcePesquisaMineralProspeccaoDAO.excluirFcePesquisaMineralProspeccao(ideFcePesquisaMineral);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFcePesquisaMineralProspeccao(FcePesquisaMineralProspeccao ideFcePesquisaMineralProspeccao){
		fcePesquisaMineralProspeccaoDAO.excluirFcePesquisaMineralProspeccao(ideFcePesquisaMineralProspeccao);
	}

		
	

}
