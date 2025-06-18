package br.gov.ba.seia.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.entity.TipoVinculoPCT;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoVinculoPCTDAOImpl {
	
	@Inject
	private IDAO<TipoVinculoPCT> idao;
	
	public List<TipoVinculoPCT> listarTipoVinculoPCT() throws Exception{
		
		DetachedCriteria criteria = DetachedCriteria.forClass(TipoVinculoPCT.class, "tVP");
		
		criteria.add(Restrictions.eq("tVP.indExcluido", Boolean.FALSE)); 
		
		return idao.listarPorCriteria(criteria, Order.asc("tVP.ideTipoVinculoPCT"));
	}

}
