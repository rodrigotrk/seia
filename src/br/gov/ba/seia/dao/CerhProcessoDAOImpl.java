package br.gov.ba.seia.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.apache.log4j.Level;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.abstracts.AbstractDAO;
import br.gov.ba.seia.entity.Cerh;
import br.gov.ba.seia.entity.CerhProcesso;
import br.gov.ba.seia.enumerator.TipoUsoRecursoHidricoEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;


@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhProcessoDAOImpl extends AbstractDAO<CerhProcesso>{
	private static final long serialVersionUID = 1L;

	@Inject
	private IDAO<CerhProcesso> cerhProcesoDAO;
	
	@Inject
	private IDAO<Integer> ideDAO;
	
	@Override
	protected IDAO<CerhProcesso> getDAO() {
		return cerhProcesoDAO;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(CerhProcesso cerhProcesso) {
		cerhProcesoDAO.salvarOuAtualizar(cerhProcesso);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarEmLote(Collection<CerhProcesso> listaCerhProcesso) {
		cerhProcesoDAO.salvarEmLote((List<CerhProcesso>) listaCerhProcesso);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CerhProcesso carregarCerhProcesso(CerhProcesso cerhProcesso) {
		try{
			
			return cerhProcesoDAO.buscarPorCriteria(
				DetachedCriteria.forClass(CerhProcesso.class)
					.createAlias("ideCerhSituacaoRegularizacao", "ideCerhSituacaoRegularizacao", JoinType.LEFT_OUTER_JOIN)
					.add(Restrictions.eq("ideCerhProcesso", cerhProcesso.getIdeCerhProcesso()))
					.setProjection(Projections.projectionList()
						.add(Projections.property("ideCerhProcesso"), "ideCerhProcesso")
						.add(Projections.property("dtInicioAutorizacao"), "dtInicioAutorizacao")
						.add(Projections.property("indDadosConcedidos"), "indDadosConcedidos")
						.add(Projections.property("indPossuiCartaInexigibilidade"), "indPossuiCartaInexigibilidade")
						.add(Projections.property("numPortariaDocumento"), "numPortariaDocumento")
						.add(Projections.property("numPrazoValidade"), "numPrazoValidade")
						.add(Projections.property("numProcesso"), "numProcesso")
						.add(Projections.property("indOutorgaReferentePontoCadastradoCerh"), "indOutorgaReferentePontoCadastradoCerh")
						.add(Projections.property("ideCerhSituacaoRegularizacao.ideCerhSituacaoRegularizacao"), "ideCerhSituacaoRegularizacao.ideCerhSituacaoRegularizacao")
						.add(Projections.property("ideCerhSituacaoRegularizacao.dscSituacaoRegularizacao"), "ideCerhSituacaoRegularizacao.dscSituacaoRegularizacao")
				).setResultTransformer(new AliasToNestedBeanResultTransformer(CerhProcesso.class))
			);
		}catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<CerhProcesso> listarCerhProcessoPor(Cerh cerh) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CerhProcesso.class)
			.createAlias("ideCerh", "c", JoinType.INNER_JOIN)
			.createAlias("ideCerhSituacaoRegularizacao", "csr", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideCerhTipoAtoDispensa", "ctad", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideCerhTipoAutorizacaoOutorgado", "ctao", JoinType.LEFT_OUTER_JOIN)
			
			.add(Restrictions.eq("ideCerh", cerh))
			.addOrder(Order.asc("ideCerhProcesso"))
			.setProjection(Projections.projectionList()
				.add(Projections.groupProperty("c.ideCerh"), "ideCerh.ideCerh")

				.add(Projections.groupProperty("ideCerhProcesso"), "ideCerhProcesso")
				.add(Projections.groupProperty("dtInicioAutorizacao"), "dtInicioAutorizacao")
				.add(Projections.groupProperty("indDadosConcedidos"), "indDadosConcedidos")
				.add(Projections.groupProperty("indPossuiCartaInexigibilidade"), "indPossuiCartaInexigibilidade")
				.add(Projections.groupProperty("numPortariaDocumento"), "numPortariaDocumento")
				.add(Projections.groupProperty("numPrazoValidade"), "numPrazoValidade")
				.add(Projections.groupProperty("numProcesso"), "numProcesso")
				.add(Projections.groupProperty("indOutorgaReferentePontoCadastradoCerh"), "indOutorgaReferentePontoCadastradoCerh")
				.add(Projections.groupProperty("ideSistema.ideSistema"), "ideSistema.ideSistema")
				.add(Projections.groupProperty("ideObjetoPai"), "ideObjetoPai")
				
				.add(Projections.groupProperty("csr.ideCerhSituacaoRegularizacao"), "ideCerhSituacaoRegularizacao.ideCerhSituacaoRegularizacao")
				.add(Projections.groupProperty("csr.dscSituacaoRegularizacao"), "ideCerhSituacaoRegularizacao.dscSituacaoRegularizacao")
				.add(Projections.groupProperty("csr.ideObjetoPai"), "ideCerhSituacaoRegularizacao.ideObjetoPai")

				.add(Projections.groupProperty("ctad.ideCerhTipoAtoDispensa"), "ideCerhTipoAtoDispensa.ideCerhTipoAtoDispensa")
				.add(Projections.groupProperty("ctad.ideObjetoPai"), "ideCerhTipoAtoDispensa.ideObjetoPai")
				
				.add(Projections.groupProperty("ctao.ideCerhTipoAutorizacaoOutorgado"), "ideCerhTipoAutorizacaoOutorgado.ideCerhTipoAutorizacaoOutorgado")
				.add(Projections.groupProperty("ctao.ideObjetoPai"), "ideCerhTipoAutorizacaoOutorgado.ideObjetoPai")
			).setResultTransformer(new AliasToNestedBeanResultTransformer(CerhProcesso.class));
		return cerhProcesoDAO.listarPorCriteria(criteria);
		
	}
	
	public void gerenciarCerhProcessoRemovido(Cerh cerh) {
		Collection<Integer> listaRemocao = listarIdeCerhProcesso(cerh);
		if(!Util.isNullOuVazio(listaRemocao)) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("listaRemocao", listaRemocao);
			
			StringBuilder hql = null;
			
			hql = new StringBuilder();
			hql.append("delete from CerhLocalizacaoGeografica clg where clg.ideCerhProcesso.ideCerhProcesso in (select cp.ideCerhProcesso from CerhProcesso cp where cp.ideCerhProcesso in (:listaRemocao))");
			cerhProcesoDAO.executarQuery(hql.toString(), params);
			
			hql = new StringBuilder();
			hql.append("delete from CerhTipoUso ctu where ctu.ideCerhProcesso.ideCerhProcesso in (select cp.ideCerhProcesso from CerhProcesso cp where cp.ideCerhProcesso in (:listaRemocao))");
			cerhProcesoDAO.executarQuery(hql.toString(), params);
			
			
			hql = new StringBuilder();
			hql.append("delete from CerhProcessoSuspensao cps where cps.ideCerhProcesso.ideCerhProcesso in (:listaRemocao)");
			cerhProcesoDAO.executarQuery(hql.toString(), params);
			
			hql = new StringBuilder();
			hql.append("delete from CerhProcesso cp where cp.ideCerhProcesso in (:listaRemocao)");
			cerhProcesoDAO.executarQuery(hql.toString(), params);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CerhProcesso> listarCerhProcessoPorCerh(Cerh ideCerh, TipoUsoRecursoHidricoEnum tipoRecursoHidricoEnum){
		try{
			DetachedCriteria criteria = DetachedCriteria.forClass(CerhProcesso.class)
				.createAlias("cerhTipoUsoCollection", "cerhTipoUso")
				.createAlias("cerhTipoUso.ideCerh","cerh")
			
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideCerhProcesso"),"ideCerhProcesso")
				.add(Projections.property("dtInicioAutorizacao"),"dtInicioAutorizacao")
				.add(Projections.property("indDadosConcedidos"),"indDadosConcedidos")
				.add(Projections.property("indPossuiCartaInexigibilidade"),"indPossuiCartaInexigibilidade")
				.add(Projections.property("numPortariaDocumento"),"numPortariaDocumento")
				.add(Projections.property("numPrazoValidade"),"numPrazoValidade")
				.add(Projections.property("numProcesso"),"numProcesso")
				.add(Projections.property("indOutorgaReferentePontoCadastradoCerh"),"indOutorgaReferentePontoCadastradoCerh")
				.add(Projections.property("ideSistema"),"ideSistema")
				.add(Projections.property("ideCerh"),"ideCerh")
				.add(Projections.property("ideCerhSituacaoRegularizacao"),"ideCerhSituacaoRegularizacao")
				.add(Projections.property("ideCerhTipoAtoDispensa"),"ideCerhTipoAtoDispensa")
				.add(Projections.property("ideCerhTipoAutorizacaoOutorgado"),"ideCerhTipoAutorizacaoOutorgado")
			).setResultTransformer(new AliasToNestedBeanResultTransformer(CerhProcesso.class))
			
			.add(Restrictions.eq("cerh.ideCerh", ideCerh.getIdeCerh()));

			return cerhProcesoDAO.listarPorCriteria(criteria, Order.asc("numProcesso"));
		}catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	private Collection<Integer> listarIdeCerhProcesso(Cerh cerh)  {
		
		Collection<Integer> listaIdeObjetosEmBanco = null;
		
		if(!Util.isNullOuVazio(cerh.getCerhProcessoCollection())) {
			listaIdeObjetosEmBanco = new ArrayList<Integer>();
			for (CerhProcesso cp : cerh.getCerhProcessoCollection()) {
				if(!Util.isNull(cp.getIdeCerhProcesso())) {
					listaIdeObjetosEmBanco.add(cp.getIdeCerhProcesso());
				}
			}
		}
		
		DetachedCriteria criteria = DetachedCriteria.forClass(CerhProcesso.class);
		criteria
			.add(Restrictions.eq("ideCerh", cerh))
		;
		
		if(!Util.isNullOuVazio(listaIdeObjetosEmBanco)) {
			criteria.add(Restrictions.not(Restrictions.in("ideCerhProcesso", listaIdeObjetosEmBanco)));
		}
		
		criteria
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideCerhProcesso"),"ideCerhProcesso")
			)
		;
		
		return ideDAO.listarPorCriteria(criteria);
	}

	
}
