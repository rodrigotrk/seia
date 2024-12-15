package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.PagamentoReposicaoFlorestal;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PagamentoReposicaoFlorestalService {

	@Inject
	private IDAO<PagamentoReposicaoFlorestal> pagamentoReposicaoFlorestalDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<PagamentoReposicaoFlorestal> listarPagamentoReposicaoFlorestalPai()  {
		DetachedCriteria criteria = DetachedCriteria.forClass(PagamentoReposicaoFlorestal.class);
		
		criteria.add(Restrictions.isNull("idePagamentoReposicaoFlorestalPai"));
		
		criteria.setProjection(
				Projections.projectionList()
					.add(Projections.property("idePagamentoReposicaoFlorestal"), "idePagamentoReposicaoFlorestal")
					.add(Projections.property("nomPagamentoReposicaoFlorestal"), "nomPagamentoReposicaoFlorestal")
				).setResultTransformer(new AliasToNestedBeanResultTransformer(PagamentoReposicaoFlorestal.class))
				;
		return pagamentoReposicaoFlorestalDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<PagamentoReposicaoFlorestal> listarPagamentoReposicaoFlorestalFilho(PagamentoReposicaoFlorestal pagamentoReposicaoFlorestal)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(PagamentoReposicaoFlorestal.class);
		
		criteria.createAlias("idePagamentoReposicaoFlorestalPai", "pai", JoinType.LEFT_OUTER_JOIN);
		
		criteria.add(Restrictions.eq("idePagamentoReposicaoFlorestalPai.idePagamentoReposicaoFlorestal", pagamentoReposicaoFlorestal.getIdePagamentoReposicaoFlorestal()));
		
		criteria.setProjection(
				Projections.projectionList()
					.add(Projections.property("idePagamentoReposicaoFlorestal"), "idePagamentoReposicaoFlorestal")
					.add(Projections.property("nomPagamentoReposicaoFlorestal"), "nomPagamentoReposicaoFlorestal")
					.add(Projections.property("pai.idePagamentoReposicaoFlorestal"), "idePagamentoReposicaoFlorestalPai.idePagamentoReposicaoFlorestal")
					.add(Projections.property("pai.nomPagamentoReposicaoFlorestal"), "idePagamentoReposicaoFlorestalPai.nomPagamentoReposicaoFlorestal")
				).setResultTransformer(new AliasToNestedBeanResultTransformer(PagamentoReposicaoFlorestal.class));
		
		return pagamentoReposicaoFlorestalDAO.listarPorCriteria(criteria);
	}
}
