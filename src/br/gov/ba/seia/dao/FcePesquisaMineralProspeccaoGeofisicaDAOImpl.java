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
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.FcePesquisaMineral;
import br.gov.ba.seia.entity.FcePesquisaMineralProspeccao;
import br.gov.ba.seia.entity.FcePesquisaMineralProspeccaoGeofisica;
import br.gov.ba.seia.enumerator.MetodoProspeccaoEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;


/**
 * @author Alexandre Queiroz
 * @since 08/07/2016
 * @see <a href="http://10.105.12.26/redmine/issues/7703">#7703</a>
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FcePesquisaMineralProspeccaoGeofisicaDAOImpl {

	@Inject
	private IDAO<FcePesquisaMineralProspeccaoGeofisica> fcePesquisaMineralProspeccaoGeofisicaDAO;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarListaFcePesquisaMineralProspeccao(List<FcePesquisaMineralProspeccaoGeofisica> listaFcePesquisaMineralProspeccaoGeofisca) {
		fcePesquisaMineralProspeccaoGeofisicaDAO.salvarEmLote(listaFcePesquisaMineralProspeccaoGeofisca);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirIdeFcePesquisaMineralProspeccaoGeofisicaBy(FcePesquisaMineralProspeccao ideFcePesquisaMineralProspeccao){
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideFcePesquisaMineralProspeccao", ideFcePesquisaMineralProspeccao.getIdeFcePesquisaMineralProspeccao());
		fcePesquisaMineralProspeccaoGeofisicaDAO.executarNamedQuery("FcePesquisaMineralProspeccaoGeofisica.removeByIdeFcePesquisaMineralProspeccao", parameters);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirIdeFcePesquisaMineralProspeccaoGeofisicaBy(FcePesquisaMineralProspeccaoGeofisica ideFcePesquisaMineralProspeccaoGeofisica) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideFcePesquisaMineralProspeccao", ideFcePesquisaMineralProspeccaoGeofisica.getIdeFcePesquisaMineralProspeccao().getIdeFcePesquisaMineralProspeccao());
		fcePesquisaMineralProspeccaoGeofisicaDAO.executarNamedQuery("FcePesquisaMineralProspeccaoGeofisica.removeByIdeFcePesquisaMineralProspeccao", parameters);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FcePesquisaMineralProspeccaoGeofisica> listarFcePesquisaMineralProspeccaoGeofisicaByPesquisaMineral(FcePesquisaMineral ideFcePesquisaMineral) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(FcePesquisaMineralProspeccaoGeofisica.class)
			.createAlias("ideFcePesquisaMineralProspeccao","ideFcePesquisaMineralProspeccao")
			.createAlias("ideFcePesquisaMineralProspeccao.ideMetodoProspeccao", "ideMetodoProspeccao")
			
			.createAlias("ideFcePesquisaMineralProspeccao.ideFcePesquisaMineral", "ideFcePesquisaMineral")
			
			.add(Restrictions.eq("ideFcePesquisaMineral.ideFcePesquisaMineral", ideFcePesquisaMineral.getIdeFcePesquisaMineral()))
			.add(Restrictions.eq("ideMetodoProspeccao.ideMetodoProspeccao", MetodoProspeccaoEnum.GEOFISICA.getId()))
				.setProjection(Projections.projectionList()
					.add(Projections.property("ideFcePesquisaMineralProspeccao.ideFcePesquisaMineralProspeccao"), "ideFcePesquisaMineralProspeccao.ideFcePesquisaMineralProspeccao")
					.add(Projections.property("ideGeofisica.ideGeofisica"), "ideGeofisica.ideGeofisica")
					
					.add(Projections.property("ideGeofisica.ideGeofisica"), "ideFcePesquisaMineralProspeccaoGeofisicaPK.ideGeofisica")
					.add(Projections.property("ideFcePesquisaMineralProspeccao.ideFcePesquisaMineralProspeccao"), "ideFcePesquisaMineralProspeccaoGeofisicaPK.ideFcePesquisaMineralProspeccao")
					
				).setResultTransformer(new AliasToNestedBeanResultTransformer(FcePesquisaMineralProspeccaoGeofisica.class));	
		return fcePesquisaMineralProspeccaoGeofisicaDAO.listarPorCriteria(detachedCriteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FcePesquisaMineralProspeccaoGeofisica> listarFcePesquisaMineralProspeccaoGeofisicaBy(FcePesquisaMineralProspeccao fcePesquisaMineralProspeccao) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(FcePesquisaMineralProspeccaoGeofisica.class)
			.add(Restrictions.eq("ideFcePesquisaMineralProspeccao.ideFcePesquisaMineralProspeccao", fcePesquisaMineralProspeccao.getIdeFcePesquisaMineralProspeccao()))
			.setProjection(Projections.projectionList()			
				.add(Projections.property("ideFcePesquisaMineralProspeccao.ideFcePesquisaMineralProspeccao"), "ideFcePesquisaMineralProspeccao.ideFcePesquisaMineralProspeccao")
				.add(Projections.property("ideGeofisica.ideGeofisica"), "ideGeofisica.ideGeofisica")
				.add(Projections.property("ideFcePesquisaMineralProspeccaoGeofisicaPK.ideGeofisica"), "ideGeofisica")
				.add(Projections.property("ideFcePesquisaMineralProspeccaoGeofisicaPK.ideFcePesquisaMineralProspeccao"), "ideFcePesquisaMineralProspeccao")
			).setResultTransformer(new AliasToNestedBeanResultTransformer(FcePesquisaMineralProspeccaoGeofisica.class));
		return  fcePesquisaMineralProspeccaoGeofisicaDAO.listarPorCriteria(detachedCriteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FcePesquisaMineralProspeccaoGeofisica> listarFcePesquisaMineralProspeccaoGeofisicaByFcePesquisaMineral(FcePesquisaMineral ideFcePesquisaMineral) {
 		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(FcePesquisaMineralProspeccaoGeofisica.class)
 			.createAlias("ideFcePesquisaMineralProspeccao", "ideFcePesquisaMineralProspeccao", JoinType.INNER_JOIN)
			.createAlias("ideFcePesquisaMineralProspeccao.ideFcePesquisaMineral", "ideFcePesquisaMineral", JoinType.INNER_JOIN)
			.add(Restrictions.eq("ideFcePesquisaMineral.ideFcePesquisaMineral", ideFcePesquisaMineral.getIdeFcePesquisaMineral()))
			
			.setProjection(Projections.projectionList()
			   .add(Projections.property("ideGeofisica.ideGeofisica"),"ideGeofisica.ideGeofisica")		
			   .add(Projections.property("ideFcePesquisaMineralProspeccao.ideFcePesquisaMineralProspeccao"),"ideFcePesquisaMineralProspeccao.ideFcePesquisaMineralProspeccao")
			   .add(Projections.property("ideGeofisica.ideGeofisica"),"ideFcePesquisaMineralProspeccaoGeofisicaPK.ideGeofisica")		
			   .add(Projections.property("ideFcePesquisaMineralProspeccao.ideFcePesquisaMineralProspeccao"),"ideFcePesquisaMineralProspeccaoGeofisicaPK.ideFcePesquisaMineralProspeccao")
			).setResultTransformer(new AliasToNestedBeanResultTransformer(FcePesquisaMineralProspeccaoGeofisica.class));
			
 		return fcePesquisaMineralProspeccaoGeofisicaDAO.listarPorCriteria(detachedCriteria);
 	}
}
