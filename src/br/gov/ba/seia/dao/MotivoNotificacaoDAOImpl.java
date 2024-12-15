package br.gov.ba.seia.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.MotivoNotificacao;
import br.gov.ba.seia.entity.Notificacao;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class MotivoNotificacaoDAOImpl {
	
	@Inject
	private IDAO<MotivoNotificacao> daoMotivoNotificacao;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<MotivoNotificacao> listarTodos() {
		return daoMotivoNotificacao.listarTodos();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<MotivoNotificacao> listarMotivoNotificacaoComEnvioShape(Integer ideNotificacao)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(MotivoNotificacao.class);
		criteria
			.createAlias("notificacaoMotivoNotificacaoCollection", "nmn", JoinType.INNER_JOIN)
			.createAlias("nmn.ideNotificacao", "n", JoinType.INNER_JOIN)
			
			.add(Restrictions.eq("indEnvioShape", true))
			.add(Restrictions.eq("n.ideNotificacao", ideNotificacao))
			.setResultTransformer(Criteria.ROOT_ENTITY)
		;
		
		return daoMotivoNotificacao.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<MotivoNotificacao> listarMotivoNotificacaoSemEnvioShape(Notificacao notificacao)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(MotivoNotificacao.class);
		criteria
			.add(Restrictions.conjunction()
				.add(Restrictions.eq("indNotPrazo", notificacao.isNotificacaoPrazo()))
				.add(Restrictions.eq("indNotComunicacao", notificacao.isNotificacaoComunicacao()))
				.add(Restrictions.eq("indNotHomologacao", notificacao.isNotificacaoHomologacao()))
				.add(Restrictions.eq("indEnvioShape", false))
			)
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideMotivoNotificacao"),"ideMotivoNotificacao")
				.add(Projections.property("nomMotivoNotificacao"),"nomMotivoNotificacao")
				.add(Projections.property("indNotPrazo"),"indNotPrazo")
				.add(Projections.property("indNotComunicacao"),"indNotComunicacao")
				.add(Projections.property("indNotHomologacao"),"indNotHomologacao")
				.add(Projections.property("indEnvioShape"),"indEnvioShape")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(MotivoNotificacao.class))
		;
		
		return daoMotivoNotificacao.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<MotivoNotificacao> listarMotivoNotificacao(Notificacao notificacao)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(MotivoNotificacao.class);
		criteria
			.createAlias("notificacaoMotivoNotificacaoCollection", "nmn", JoinType.INNER_JOIN)
			.createAlias("nmn.ideNotificacao", "n", JoinType.INNER_JOIN)
			.add(Restrictions.eq("n.ideNotificacao", notificacao.getIdeNotificacao()))
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideMotivoNotificacao"),"ideMotivoNotificacao")
				.add(Projections.property("nomMotivoNotificacao"),"nomMotivoNotificacao")
				.add(Projections.property("indNotPrazo"),"indNotPrazo")
				.add(Projections.property("indNotComunicacao"),"indNotComunicacao")
				.add(Projections.property("indNotHomologacao"),"indNotHomologacao")
				.add(Projections.property("indEnvioShape"),"indEnvioShape")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(MotivoNotificacao.class))
		;
		
		return daoMotivoNotificacao.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<MotivoNotificacao> listarMotivoNotificacaoEnvioShape(Notificacao notificacao)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(MotivoNotificacao.class);
		
		criteria
			.add(Restrictions.conjunction()
				.add(Restrictions.eq("indNotPrazo", notificacao.isNotificacaoPrazo()))
				.add(Restrictions.eq("indNotComunicacao", notificacao.isNotificacaoComunicacao()))
				.add(Restrictions.eq("indNotHomologacao", notificacao.isNotificacaoHomologacao()))
				.add(Restrictions.eq("indEnvioShape", true))
			)
			
			.setResultTransformer(Criteria.ROOT_ENTITY);
		
		return daoMotivoNotificacao.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<MotivoNotificacao> listarByIdeNotificacao(Integer ideNotificacao) {
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("ideNotificacao", ideNotificacao);
		return daoMotivoNotificacao.buscarPorNamedQuery("MotivoNotificacao.findByIdeNotificacao",parametros);
	}

	public MotivoNotificacao carregar(Integer id){
		return daoMotivoNotificacao.carregarGet(id);
	}
}