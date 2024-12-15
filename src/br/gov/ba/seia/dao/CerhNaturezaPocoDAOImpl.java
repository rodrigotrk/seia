package br.gov.ba.seia.dao;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.entity.CerhNaturezaPoco;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

/**
 * @author eduardo.fernandes 
 * @since 20/04/2017
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhNaturezaPocoDAOImpl {

	@Inject
	private IDAO<CerhNaturezaPoco> dao;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<CerhNaturezaPoco> listarTodos() {
		return dao.listarTodos();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CerhNaturezaPoco carregar(Integer ide) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CerhNaturezaPoco.class)
				.add(Restrictions.eq("ideCerhNaturezaPoco", ide))
				.setProjection(
						Projections.projectionList()
							.add(Projections.property("ideCerhNaturezaPoco"), "ideCerhNaturezaPoco")
							.add(Projections.property("dscNaturezaPoco"), "dscNaturezaPoco")
						)
				.setResultTransformer(new AliasToNestedBeanResultTransformer(CerhNaturezaPoco.class))
				;
		return dao.buscarPorCriteria(criteria);
	}
	
}
