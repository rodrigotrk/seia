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
import br.gov.ba.seia.entity.FcePesquisaMineralOrigemEnergia;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

/**
 * @author Alexandre Queiroz
 *
 */

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FcePesquisaMineralOrigemEnergiaDAOImpl {

	@Inject
	private IDAO<FcePesquisaMineralOrigemEnergia> fcePesquisaMineralOrigemEnergiaDAO;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFcePesquisaMineralOrigemEnergia(List<FcePesquisaMineralOrigemEnergia> ideFcePesquisaMineralOrigemEnergia)  {
		fcePesquisaMineralOrigemEnergiaDAO.salvarEmLote(ideFcePesquisaMineralOrigemEnergia);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FcePesquisaMineralOrigemEnergia> listarFcePesquisaMineralOrigemEnergiaBy(FcePesquisaMineral ideFcePesquisaMineral) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(FcePesquisaMineralOrigemEnergia.class)				
			.createAlias("ideTipoOrigemEnergia", "ideTipoOrigemEnergia")
			.add(Restrictions.eq("ideFcePesquisaMineral.ideFcePesquisaMineral", ideFcePesquisaMineral.getIdeFcePesquisaMineral()))
			
			.setProjection(Projections.projectionList()
				   .add(Projections.property("ideFcePesquisaMineral.ideFcePesquisaMineral"),"ideFcePesquisaMineral.ideFcePesquisaMineral")
				   .add(Projections.property("ideTipoOrigemEnergia.ideTipoOrigemEnergia"),"ideTipoOrigemEnergia.ideTipoOrigemEnergia")
				   .add(Projections.property("ideTipoOrigemEnergia.dscTipoOrigemEnergia"),"ideTipoOrigemEnergia.dscTipoOrigemEnergia")	   
			).setResultTransformer(new AliasToNestedBeanResultTransformer(FcePesquisaMineralOrigemEnergia.class));
			
		return fcePesquisaMineralOrigemEnergiaDAO.listarPorCriteria(detachedCriteria, Order.asc("ideFcePesquisaMineral.ideFcePesquisaMineral"));
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFceSuprimentoEnergia(FcePesquisaMineral ideFcePesquisaMineral)  {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideFcePesquisaMineral", ideFcePesquisaMineral.getIdeFcePesquisaMineral());
		fcePesquisaMineralOrigemEnergiaDAO.executarNamedQuery("FcePesquisaMineralOrigemEnergia.removeByIdeFcePesquisaMineral", parameters);		
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFcePesquisaMineralOrigemEnergiaByNativeQuery(List<FcePesquisaMineralOrigemEnergia> ideFcePesquisaMineralOrigemEnergia)throws Exception  {
//		Map<String, Object> parameters = new HashMap<String, Object>();
//		parameters.put("ideFcePesquisaMineral", ideFcePesquisaMineral.getIdeFcePesquisaMineral());
//		fcePesquisaMineralOrigemEnergiaDAO.executarNamedQuery("FcePesquisaMineralOrigemEnergia.removeByIdeFcePesquisaMineral", parameters);
		
//		fcePesquisaMineralOrigemEnergiaDAO.salvarEmLote(ideFcePesquisaMineralOrigemEnergia);
		for(FcePesquisaMineralOrigemEnergia energia : ideFcePesquisaMineralOrigemEnergia) {
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("ideFcePesquisaMineral", energia.getIdeFcePesquisaMineralOrigemEnergiaPK().getIdeFcePesquisaMineral());
			parametros.put("ideTipoOrigemEnergia", energia.getIdeFcePesquisaMineralOrigemEnergiaPK().getIdeTipoOrigemEnergia());
			
			fcePesquisaMineralOrigemEnergiaDAO.executarNativeQuery("INSERT INTO fce_pesquisa_mineral_origem_energia values(:ideFcePesquisaMineral,:ideTipoOrigemEnergia)", parametros);
		}
	}
}
