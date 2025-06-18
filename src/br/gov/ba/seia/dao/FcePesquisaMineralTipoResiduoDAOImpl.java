package br.gov.ba.seia.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.entity.FcePesquisaMineral;
import br.gov.ba.seia.entity.FcePesquisaMineralTipoResiduo;

/**
 * @author Alexandre Queiroz
 *
 */

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FcePesquisaMineralTipoResiduoDAOImpl {

	@Inject
	private IDAO<FcePesquisaMineralTipoResiduo> fcePesquisaMineralTipoResiduoDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FcePesquisaMineralTipoResiduo> listarFcePesquisaMineralTipoResiduoBy(FcePesquisaMineral ideFcePesquisaMineral) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(FcePesquisaMineralTipoResiduo.class)
				.add(Restrictions.eq("ideFcePesquisaMineral.ideFcePesquisaMineral", ideFcePesquisaMineral.getIdeFcePesquisaMineral()));
		return fcePesquisaMineralTipoResiduoDAO.listarPorCriteria(detachedCriteria);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirIdeFcePesquisaMineralTipoResiduo(FcePesquisaMineral ideFcePesquisaMineral) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideFcePesquisaMineral", ideFcePesquisaMineral.getIdeFcePesquisaMineral());
		fcePesquisaMineralTipoResiduoDAO.executarNamedQuery("FcePesquisaMineralTipoResiduo.removeByideFcePesquisaMineral", parameters);		
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarListaFcePesquisaMineralTipoResiduo(List<FcePesquisaMineralTipoResiduo> fcePesquisaMineralTipoResiduos)  {
		fcePesquisaMineralTipoResiduoDAO.salvarEmLote(fcePesquisaMineralTipoResiduos);
	}
}
