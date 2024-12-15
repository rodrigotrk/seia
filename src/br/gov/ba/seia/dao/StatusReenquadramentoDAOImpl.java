package br.gov.ba.seia.dao;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.entity.StatusReenquadramento;
import br.gov.ba.seia.enumerator.StatusReenquadramentoEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class StatusReenquadramentoDAOImpl {

	@Inject
	private IDAO<StatusReenquadramento> statusReenquadramentoDAO;
	
	public Collection<StatusReenquadramento> listarStatusReenquadramento()  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(StatusReenquadramento.class);
		criteria
		.add(Restrictions.not(Restrictions.eq("ideStatusReenquadramento", StatusReenquadramentoEnum.NAO_REENQUADRADO.getId())))
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideStatusReenquadramento"),"ideStatusReenquadramento")	
				.add(Projections.property("nomStatusReenquadramento"),"nomStatusReenquadramento")	
			).add(Restrictions.eq("indAtivo",true))
			.setResultTransformer(new AliasToNestedBeanResultTransformer(StatusReenquadramento.class))
		;
		
		return statusReenquadramentoDAO.listarPorCriteria(criteria, Order.asc("ideStatusReenquadramento"));
	}
	
}