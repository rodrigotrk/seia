package br.gov.ba.seia.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.abstracts.AbstractDAO;
import br.gov.ba.seia.entity.Pais;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PaisDAOImpl extends AbstractDAO<Pais> {
	private static final long serialVersionUID = 1L;

	@Inject
	private IDAO<Pais> listPaisDaoImpl;
	
	
	@Override
	protected IDAO<Pais> getDAO() {
		return listPaisDaoImpl;
	}
	
	@Deprecated
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Pais> getPaises(Pais pais) throws Exception {
		return 
			listPaisDaoImpl.listarPorCriteria(getRestrictions(pais,
				DetachedCriteria.forClass(Pais.class)
					.addOrder(Order.asc("idePais"))
					.setProjection(Projections.projectionList()
						.add(Projections.property("idePais"),"idePais")
						.add(Projections.property("nomPais"),"nomPais"))
					.setResultTransformer(new AliasToNestedBeanResultTransformer(Pais.class))));	
    }
	
	
	private DetachedCriteria getRestrictions(Pais pais, DetachedCriteria criteria){
		if(pais != null){
			if(pais.getIdePais()!= null){
				criteria
					.add(Restrictions.eq("idePais", pais.getIdePais()));
			}
			if(pais.getNomPais()!= null){
				criteria
					.add(Restrictions.ilike("nomPais", pais.getNomPais(), MatchMode.START));
			}
		}
		return criteria;
	}


}
