package br.gov.ba.seia.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.EspecieFlorestal;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class EspecieFlorestalDAOImpl {

	@Inject
	private IDAO<EspecieFlorestal> idao;
	
	public List<EspecieFlorestal> listarEspecieFlorestal(Integer ideTipoEspecieFlorestal) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(EspecieFlorestal.class, "eF");
		
		criteria.createAlias("eF.ideTipoEspecieFlorestal", "tEF", JoinType.INNER_JOIN);
		criteria.add(Restrictions.eq("eF.indAtivo", Boolean.TRUE));
		
		criteria.add(Restrictions.eq("eF.ideTipoEspecieFlorestal.ideTipoEspecieFlorestal", ideTipoEspecieFlorestal));
		
		
		return idao.listarPorCriteria(criteria, Order.asc("eF.ideEspecieFlorestal"));
	}
}
