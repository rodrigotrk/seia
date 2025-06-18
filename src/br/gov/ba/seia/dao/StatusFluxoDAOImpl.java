package br.gov.ba.seia.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.entity.StatusFluxo;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class StatusFluxoDAOImpl {
	
	private IDAO<StatusFluxo> statusFluxoDAO;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<StatusFluxo> listarStatusFluxo(StatusFluxo pStatusFluxo)  {
    	
    	DetachedCriteria criteria = DetachedCriteria.forClass(StatusFluxo.class);
    	
    	if (!Util.isNull(pStatusFluxo)) {
    		criteria.add(Restrictions.eq("indExcluido", pStatusFluxo.getIndExcluido()));
    		criteria.add(Restrictions.eq("ideStatusFluxo", pStatusFluxo.getIdeStatusFluxo()));
    		
    	}
    	
    	criteria
    		.setProjection(Projections.projectionList()
    			.add(Projections.property("ideStatusFluxo"), "ideStatusFluxo")
    			.add(Projections.property("dscStatusFluxo"), "dscStatusFluxo")
    			.add(Projections.property("indExcluido"), "indExcluido")
    			.add(Projections.property("dtcCriacao"), "dtcCriacao")
    			.add(Projections.property("dtcExclusao"), "dtcExclusao")
    			.add(Projections.property("dscStatusFluxoExterno"), "dscStatusFluxoExterno")
    		)
    		.setResultTransformer(new AliasToNestedBeanResultTransformer(StatusFluxo.class))
    	;
    	
    	return statusFluxoDAO.listarPorCriteria(criteria, Order.asc("ideStatusFluxo"));
    	
    }
}