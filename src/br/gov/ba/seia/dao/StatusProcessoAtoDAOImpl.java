package br.gov.ba.seia.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.StatusProcessoAto;
import br.gov.ba.seia.enumerator.StatusProcessoAtoEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class StatusProcessoAtoDAOImpl {
	
	@Inject
	private IDAO<StatusProcessoAto> statusProcessoAtoDAO;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<StatusProcessoAto> listarTodos()  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(StatusProcessoAto.class);
		criteria
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideStatusProcessoAto"),"ideStatusProcessoAto")
				.add(Projections.property("nomStatusProcessoAto"),"nomStatusProcessoAto")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(StatusProcessoAto.class))
		;
		
		return statusProcessoAtoDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<StatusProcessoAto> listaParaAnaliseTecnica()  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(StatusProcessoAto.class);
		criteria
			.add(Restrictions.in("ideStatusProcessoAto", new Integer[] {
						StatusProcessoAtoEnum.DEFERIDO_PELO_TECNICO.getId(),
						StatusProcessoAtoEnum.INDEFERIDO_PELO_TECNICO.getId()
					}
				)
			)
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideStatusProcessoAto"),"ideStatusProcessoAto")
				.add(Projections.property("nomStatusProcessoAto"),"nomStatusProcessoAto")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(StatusProcessoAto.class))
		;
		
		return statusProcessoAtoDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public StatusProcessoAto buscarStatusProcessoAtoPor(Integer ideProcesso, Integer ideAtoAmbiental){
		
		DetachedCriteria criteria = DetachedCriteria.forClass(StatusProcessoAto.class)

			.add(Property.forName("ideStatusProcessoAto").eq(
					DetachedCriteria.forClass(StatusProcessoAto.class)
					
					.createAlias("controleProcessoAtoCollection", "cpa", JoinType.INNER_JOIN)
					.createAlias("cpa.ideProcessoAto", "pa", JoinType.INNER_JOIN)
					.createAlias("pa.processo", "p", JoinType.INNER_JOIN)
					.createAlias("pa.atoAmbiental", "aa", JoinType.INNER_JOIN)
					
					.add(Restrictions.eq("p.ideProcesso", ideProcesso))
					.add(Restrictions.eq("aa.ideAtoAmbiental", ideAtoAmbiental))
					
					.setProjection(Projections.max("ideStatusProcessoAto"))					
			))
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideStatusProcessoAto"),"ideStatusProcessoAto")
				.add(Projections.property("nomStatusProcessoAto"),"nomStatusProcessoAto")
//				.add(Projections.groupProperty("ideStatusProcessoAto"),"ideStatusProcessoAto")
//				.add(Projections.groupProperty("nomStatusProcessoAto"),"nomStatusProcessoAto")
			)
			
			.setResultTransformer(new AliasToNestedBeanResultTransformer(StatusProcessoAto.class));
			
		return statusProcessoAtoDAO.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizar(StatusProcessoAto statusProcessoAto) {
		statusProcessoAtoDAO.salvarOuAtualizar(statusProcessoAto);
	}
}