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
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.FcePesquisaMineral;
import br.gov.ba.seia.entity.FcePesquisaMineralProspeccao;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

/**
 * @author Alexandre Queiroz
 *
 */

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FcePesquisaMineralProspeccaoDAOImpl {

	@Inject
	private IDAO<FcePesquisaMineralProspeccao> fcePesquisaMineralProspeccaoDAO;

 	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FcePesquisaMineralProspeccao> listarFcePesquisaMineralProspeccaoBy(FcePesquisaMineral ideFcePesquisaMineral)  {		
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(FcePesquisaMineralProspeccao.class)
			.createAlias("ideMetodoProspeccao", "ideMetodoProspeccao", JoinType.INNER_JOIN)
			.add(Restrictions.eq("ideFcePesquisaMineral.ideFcePesquisaMineral", ideFcePesquisaMineral.getIdeFcePesquisaMineral()))
			.setProjection(Projections.projectionList()
			   .add(Projections.property("ideFcePesquisaMineralProspeccao"),"ideFcePesquisaMineralProspeccao")		
			   .add(Projections.property("ideFcePesquisaMineral.ideFcePesquisaMineral"),"ideFcePesquisaMineral.ideFcePesquisaMineral")
			   .add(Projections.property("ideMetodoProspeccao.ideMetodoProspeccao"),"ideMetodoProspeccao.ideMetodoProspeccao")
			   .add(Projections.property("ideMetodoProspeccao.nomMetodoProspeccao"),"ideMetodoProspeccao.nomMetodoProspeccao")		
			).setResultTransformer(new AliasToNestedBeanResultTransformer(FcePesquisaMineralProspeccao.class));
		return fcePesquisaMineralProspeccaoDAO.listarPorCriteria(detachedCriteria, Order.asc("ideFcePesquisaMineral.ideFcePesquisaMineral"));
	}
 	
 	@TransactionAttribute(TransactionAttributeType.REQUIRED)
 	public void salvarFcePesquisaMineralProspeccao(List<FcePesquisaMineralProspeccao> ideFcePesquisaMineralProspeccao){
 		fcePesquisaMineralProspeccaoDAO.salvarEmLote(ideFcePesquisaMineralProspeccao);	
 	}

 	@TransactionAttribute(TransactionAttributeType.REQUIRED)
 	public void salvarListaFcePesquisaMineralProspeccao(List<FcePesquisaMineralProspeccao> listaFcePesquisaMineralProspeccao){
 		fcePesquisaMineralProspeccaoDAO.salvarEmLote(listaFcePesquisaMineralProspeccao);	
 	}
 	
 	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFcePesquisaMineralProspeccao(FcePesquisaMineral ideFcePesquisaMineral) {
 		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideFcePesquisaMineral", ideFcePesquisaMineral.getIdeFcePesquisaMineral());
		fcePesquisaMineralProspeccaoDAO.executarNamedQuery("FcePesquisaMineralProspeccao.removeByIdeFcePesquisaMineralProspeccao", parameters);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFcePesquisaMineralProspeccao(FcePesquisaMineralProspeccao ideFcePesquisaMineralProspeccao) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideFcePesquisaMineralProspeccao", ideFcePesquisaMineralProspeccao.getIdeFcePesquisaMineralProspeccao());
		fcePesquisaMineralProspeccaoDAO.executarNamedQuery("FcePesquisaMineralProspeccao.remove", parameters);
	}
	
}
