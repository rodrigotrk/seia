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

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.abstracts.AbstractDAO;
import br.gov.ba.seia.entity.Cerh;
import br.gov.ba.seia.entity.CerhProcesso;
import br.gov.ba.seia.entity.CerhStatus;
import br.gov.ba.seia.entity.CerhTipoUso;
import br.gov.ba.seia.entity.TipoUsoRecursoHidrico;
import br.gov.ba.seia.enumerator.CerhStatusEnum;
import br.gov.ba.seia.enumerator.TipoUsoRecursoHidricoEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhTipoUsoDAOImpl extends AbstractDAO<CerhTipoUso>{
	private static final long serialVersionUID = 1L;

	@Inject
	private IDAO<CerhTipoUso> cerhTipoUsoDAO;
	
	@Inject
	private IDAO<Integer> ideDAO;
	
	@Override
	protected IDAO<CerhTipoUso> getDAO() {
		return cerhTipoUsoDAO;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(CerhTipoUso cerhTipoUso) {
		cerhTipoUsoDAO.salvarOuAtualizar(cerhTipoUso);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remover(CerhTipoUso cerhTipoUso) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideCerhProcesso", cerhTipoUso.getIdeCerhProcesso());
		params.put("ideTipoUso", cerhTipoUso.getIdeTipoUsoRecursoHidrico());
		cerhTipoUsoDAO.executarNativeQuery("DELETE FROM cerh_tipo_uso  WHERE ide_cerh_processo = :ideCerhProcesso and ide_tipo_uso_recurso_hidrico = :ideTipoUso", params);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerSemProcesso(CerhTipoUso cerhTipoUso){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideCerh", cerhTipoUso.getIdeCerh());
		params.put("ideTipoUso", cerhTipoUso.getIdeTipoUsoRecursoHidrico());
		cerhTipoUsoDAO.executarNativeQuery("DELETE FROM cerh_tipo_uso  WHERE ide_cerh = :ideCerh and ide_tipo_uso_recurso_hidrico = :ideTipoUso and ide_cerh_processo is null", params);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarEmLote(Collection<CerhTipoUso> cerhTipoUsoColl) {
		cerhTipoUsoDAO.salvarEmLote((List<CerhTipoUso>) cerhTipoUsoColl);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<CerhTipoUso> listar(CerhProcesso cerhProcesso)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(CerhTipoUso.class)
			.add(Restrictions.eq("ideCerhProcesso.ideCerhProcesso", cerhProcesso.getIdeCerhProcesso()))
			.setProjection(
					Projections.projectionList()
						.add(Projections.property("ideObjetoPai"), "ideObjetoPai")
						.add(Projections.property("ideCerhTipoUso"), "ideCerhTipoUso")
						.add(Projections.property("ideCerh.ideCerh"), "ideCerh.ideCerh")
						.add(Projections.property("ideCerhProcesso.ideCerhProcesso"), "ideCerhProcesso.ideCerhProcesso")
						.add(Projections.property("ideCerhRespostaDadosGerais.ideCerhRespostaDadosGerais"), "ideCerhRespostaDadosGerais.ideCerhRespostaDadosGerais")
						.add(Projections.property("ideTipoUsoRecursoHidrico.ideTipoUsoRecursoHidrico"), "ideTipoUsoRecursoHidrico.ideTipoUsoRecursoHidrico")
				).setResultTransformer(new AliasToNestedBeanResultTransformer(CerhTipoUso.class));
		return cerhTipoUsoDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<CerhTipoUso> listarPorProcesso(CerhProcesso cerhProcesso) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CerhTipoUso.class)
				.createAlias("ideCerhProcesso", "cerhProcesso", JoinType.INNER_JOIN)
			.add(Restrictions.eq("cerhProcesso.numProcesso", cerhProcesso.getNumProcesso()))
			.add(Restrictions.eq("ideCerh.ideCerh", cerhProcesso.getIdeCerh().getIdeCerh()))
			.setProjection(
					Projections.projectionList()
						.add(Projections.property("ideObjetoPai"), "ideObjetoPai")
						.add(Projections.property("ideCerhTipoUso"), "ideCerhTipoUso")
						.add(Projections.property("ideCerh.ideCerh"), "ideCerh.ideCerh")
						.add(Projections.property("cerhProcesso.ideCerhProcesso"), "ideCerhProcesso.ideCerhProcesso")
						.add(Projections.property("cerhProcesso.numProcesso"), "ideCerhProcesso.numProcesso")
						.add(Projections.property("ideCerhRespostaDadosGerais.ideCerhRespostaDadosGerais"), "ideCerhRespostaDadosGerais.ideCerhRespostaDadosGerais")
						.add(Projections.property("ideTipoUsoRecursoHidrico.ideTipoUsoRecursoHidrico"), "ideTipoUsoRecursoHidrico.ideTipoUsoRecursoHidrico")
				).setResultTransformer(new AliasToNestedBeanResultTransformer(CerhTipoUso.class));
		return cerhTipoUsoDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CerhTipoUso buscarPorPorPorcesso(CerhProcesso ideCerhProcesso, TipoUsoRecursoHidricoEnum tipoUsoRecursoHidricoEnum) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CerhTipoUso.class)
			.add(Restrictions.eq("ideCerhProcesso.ideCerhProcesso", ideCerhProcesso.getIdeCerhProcesso()))
			.add(Restrictions.eq("ideTipoUsoRecursoHidrico.ideTipoUsoRecursoHidrico", tipoUsoRecursoHidricoEnum.getId()))
			.setProjection(
				Projections.projectionList()
					.add(Projections.property("ideObjetoPai"), "ideObjetoPai")
					.add(Projections.property("ideCerhTipoUso"), "ideCerhTipoUso")
					.add(Projections.property("ideCerh.ideCerh"), "ideCerh.ideCerh")
					.add(Projections.property("ideCerhProcesso.ideCerhProcesso"), "ideCerhProcesso.ideCerhProcesso")
					.add(Projections.property("ideCerhRespostaDadosGerais.ideCerhRespostaDadosGerais"), "ideCerhRespostaDadosGerais.ideCerhRespostaDadosGerais")
					.add(Projections.property("ideTipoUsoRecursoHidrico.ideTipoUsoRecursoHidrico"), "ideTipoUsoRecursoHidrico.ideTipoUsoRecursoHidrico")
			).setResultTransformer(new AliasToNestedBeanResultTransformer(CerhTipoUso.class));
		return cerhTipoUsoDAO.buscarPorCriteria(criteria);
		
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void gerenciarCerhTipoUsoRemovido(Cerh cerh)  {
		Collection<Integer> listaRemocao = listarIdeCerhTipoUsoRemocao(cerh);
		if(!Util.isNullOuVazio(listaRemocao)) {
			StringBuilder hql = new StringBuilder();
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("listaRemocao", listaRemocao);
			hql
				.append("delete from CerhTipoUso ctu where ctu.ideCerhTipoUso in (:listaRemocao)")
			;
			cerhTipoUsoDAO.executarQuery(hql.toString(), params);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Integer> listarIdeCerhTipoUsoRemocao(Cerh cerh)  {
		
		Collection<Integer> listaIdeObjetosEmBanco = null;
		
		if(!Util.isNullOuVazio(cerh.getCerhTipoUsoCollection())) {
			listaIdeObjetosEmBanco = new ArrayList<Integer>();
			for (CerhTipoUso ctu : cerh.getCerhTipoUsoCollection()) {
				if(!Util.isNull(ctu.getIdeCerhTipoUso())) {
					listaIdeObjetosEmBanco.add(ctu.getIdeCerhTipoUso());
				}
			}
		}
		
		DetachedCriteria criteria = DetachedCriteria.forClass(CerhTipoUso.class);
		criteria
			.add(Restrictions.eq("ideCerh", cerh))
			.add(Restrictions.isNull("ideCerhProcesso"))
		;
		
		if(!Util.isNullOuVazio(listaIdeObjetosEmBanco)) {
			criteria.add(Restrictions.not(Restrictions.in("ideCerhTipoUso", listaIdeObjetosEmBanco)));
		}
		
		criteria
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideCerhTipoUso"),"ideCerhTipoUso")
			)
		;
		
		return ideDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<CerhTipoUso> listarCerhTipoUsoRemocao(Cerh cerh) {
		Collection<Integer> listarIdeCerhTipoUsoRemocao = listarIdeCerhTipoUsoRemocao(cerh);
		if(!Util.isNullOuVazio(listarIdeCerhTipoUsoRemocao)){
			DetachedCriteria criteria = DetachedCriteria.forClass(CerhTipoUso.class)
					.createAlias("ideTipoUsoRecursoHidrico", "turh")
					.add(Restrictions.in("ideCerhTipoUso", listarIdeCerhTipoUsoRemocao))
					.setProjection(
							Projections.projectionList()
							.add(Projections.property("ideCerhTipoUso"),"ideCerhTipoUso")
							.add(Projections.property("turh.ideTipoUsoRecursoHidrico"), "ideTipoUsoRecursoHidrico.ideTipoUsoRecursoHidrico")
							.add(Projections.property("turh.dscTipoUsoRecursoHidrico"), "ideTipoUsoRecursoHidrico.dscTipoUsoRecursoHidrico")
							).setResultTransformer(new AliasToNestedBeanResultTransformer(CerhTipoUso.class))
							;
			return cerhTipoUsoDAO.listarPorCriteria(criteria);
		}
		return null;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerPor(Cerh cerh)  {
		
		Collection<Integer> listaIdeCerhTipoUso = new ArrayList<Integer>(); 
		
		Collection<CerhTipoUso> listaCerhTipoUsoParaRemover = listarCerhTipoUsoParaRemover(cerh);
		for(CerhTipoUso ctu : listaCerhTipoUsoParaRemover) {
			listaIdeCerhTipoUso.add(ctu.getIdeCerhTipoUso());
		}
		if(!Util.isNullOuVazio(listaIdeCerhTipoUso)) {
			
			StringBuilder hql = new StringBuilder();
			
			hql
				.append("delete from CerhTipoUso ctu1 ")
				.append("where ctu1.ideCerhTipoUso in (")
				.append("	select ctu2.ideCerhTipoUso ")
				.append("	from CerhTipoUso ctu2 ")
				.append("	where ctu2.ideCerh = :ideCerh ")
				.append("	and ctu2.ideCerhTipoUso not in :listaIdeCerhTipoUso)")
			;
			
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("ideCerh", cerh);
			params.put("listaIdeCerhTipoUso", listaIdeCerhTipoUso);
			
			cerhTipoUsoDAO.executarQuery(hql.toString(), params);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Collection<CerhTipoUso> listarCerhTipoUsoParaRemover(Cerh cerh) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CerhTipoUso.class);
		
		Collection<Integer> listaIdeCerhTipoUso = new ArrayList<Integer>(); 
		for(CerhTipoUso ctu : cerh.getCerhTipoUsoCollection()) {
			listaIdeCerhTipoUso.add(ctu.getIdeCerhTipoUso());
		}
		
		criteria
			.add(Restrictions.eq("ideCerh", cerh))
			.add(Restrictions.in("ideCerhTipoUso", listaIdeCerhTipoUso))
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideCerhTipoUso"),"ideCerhTipoUso")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(CerhTipoUso.class))
		;
		return cerhTipoUsoDAO.listarPorCriteria(criteria);
	}

	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<CerhTipoUso> listarCerhTipoUsoPor(Cerh cerh) {
		return listarCerh(cerh, true);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<CerhTipoUso> listarCerhTipoUsoPorProcesso(Cerh cerh)  {
		return listarCerh(cerh, false);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<CerhTipoUso> listarCerh(Cerh cerh, boolean isRespostarDadosGerias) {
		DetachedCriteria criteria = getCriteriaTo(cerh);
		
		if(!Util.isNullOuVazio(cerh.getCerhProcessoCollection())){
			criteria.add(Restrictions.isNull("crdg.ideCerhRespostaDadosGerais"));
		}else{
			criteria.add(Restrictions.isNotNull("crdg.ideCerhRespostaDadosGerais"));
		}

		criteria.setProjection(getProjection()).setResultTransformer(new AliasToNestedBeanResultTransformer(CerhTipoUso.class));
		
		return cerhTipoUsoDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CerhTipoUso carregarCerhTipoUso(Cerh cerh, TipoUsoRecursoHidrico tipoUsoRecursoHidrico) {
		DetachedCriteria criteria = getCriteriaTo(cerh)
				.add(Restrictions.isNotNull("crdg.ideCerhRespostaDadosGerais"))
				.add(Restrictions.eq("turh.ideTipoUsoRecursoHidrico", tipoUsoRecursoHidrico.getIdeTipoUsoRecursoHidrico()))
				.setProjection(getProjection()).setResultTransformer(new AliasToNestedBeanResultTransformer(CerhTipoUso.class))	;
		return cerhTipoUsoDAO.buscarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CerhTipoUso carregarCerhTipoUsoPorNumProcesso(String numProcesso, TipoUsoRecursoHidrico tipoUsoRecursoHidrico)  {
			DetachedCriteria criteria = DetachedCriteria.forClass(CerhTipoUso.class, "ctu")
				.createAlias("ctu.ideCerh", "cerh", JoinType.INNER_JOIN)	
				.createAlias("ctu.ideCerhProcesso", "p", JoinType.INNER_JOIN)
				.createAlias("cerh.ideCerhStatus", "cs", JoinType.INNER_JOIN)
					
				.add(Restrictions.ne("cs.ideCerhStatus", CerhStatusEnum.CANCELADO.getId()))
				.add(Restrictions.eq("p.numProcesso", numProcesso))
				.add(Restrictions.eq("ctu.ideTipoUsoRecursoHidrico.ideTipoUsoRecursoHidrico", tipoUsoRecursoHidrico.getId()))
				.setProjection(Projections.projectionList()
				.add(Projections.property("ctu.ideCerhTipoUso"),"ideCerhTipoUso")).
				setResultTransformer(new AliasToNestedBeanResultTransformer(CerhTipoUso.class));
			
			
			List<CerhTipoUso> listCerhTipoUso = cerhTipoUsoDAO.listarPorCriteria(criteria);
		
			if(!Util.isNullOuVazio(listCerhTipoUso) && (listCerhTipoUso.isEmpty() || listCerhTipoUso.size() > 1)) {
				return listCerhTipoUso.get(0);
			}
			
			return null;
		
	}

	private DetachedCriteria getCriteriaTo(Cerh cerh) {
		return DetachedCriteria.forClass(CerhTipoUso.class)
			.createAlias("ideCerh", "c", JoinType.INNER_JOIN)
			.createAlias("ideTipoUsoRecursoHidrico", "turh", JoinType.INNER_JOIN)
			.createAlias("ideCerhRespostaDadosGerais", "crdg", JoinType.LEFT_OUTER_JOIN)
			.add(Restrictions.eq("ideCerh", cerh));
		
	}
	
	private ProjectionList getProjection() {
		return 
			Projections.projectionList()
				.add(Projections.property("ideObjetoPai"),"ideObjetoPai")
				.add(Projections.property("ideCerhTipoUso"),"ideCerhTipoUso")
				.add(Projections.property("c.ideCerh"),"ideCerh.ideCerh")
				.add(Projections.property("crdg.ideCerhRespostaDadosGerais"),"ideCerhRespostaDadosGerais.ideCerhRespostaDadosGerais")
				.add(Projections.property("turh.ideTipoUsoRecursoHidrico"),"ideTipoUsoRecursoHidrico.ideTipoUsoRecursoHidrico");
	}

	
	
}