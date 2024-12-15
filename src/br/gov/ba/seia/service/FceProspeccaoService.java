package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.FceProspeccaoDAOImpl;
import br.gov.ba.seia.entity.FcePesquisaMineral;
import br.gov.ba.seia.entity.FcePesquisaMineralProspeccao;
import br.gov.ba.seia.entity.FceProspeccao;
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
public class FceProspeccaoService {

	@Inject
	private FceProspeccaoDAOImpl fceProspeccaoDAO;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFcePesquisaMineralProspeccaoBy(FceProspeccao ideFcePesquisaMineralProspeccao) {
		fceProspeccaoDAO.salvarFceProspeccao(ideFcePesquisaMineralProspeccao);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarListaFceProspeccao(List<FceProspeccao> listaFceProspeccao) {
		fceProspeccaoDAO.salvarListaFceProspeccao(listaFceProspeccao);
	}


	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceProspeccao> listarFceProspeccaoBy(FcePesquisaMineralProspeccao ideFcePesquisaMineralProspeccao){
		return fceProspeccaoDAO.listarFceProspeccaoBy(ideFcePesquisaMineralProspeccao);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFceProspeccaoBy(FcePesquisaMineralProspeccao ideFcePesquisaMineralProspeccao){
		fceProspeccaoDAO.excluirFceProspeccaoBy(ideFcePesquisaMineralProspeccao);
	}
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceProspeccao> listarFceProspeccaoByIdeFcePesquisaMineral(FcePesquisaMineral ideFcePesquisaMineral) {
		return fceProspeccaoDAO.listarFceProspeccaoByIdeFcePesquisaMineral(ideFcePesquisaMineral);
	}


	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFceProspeccao(FceProspeccao fceProspeccao){
		fceProspeccaoDAO.excluirFceProspeccao(fceProspeccao);
	}
}
