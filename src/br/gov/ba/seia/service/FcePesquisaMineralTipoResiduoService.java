package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.FcePesquisaMineralTipoResiduoDAOImpl;
import br.gov.ba.seia.entity.FcePesquisaMineral;
import br.gov.ba.seia.entity.FcePesquisaMineralTipoResiduo;
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
public class FcePesquisaMineralTipoResiduoService {

	@Inject
	private FcePesquisaMineralTipoResiduoDAOImpl fcePesquisaMineralTipoResiduoDAO;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FcePesquisaMineralTipoResiduo> listarFcePesquisaMineralTipoResiduoBy(FcePesquisaMineral ideFcePesquisaMineral)  {
		return fcePesquisaMineralTipoResiduoDAO.listarFcePesquisaMineralTipoResiduoBy(ideFcePesquisaMineral);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirIdeFcePesquisaMineralTipoResiduo(FcePesquisaMineral ideFcePesquisaMineral)  {
		fcePesquisaMineralTipoResiduoDAO.excluirIdeFcePesquisaMineralTipoResiduo(ideFcePesquisaMineral);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarListaFcePesquisaMineralTipoResiduo(List<FcePesquisaMineralTipoResiduo> fcePesquisaMineralTipoResiduos) {
		fcePesquisaMineralTipoResiduoDAO.salvarListaFcePesquisaMineralTipoResiduo(fcePesquisaMineralTipoResiduos);
	}
}
