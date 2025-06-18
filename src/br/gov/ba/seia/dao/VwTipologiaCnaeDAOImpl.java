package br.gov.ba.seia.dao;

import java.util.List;

import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.entity.VwTipologiaCnae;
import br.gov.ba.seia.util.Util;

public class VwTipologiaCnaeDAOImpl {
	
	@Inject
	IDAO<VwTipologiaCnae> vwTipologiaCnaeDAO;

    public List<VwTipologiaCnae> getVwTipologiaCnaes(VwTipologiaCnae pVwTipologiaCnae)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(VwTipologiaCnae.class,"vwTipologiaCnae");
		
		if (!Util.isNullOuVazio(pVwTipologiaCnae.getCodTipologia())){
			criteria.add(Restrictions.ilike("codTipologia", pVwTipologiaCnae.getCodTipologia(), MatchMode.ANYWHERE));
		}	
		
    	if (!Util.isNullOuVazio(pVwTipologiaCnae.getDesTipologia())){
    		criteria.add(Restrictions.ilike("desTipologia", pVwTipologiaCnae.getDesTipologia(),MatchMode.ANYWHERE));
    	}
    	
    	
    	if (!Util.isNullOuVazio(pVwTipologiaCnae.getIdeNivelTipologia())){
    		criteria.add(Restrictions.eq("ideNivelTipologia", pVwTipologiaCnae.getIdeNivelTipologia()));
    	}
    	
    	
    	return vwTipologiaCnaeDAO.listarPorCriteria(criteria, Order.asc("id"));
    }
	
	
	
    public List<VwTipologiaCnae> getListBuscarQuery() {
		return vwTipologiaCnaeDAO.buscarPorNamedQuery("VwTipologiaCnae.findAll");
    }
    
    
       
    
    
}