package br.gov.ba.seia.dao;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.FcePesquisaMineral;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

/**
 * @author Alexandre Queiroz
 *
 */

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FcePesquisaMineralDAOImpl {

	@Inject
	private IDAO<FcePesquisaMineral> fcePesquisaMineralDAO;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFcePesquisaMineral(FcePesquisaMineral ideFcePesquisaMineral)  {
		fcePesquisaMineralDAO.salvarOuAtualizar(ideFcePesquisaMineral);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FcePesquisaMineral getFcePesquisaMineralBy(Fce ideFce)  {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(FcePesquisaMineral.class)		
			.add(Restrictions.eq("ideFce.ideFce", ideFce.getIdeFce()))   
			.setProjection(Projections.projectionList()
			   .add(Projections.groupProperty("ideFcePesquisaMineral"),"ideFcePesquisaMineral")
			   .add(Projections.groupProperty("indEsferaEstadual"),"indEsferaEstadual")
			   .add(Projections.groupProperty("indEsferaFederal"),"indEsferaFederal")
			   .add(Projections.groupProperty("indPesqIntervRecursoHidrico"),"indPesqIntervRecursoHidrico")
			   .add(Projections.groupProperty("indSupressao"),"indSupressao") 
			).setResultTransformer(new AliasToNestedBeanResultTransformer(FcePesquisaMineral.class)) ; 
			
		return fcePesquisaMineralDAO.buscarPorCriteria(detachedCriteria);
	}
	
	

}
