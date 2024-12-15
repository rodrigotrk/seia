package br.gov.ba.seia.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
import br.gov.ba.seia.dao.TipologiaGrupoDAOImpl;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.entity.TipologiaGrupo;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipologiaGrupoService {

	@Inject
	private IDAO<TipologiaGrupo> tipologiaGrupoDAO;

	@Inject
	private TipologiaGrupoDAOImpl tipologiaGrupoDAOImpl;
	
	public void salvarTipologiaGrupo(TipologiaGrupo tipologiaGrupo)  {
		tipologiaGrupoDAO.salvarOuAtualizar(tipologiaGrupo);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<TipologiaGrupo> listaTipologiaGrupo() {
		return tipologiaGrupoDAO.listarTodos();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public TipologiaGrupo carregarTipologiaGrupo(Integer id){
		return tipologiaGrupoDAO.carregarGet(id);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public TipologiaGrupo carregarTipologiaGrupoId(Integer id)  {

		DetachedCriteria criteria = DetachedCriteria.forClass(TipologiaGrupo.class);
		criteria.createAlias("idePotencialPoluicao", "potencialpoluicao", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("ideTipologiaGrupo", id));

		return tipologiaGrupoDAO.buscarPorCriteria(criteria);

	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public TipologiaGrupo carregarTipologiaGrupoTipologia(Tipologia tipologia)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(TipologiaGrupo.class);
		criteria.add(Restrictions.eq("ideTipologia.ideTipologia", tipologia.getIdeTipologia()));
		return tipologiaGrupoDAO.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public TipologiaGrupo carregarTipologiaGrupoTipologiaAtivo(Tipologia tipologia)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(TipologiaGrupo.class)
			.createAlias("ideTipologia","t",JoinType.INNER_JOIN)
			.createAlias("idePotencialPoluicao","pp",JoinType.LEFT_OUTER_JOIN)
			.createAlias("unidadeMedidaTipologiaGrupo","umtp",JoinType.INNER_JOIN)
			.createAlias("umtp.ideUnidadeMedida","um",JoinType.INNER_JOIN)
			.add(Restrictions.eq("t.ideTipologia", tipologia.getIdeTipologia()))
			.add(Restrictions.eq("t.indExcluido", false))
			.add(Restrictions.eq("indExcluido", false))
			.setProjection(Projections.projectionList()
					.add(Projections.property("ideTipologiaGrupo"),"ideTipologiaGrupo")
					.add(Projections.property("dtcCriacao"),"dtcCriacao")
					.add(Projections.property("dtcExcluido"),"dtcExcluido")
					.add(Projections.property("indExcluido"),"indExcluido")
					.add(Projections.property("t.ideTipologia"),"ideTipologia.ideTipologia")
					.add(Projections.property("pp.idePotencialPoluicao"),"idePotencialPoluicao.idePotencialPoluicao")
					.add(Projections.property("umtp.ideUnidadeMedidaTipologiaGrupo"),"unidadeMedidaTipologiaGrupo.ideUnidadeMedidaTipologiaGrupo")
					.add(Projections.property("um.ideUnidadeMedida"),"unidadeMedidaTipologiaGrupo.ideUnidadeMedida.ideUnidadeMedida")
					.add(Projections.property("um.codUnidadeMedida"),"unidadeMedidaTipologiaGrupo.ideUnidadeMedida.codUnidadeMedida")
					.add(Projections.property("um.nomUnidadadeMedida"),"unidadeMedidaTipologiaGrupo.ideUnidadeMedida.nomUnidadadeMedida")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(TipologiaGrupo.class))
		;
		
		return tipologiaGrupoDAO.buscarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<TipologiaGrupo> listaTipologiaGrupoTipologia(Tipologia tipologia)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(TipologiaGrupo.class);
		criteria.add(Restrictions.eq("ideTipologia.ideTipologia", tipologia.getIdeTipologia()));
		return tipologiaGrupoDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void deletarTipologiaGrupo(TipologiaGrupo tipologiaGrupo)  {
		tipologiaGrupoDAO.remover(tipologiaGrupo);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void deletarTipologiaGrupoPorNamedQuery(TipologiaGrupo tipologiaGrupo)  {
		Map<String, Object> paramTipologiaGrupo = new HashMap<String, Object>();
		paramTipologiaGrupo.put("ideTipologiaGrupo", tipologiaGrupo.getIdeTipologiaGrupo());
		tipologiaGrupoDAO.executarNamedQuery("TipologiaGrupo.removeTipologiaGrupo", paramTipologiaGrupo);
	}

	public Collection<TipologiaGrupo> buscarPorEmpreendimento(Integer ideEmpreendimento)  {
		return this.tipologiaGrupoDAOImpl.buscarPorEmpreendimento(ideEmpreendimento);
	}
	
	public TipologiaGrupo buscarTipologiaPorEmpreendimento(Integer ideEmpreendimento)  {
		return this.tipologiaGrupoDAOImpl.buscarTipologiaPorEmpreendimento(ideEmpreendimento);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public TipologiaGrupo carregarTipologiaGrupoPorProjection(Tipologia tipologia)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(TipologiaGrupo.class, "tg")
				.createAlias("ideTipologia", "t", JoinType.INNER_JOIN)
				.createAlias("idePotencialPoluicao", "pp", JoinType.INNER_JOIN)
				.createAlias("unidadeMedidaTipologiaGrupo", "umtg", JoinType.INNER_JOIN)
				
				.setProjection(Projections.projectionList()
						.add(Projections.property("ideTipologiaGrupo"), "ideTipologiaGrupo")
						.add(Projections.property("dtcCriacao"), "dtcCriacao")
						.add(Projections.property("dtcExcluido"), "dtcExcluido")
						.add(Projections.property("indExcluido"), "indExcluido")
						.add(Projections.property("t.ideTipologia"), "ideTipologia.ideTipologia")
						.add(Projections.property("pp.idePotencialPoluicao"), "idePotencialPoluicao.idePotencialPoluicao")
						.add(Projections.property("umtg.ideUnidadeMedidaTipologiaGrupo"), "unidadeMedidaTipologiaGrupo.ideUnidadeMedidaTipologiaGrupo"))
				
				.add(Restrictions.eq("t.ideTipologia", tipologia.getIdeTipologia()))
				.add(Restrictions.eq("tg.indExcluido", false))
				
				.setResultTransformer(new AliasToNestedBeanResultTransformer(TipologiaGrupo.class));
		
		return tipologiaGrupoDAO.buscarPorCriteria(criteria);
	}
}
