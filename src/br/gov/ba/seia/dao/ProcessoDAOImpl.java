
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

import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.TransferenciaAmbiental;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

/**
 * @author Alexandre Queiroz
 *
 */

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ProcessoDAOImpl {
	
	@Inject
	IDAO<Processo> processoDAO;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Processo getIdeProcessoTla(Processo processo) {
			
		DetachedCriteria detachedCriteria =DetachedCriteria.forClass(TransferenciaAmbiental.class)
			.add(Restrictions.eq("ideProcessoTla", processo.getIdeProcesso()))			
			.setProjection(Projections.projectionList()
			    .add(Projections.groupProperty("ideProcessoTla")) 	
				.add(Projections.property("ideProcessoTla"),"ideProcessoTla"))
			.setResultTransformer(new AliasToNestedBeanResultTransformer(Processo.class));

		return processoDAO.buscarPorCriteria(detachedCriteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Processo buscarProcessoPor(String numProcesso){
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Processo.class);		
		criteria
			.add(Restrictions.eq("numProcesso", numProcesso))
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideProcesso"),"ideProcesso")
				.add(Projections.property("numProcesso"),"numProcesso")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(Processo.class))
		;
		
		
		return processoDAO.buscarPorCriteria(criteria);
	}
	
}
