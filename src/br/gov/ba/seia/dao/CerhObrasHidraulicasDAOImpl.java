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

import br.gov.ba.seia.dao.abstracts.AbstractDAO;
import br.gov.ba.seia.entity.CerhObrasHidraulicas;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhObrasHidraulicasDAOImpl extends AbstractDAO<CerhObrasHidraulicas> {
	private static final long serialVersionUID = 1L;

	@Inject
	private IDAO<CerhObrasHidraulicas> dao;
	
	@Override
	protected IDAO<CerhObrasHidraulicas> getDAO() {
		return dao;
	}
	

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CerhObrasHidraulicas carregarCerhObrasHidraulicas(Integer ideCerhObrasHidraulicas) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CerhObrasHidraulicas.class)
				.add(Restrictions.eq("ideCerhObrasHidraulicas", ideCerhObrasHidraulicas))
				.setProjection(
						Projections.projectionList()
							.add(Projections.property("ideCerhObrasHidraulicas"), "ideCerhObrasHidraulicas")
							.add(Projections.property("dscObrasHidraulicas"), "dscObrasHidraulicas")
							).setResultTransformer(new AliasToNestedBeanResultTransformer(CerhObrasHidraulicas.class))
				;
		return dao.buscarPorCriteria(criteria);
	}

	

}
