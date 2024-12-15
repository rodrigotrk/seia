package br.gov.ba.seia.dao;

import java.util.List;
import java.util.Map;

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
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;

import br.gov.ba.seia.entity.ProcessoSinaflor;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ProcessoSinaflorDAOImpl {
	
	@Inject
	private IDAO<ProcessoSinaflor> processoSinaflorDAO;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void salvar(ProcessoSinaflor processoSinaflor)  {
		processoSinaflorDAO.salvar(processoSinaflor);
	}	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProcessoSinaflor> listarProcessoSinaflor(Map<String, Object> params, Integer first, Integer pageSize)  {
		
		DetachedCriteria criteria = listarProcessoSinaflorCriteria(params);
		criteria	
			.addOrder(Order.desc("ps1.dtcSincronizacao"))
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideProcessoSinaflor"),"ideProcessoSinaflor")
				.add(Projections.property("token"),"token")
				.add(Projections.property("dtcSincronizacao"),"dtcSincronizacao")
				.add(Projections.property("indConcluido"),"indConcluido")
				.add(Projections.property("dscLog"),"dscLog")
				.add(Projections.property("p.ideProcesso"),"ideProcesso.ideProcesso")
				.add(Projections.property("p.numProcesso"),"ideProcesso.numProcesso")
			)
			
			.setResultTransformer(new AliasToNestedBeanResultTransformer(ProcessoSinaflor.class))
		;
		
		
		return processoSinaflorDAO.listarPorCriteriaDemanda(criteria, first, pageSize);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Integer listarProcessoSinaflorCount(Map<String, Object> params)  {
		
		DetachedCriteria criteria = listarProcessoSinaflorCriteria(params);
		criteria.setProjection(Projections.groupProperty("ps1.ideProcessoSinaflor"));
		
		DetachedCriteria criteriaCount = DetachedCriteria.forClass(ProcessoSinaflor.class);
		criteriaCount.add(Property.forName("ideProcessoSinaflor").in(criteria));
		
		return processoSinaflorDAO.count(criteriaCount);
	}

	private DetachedCriteria listarProcessoSinaflorCriteria(Map<String, Object> params) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProcessoSinaflor.class, "ps1");
		criteria
			.createAlias("ps1.ideProcesso", "p", JoinType.INNER_JOIN)
			.add(Restrictions.eq("ps1.indConcluido", false))
			.add(Property.forName("ps1.ideProcessoSinaflor").eq(
				DetachedCriteria.forClass(ProcessoSinaflor.class, "ps2")
					.add(Restrictions.eqProperty("ps1.ideProcesso", "ps2.ideProcesso"))
					.setProjection(Projections.projectionList()
						.add(Projections.sqlGroupProjection("max({alias}.ide_processo_sinaflor) as ide_processo_sinaflor", "{alias}.ide_processo", new String[] { "ide_processo_sinaflor" }, new Type[] { StandardBasicTypes.INTEGER }))
					)
				)
			)
		;
		
		if(!Util.isNullOuVazio(params.get("numProcesso"))) {
			String numProcesso = (String) params.get("numProcesso");
			criteria.add(Restrictions.ilike("p.numProcesso", numProcesso, MatchMode.ANYWHERE));
		}
		
		return criteria;
	}	
}