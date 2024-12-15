package br.gov.ba.seia.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.entity.TipoSeguimentoPct;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoSeguimentoPctDAOImpl {

	@Inject
	private IDAO<TipoSeguimentoPct> idao;
	
	public List<TipoSeguimentoPct> listarTipoSeguimentoPct() throws Exception{
		DetachedCriteria criteria = DetachedCriteria.forClass(TipoSeguimentoPct.class);
		
		criteria.add(Restrictions.eq("indExcluido", Boolean.FALSE));
		
		return idao.listarPorCriteria(criteria, Order.asc("dscTipoSeguimentoPct"));
	}

	public TipoSeguimentoPct obterTipoSeguimentoPct(Integer ideTipoSeguimentoPct) throws Exception {
		return idao.carregarGet(ideTipoSeguimentoPct);
		
	}
	
}
