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
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.entity.FcePesquisaMineral;
import br.gov.ba.seia.entity.FcePesquisaMineralDestinoResiduo;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

/**
 * @author Alexandre Queiroz
 *
 */

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FcePesquisaMineralDestinoResiduoDAOImpl {

	@Inject
	private IDAO<FcePesquisaMineralDestinoResiduo> fcePesquisaMineralDestinoResiduoDAO;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarListaFcePesquisaMineralDestinoResiduo(List<FcePesquisaMineralDestinoResiduo> listaFcePesquisaMineralDestinoResiduo) {
		fcePesquisaMineralDestinoResiduoDAO.salvarEmLote(listaFcePesquisaMineralDestinoResiduo);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFcePesquisaMineralDestinoResiduoBy(FcePesquisaMineral ideFcePesquisaMineral) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideFcePesquisaMineral", ideFcePesquisaMineral.getIdeFcePesquisaMineral());
		fcePesquisaMineralDestinoResiduoDAO.executarNamedQuery("FcePesquisaMineralDestinoResiduo.removeByIdeFcePesquisaMineral", parameters);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FcePesquisaMineralDestinoResiduo> listarFcePesquisaMineralDestinoResiduoBy(FcePesquisaMineral ideFcePesquisaMineral)  {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(FcePesquisaMineralDestinoResiduo.class)
		.createAlias("ideDestinoResiduo", "ideDestinoResiduo")
		.add(Restrictions.eq("ideFcePesquisaMineral.ideFcePesquisaMineral", ideFcePesquisaMineral.getIdeFcePesquisaMineral()))  
		.setProjection(Projections.projectionList()
			.add(Projections.property("ideFcePesquisaMineral.ideFcePesquisaMineral"),"ideFcePesquisaMineral.ideFcePesquisaMineral")
			.add(Projections.property("ideDestinoResiduo.ideDestinoResiduo"),"ideDestinoResiduo.ideDestinoResiduo")
			
			.add(Projections.property("ideFcePesquisaMineral.ideFcePesquisaMineral"),"ideFcePesquisaMineralDestinoResiduoPK.ideFcePesquisaMineral")
			.add(Projections.property("ideDestinoResiduo.ideDestinoResiduo"),"ideFcePesquisaMineralDestinoResiduoPK.ideDestinoResiduo")
			
		).setResultTransformer(new AliasToNestedBeanResultTransformer(FcePesquisaMineralDestinoResiduo.class) ) ; 
		return fcePesquisaMineralDestinoResiduoDAO.listarPorCriteria(detachedCriteria, Order.asc("ideFcePesquisaMineral.ideFcePesquisaMineral"));
	}


}
