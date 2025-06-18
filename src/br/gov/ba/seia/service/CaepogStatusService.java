package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.Caepog;
import br.gov.ba.seia.entity.CaepogStatus;
import br.gov.ba.seia.entity.CaepogTipoStatus;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CaepogStatusService {

	@Inject
	IDAO<CaepogStatus> caepogStatusDAO;

	@Inject
	IDAO<CaepogTipoStatus> caepogTipoStatusDAO;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CaepogStatus> listarTodosStatusPorCaepog(Caepog c) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CaepogStatus.class, "status")
				.createAlias("status.ideCaepog", "caepog", JoinType.INNER_JOIN)
				.createAlias("status.idePessoaFisica", "pf", JoinType.INNER_JOIN)
				.createAlias("status.ideCaepogTipoStatus", "tipo", JoinType.INNER_JOIN);
		
		if(!Util.isNullOuVazio(c)) {
			criteria.add(Restrictions.eq("caepog.ideCaepog", c.getIdeCaepog()));
		}
		
		return caepogStatusDAO.listarPorCriteria(criteria, Order.asc("ideCaepogStatus"));
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CaepogStatus getUltimoStatusByCaepog(Caepog caepog){
		
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(CaepogStatus.class)
			.add(Restrictions.eq("ideCaepog", caepog))
			.setProjection(Projections.max("ideCaepogStatus"));
		
		Object maxIdeCaepogStatus = caepogStatusDAO.buscarPorCriteria(detachedCriteria);
		
		DetachedCriteria criteria = DetachedCriteria.forClass(CaepogStatus.class)
			.createAlias("ideCaepogTipoStatus", "ideCaepogTipoStatus")
			.add(Restrictions.eq("ideCaepog", caepog))
			.add(Restrictions.eq("ideCaepogStatus", maxIdeCaepogStatus));
		
		return caepogStatusDAO.buscarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarStatus(CaepogStatus s) {
		caepogStatusDAO.salvarOuAtualizar(s);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CaepogTipoStatus> listarTodosCaepogTipoStatus() {
		DetachedCriteria criteria = DetachedCriteria.forClass(CaepogTipoStatus.class);
		return caepogTipoStatusDAO.listarPorCriteria(criteria, Order.asc("ideCaepogTipoStatus"));
	}
}