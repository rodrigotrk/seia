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
import br.gov.ba.seia.entity.ConsumidorReposicaoFlorestal;
import br.gov.ba.seia.entity.NumeroCarReposicaoFlorestal;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ConsumidorReposicaoFlorestalService {

	@Inject
	private IDAO<ConsumidorReposicaoFlorestal> consumidorReposicaoFlorestalDAO;
	
	@Inject
	private IDAO<NumeroCarReposicaoFlorestal> numeroCarReposicaoFlorestalDAO;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<NumeroCarReposicaoFlorestal> listarNumeroCarReposicaoFlorestalPorConsumidor(Integer ideConsumidorReposicaoFlorestal) {
		DetachedCriteria criteria = DetachedCriteria.forClass(NumeroCarReposicaoFlorestal.class);
		
		criteria.createAlias("ideConsumidorReposicaoFlorestal", "crf", JoinType.INNER_JOIN);
		
		criteria.add(Restrictions.eq("crf.ideConsumidorReposicaoFlorestal", ideConsumidorReposicaoFlorestal));
		
		criteria.setProjection(
				Projections.projectionList()
					.add(Projections.property("ideNumeroCarReposicaoFlorestal"), "ideNumeroCarReposicaoFlorestal")
					.add(Projections.property("crf.ideConsumidorReposicaoFlorestal"), "ideConsumidorReposicaoFlorestal.ideConsumidorReposicaoFlorestal")
					.add(Projections.property("numCAR"), "numCAR")
				).setResultTransformer(new AliasToNestedBeanResultTransformer(NumeroCarReposicaoFlorestal.class))
				;
		return numeroCarReposicaoFlorestalDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ConsumidorReposicaoFlorestal obterConsumidorReposicaoFlorestalPorCumprimentoReposicaoFlorestal(Integer ideCumprimentoReposicaoFlorestal)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(ConsumidorReposicaoFlorestal.class);
		
		criteria.createAlias("ideCumprimentoReposicaoFlorestal", "crf", JoinType.INNER_JOIN);
		criteria.createAlias("ideUnidadeMedida", "um", JoinType.INNER_JOIN);
		criteria.add(Restrictions.eq("ideCumprimentoReposicaoFlorestal.ideCumprimentoReposicaoFlorestal", ideCumprimentoReposicaoFlorestal));
		
		criteria.setProjection(
				Projections.projectionList()
					.add(Projections.property("ideConsumidorReposicaoFlorestal"), "ideConsumidorReposicaoFlorestal")
					.add(Projections.property("crf.ideCumprimentoReposicaoFlorestal"), "ideCumprimentoReposicaoFlorestal.ideCumprimentoReposicaoFlorestal")
					.add(Projections.property("vlmMaterialLenhosoConsumido"), "vlmMaterialLenhosoConsumido")
					.add(Projections.property("numPortariaAtoAdquirido"), "numPortariaAtoAdquirido")
					.add(Projections.property("dataPublicacaoPortaria"), "dataPublicacaoPortaria")
					.add(Projections.property("numProcessoOriginal"), "numProcessoOriginal")
					.add(Projections.property("indPossuiNumeroCAR"), "indPossuiNumeroCAR")
					.add(Projections.property("um.ideUnidadeMedida"), "ideUnidadeMedida.ideUnidadeMedida")
					.add(Projections.property("um.codUnidadeMedida"), "ideUnidadeMedida.codUnidadeMedida")
					
				).setResultTransformer(new AliasToNestedBeanResultTransformer(ConsumidorReposicaoFlorestal.class))
				;
		ConsumidorReposicaoFlorestal consumidorReposicaoFlorestal = consumidorReposicaoFlorestalDAO.buscarPorCriteria(criteria);
		
		if (!Util.isNullOuVazio(consumidorReposicaoFlorestal)) {
			consumidorReposicaoFlorestal.setNumeroCarReposicaoFlorestalCollection(listarNumeroCarReposicaoFlorestalPorConsumidor(consumidorReposicaoFlorestal.getIdeConsumidorReposicaoFlorestal()));
		}
		
		return consumidorReposicaoFlorestal;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ConsumidorReposicaoFlorestal obterConsumidorReposicaoFlorestalPorRequerimento(Integer ideRequerimento)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(ConsumidorReposicaoFlorestal.class);
		
		criteria.createAlias("ideCumprimentoReposicaoFlorestal", "crf", JoinType.INNER_JOIN);
		criteria.createAlias("crf.requerimento", "req", JoinType.INNER_JOIN);
		
		criteria.add(Restrictions.eq("req.ideRequerimento", ideRequerimento));
		
		criteria.setProjection(
				Projections.projectionList()
					.add(Projections.property("ideConsumidorReposicaoFlorestal"), "ideConsumidorReposicaoFlorestal")
					.add(Projections.property("req.ideRequerimento"), "ideConsumidorReposicaoFlorestal.requerimento.ideRequerimento")
					.add(Projections.property("crf.ideCumprimentoReposicaoFlorestal"), "ideCumprimentoReposicaoFlorestal.ideCumprimentoReposicaoFlorestal")
					.add(Projections.property("vlmMaterialLenhosoConsumido"), "vlmMaterialLenhosoConsumido")
					.add(Projections.property("numPortariaAtoAdquirido"), "numPortariaAtoAdquirido")
					.add(Projections.property("dataPublicacaoPortaria"), "dataPublicacaoPortaria")
					.add(Projections.property("numProcessoOriginal"), "numProcessoOriginal")
					.add(Projections.property("indPossuiNumeroCAR"), "indPossuiNumeroCAR")
					
				).setResultTransformer(new AliasToNestedBeanResultTransformer(ConsumidorReposicaoFlorestal.class))
				;
		ConsumidorReposicaoFlorestal consumidorReposicaoFlorestal = consumidorReposicaoFlorestalDAO.buscarPorCriteria(criteria);
		
		if (!Util.isNullOuVazio(consumidorReposicaoFlorestal)) {
			consumidorReposicaoFlorestal.setNumeroCarReposicaoFlorestalCollection(listarNumeroCarReposicaoFlorestalPorConsumidor(consumidorReposicaoFlorestal.getIdeConsumidorReposicaoFlorestal()));
		}
		
		return consumidorReposicaoFlorestal;
	}
	
}
