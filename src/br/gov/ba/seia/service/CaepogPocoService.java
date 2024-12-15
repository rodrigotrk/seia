package br.gov.ba.seia.service;

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
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.CaepogClasseResiduo;
import br.gov.ba.seia.entity.CaepogFasePerfuracao;
import br.gov.ba.seia.entity.CaepogFaseVariavel;
import br.gov.ba.seia.entity.CaepogFormacaoGeologica;
import br.gov.ba.seia.entity.CaepogFormacaoGeologicaPoco;
import br.gov.ba.seia.entity.CaepogLocacao;
import br.gov.ba.seia.entity.CaepogObjetivoAtividade;
import br.gov.ba.seia.entity.CaepogObjetivoAtividadePoco;
import br.gov.ba.seia.entity.CaepogPoco;
import br.gov.ba.seia.entity.CaepogTipoPoco;
import br.gov.ba.seia.entity.CaepogTipoResiduo;
import br.gov.ba.seia.entity.CaepogVariavel;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CaepogPocoService {
	
	@Inject
	IDAO<CaepogPoco> caepogPocoDAO;
	
	@Inject
	IDAO<CaepogTipoPoco> caepogTipoPocoDAO;
	
	@Inject
	IDAO<CaepogTipoResiduo> caepogTipoResiduoDAO;
	
	@Inject
	IDAO<CaepogClasseResiduo> caepogClasseResiduoDAO;
	
	@Inject
	IDAO<CaepogObjetivoAtividade> caepogObjetivoAtividadeDAO;
	
	@Inject
	IDAO<CaepogObjetivoAtividadePoco> caepogObjetivoAtividadePocoDAO;
	
	@Inject
	IDAO<CaepogFormacaoGeologicaPoco> caepogFormacaoGeologicaPocoDAO;
	
	@Inject
	IDAO<CaepogFormacaoGeologica> caepogFormacaoGeologicaDAO;
	
	@Inject
	IDAO<CaepogFasePerfuracao> caepogFasePerfuracaoDAO;
	
	@Inject
	IDAO<CaepogFaseVariavel> caepogFaseVariavelDAO;
	
	@Inject
	IDAO<CaepogVariavel> caepogVariavelDAO;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CaepogTipoPoco> listarTodosTipoPoco() {
		return caepogTipoPocoDAO.listarTodos();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CaepogObjetivoAtividade> listarTodosObjetivoAtividade() {
		DetachedCriteria criteria = DetachedCriteria.forClass(CaepogObjetivoAtividade.class);
		return caepogObjetivoAtividadeDAO.listarPorCriteria(criteria, Order.asc("ideCaepogObjetivoAtividade"));
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CaepogFormacaoGeologica> listarTodosFormacaoGeologica()  {
		DetachedCriteria criteria = DetachedCriteria.forClass(CaepogFormacaoGeologica.class);
		return caepogFormacaoGeologicaDAO.listarPorCriteria(criteria, Order.asc("ideCaepogFormacaoGeologica"));
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CaepogTipoResiduo> listarTodosTipoResiduo() {
		return caepogTipoResiduoDAO.listarTodos();
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(CaepogPoco cp)  {
		caepogPocoDAO.salvarOuAtualizar(cp);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarObjetivoAtividadePoco(CaepogObjetivoAtividadePoco oap)  {
		caepogObjetivoAtividadePocoDAO.salvarOuAtualizar(oap);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFormacaoGeologicaPoco(CaepogFormacaoGeologicaPoco cpg)  {
		caepogFormacaoGeologicaPocoDAO.salvarOuAtualizar(cpg);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CaepogPoco> listarPorCaepogLocacao(CaepogLocacao cl)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(CaepogPoco.class, "poco")
				.createAlias("poco.ideLocalizacaoGeografica", "locGeo", JoinType.LEFT_OUTER_JOIN)
				.createAlias("poco.ideCaepogTipoPoco", "tp", JoinType.LEFT_OUTER_JOIN)
				.createAlias("poco.ideCaepogLocacao", "cl", JoinType.INNER_JOIN);
		
		if(!Util.isNullOuVazio(cl)) {
			criteria.add(Restrictions.eq("cl.ideCaepogLocacao", cl.getIdeCaepogLocacao()));
		}
		
		List<CaepogPoco> listRetorno = caepogPocoDAO.listarPorCriteria(criteria, Order.asc("poco.ideCaepogPoco"));
		
		for (CaepogPoco cp : listRetorno) {
			if(!Util.isNullOuVazio(cp.getIdeLocalizacaoGeografica())) {
				Map<String,Double> map = retornaLatitudeLongitude(cp.getIdeLocalizacaoGeografica());
				cp.setLatitudeCaepogPoco(map.get("Latitude"));
				cp.setLongitudeCaepogPoco(map.get("Longitude"));
			}
		}
		
		return listRetorno;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CaepogObjetivoAtividade> listarObjetivosPorPoco(CaepogPoco cp)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(CaepogObjetivoAtividadePoco.class, "oap")
				.createAlias("oap.ideCaepogObjetivoAtividade", "oa", JoinType.INNER_JOIN)
				.createAlias("oap.ideCaepogPoco", "poco", JoinType.INNER_JOIN);
		
		ProjectionList projecao = Projections.projectionList()
				.add(Projections.property("oa.ideCaepogObjetivoAtividade"), "ideCaepogObjetivoAtividade")
				.add(Projections.property("oa.nomCaepogObjetivoAtividade"), "nomCaepogObjetivoAtividade");
		
		criteria.setProjection(projecao)
				.setProjection(Projections.distinct(projecao))
				.setResultTransformer(new AliasToNestedBeanResultTransformer(CaepogObjetivoAtividade.class));
		
		if(!Util.isNullOuVazio(cp)) {
			criteria.add(Restrictions.eq("oap.ideCaepogPoco.ideCaepogPoco", cp.getIdeCaepogPoco()));
		}
		
		return caepogObjetivoAtividadeDAO.listarPorCriteria(criteria, Order.asc("ideCaepogObjetivoAtividade"));
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CaepogFormacaoGeologica> listarFormacaoGeologicaPorPoco(CaepogPoco cp)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(CaepogFormacaoGeologicaPoco.class, "pg")
				.createAlias("pg.ideCaepogFormacaoGeologica", "fg", JoinType.INNER_JOIN)
				.createAlias("pg.ideCaepogPoco", "poco", JoinType.INNER_JOIN);
				
		ProjectionList projecao = Projections.projectionList()
				.add(Projections.property("fg.ideCaepogFormacaoGeologica"), "ideCaepogFormacaoGeologica")
				.add(Projections.property("fg.nomCaepogFormacaoGeologica"), "nomCaepogFormacaoGeologica");
				
		criteria.setProjection(projecao)
				.setProjection(Projections.distinct(projecao))
				.setResultTransformer(new AliasToNestedBeanResultTransformer(CaepogFormacaoGeologica.class));		
				
		if(!Util.isNullOuVazio(cp)) {
			criteria.add(Restrictions.eq("pg.ideCaepogPoco.ideCaepogPoco", cp.getIdeCaepogPoco()));
		}
		
		return caepogFormacaoGeologicaDAO.listarPorCriteria(criteria, Order.asc("ideCaepogFormacaoGeologica"));		
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CaepogClasseResiduo> listarTodosClasseResiduo() {
		return caepogClasseResiduoDAO.listarTodos();
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFasePerfuracao(CaepogFasePerfuracao cfp)  {
		caepogFasePerfuracaoDAO.salvarOuAtualizar(cfp);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFaseVariavel(CaepogFaseVariavel cfv)  {
		caepogFaseVariavelDAO.salvarOuAtualizar(cfv);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CaepogFaseVariavel carregarFaseVariavelPorPerfuracao(CaepogFasePerfuracao cfp, CaepogVariavel cv) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(CaepogFaseVariavel.class, "cfv")
				.createAlias("cfv.ideCaepogFasePerfuracao", "cfp", JoinType.INNER_JOIN)
				.createAlias("cfv.ideCaepogVariavel", "cv", JoinType.INNER_JOIN);
		
		if(!Util.isNullOuVazio(cfp) && !Util.isNullOuVazio(cv)) {
			criteria.add(Restrictions.eq("cfp.ideCaepogFasePerfuracao", cfp.getIdeCaepogFasePerfuracao()));
			criteria.add(Restrictions.eq("cv.ideCaepogVariavel", cv.getIdeCaepogVariavel()));
			
			return caepogFaseVariavelDAO.buscarPorCriteria(criteria);
		} else {
			return null;
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CaepogFasePerfuracao> listarPorCaepogPoco(CaepogPoco cp) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CaepogFasePerfuracao.class, "cfp")
				.createAlias("cfp.ideCaepogPoco", "cp", JoinType.LEFT_OUTER_JOIN)
				.createAlias("cfp.ideCaepogTipoResiduo", "tr", JoinType.LEFT_OUTER_JOIN)
				.createAlias("cfp.ideCaepogClasseResiduo", "cr", JoinType.LEFT_OUTER_JOIN)
				.createAlias("cfp.idePessoaJuridicaDestino", "pj", JoinType.LEFT_OUTER_JOIN);
		
		if(!Util.isNullOuVazio(cp)) {
			criteria.add(Restrictions.eq("cp.ideCaepogPoco", cp.getIdeCaepogPoco()));
		}
		
		return caepogFasePerfuracaoDAO.listarPorCriteria(criteria, Order.asc("cfp.ideCaepogFasePerfuracao"));
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(CaepogPoco cp)  {
		caepogPocoDAO.remover(cp);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFasePerfuracaoPorPoco(CaepogPoco cp)  {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ideCaepogPoco", cp.getIdeCaepogPoco());
		
		StringBuilder sql = new StringBuilder();
		sql.append("delete from CaepogFasePerfuracao cfp where cfp.ideCaepogPoco.ideCaepogPoco = :ideCaepogPoco");
		caepogPocoDAO.executarQuery(sql.toString(), params);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirCaepogFormacaoGeologicaPoco(CaepogPoco cp) {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ideCaepogPoco", cp.getIdeCaepogPoco());
		
		StringBuilder sql = new StringBuilder();
		sql.append("delete from CaepogFormacaoGeologicaPoco cfgp where cfgp.ideCaepogPoco.ideCaepogPoco = :ideCaepogPoco");
		caepogPocoDAO.executarQuery(sql.toString(), params);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirObjetivoAtividadePorPoco(CaepogPoco cp) {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ideCaepogPoco", cp.getIdeCaepogPoco());
		
		StringBuilder sql = new StringBuilder();
		sql.append("delete from CaepogObjetivoAtividadePoco coap where coap.ideCaepogPoco.ideCaepogPoco = :ideCaepogPoco");
		caepogPocoDAO.executarQuery(sql.toString(), params);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFaseVariavelPorFasePerfuracao(CaepogFasePerfuracao cfp) {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ideCaepogFasePerfuracao", cfp.getIdeCaepogFasePerfuracao());
		
		StringBuilder sql = new StringBuilder();
		sql.append("delete from CaepogFaseVariavel cfv where cfv.ideCaepogFasePerfuracao.ideCaepogFasePerfuracao = :ideCaepogFasePerfuracao");
		caepogFasePerfuracaoDAO.executarQuery(sql.toString(), params);
	}
	
	public void excluirFasePerfuracao(CaepogFasePerfuracao cfp) {
		caepogFasePerfuracaoDAO.remover(cfp);
	}
	
	//#7655 - Melhoria - Exibição das coordenadas geográficas do poço
	public Map<String,Double> retornaLatitudeLongitude(LocalizacaoGeografica locGeo)  {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ideLocalizacaoGeografica", locGeo.getIdeLocalizacaoGeografica());
		
		Object stringRetorno = 
				caepogPocoDAO.obterPorNativeQuery("select ST_AsText(the_geom) from dado_geografico where ide_localizacao_geografica = :ideLocalizacaoGeografica", params);
		
		Map<String,Double> mapRetorno = new HashMap<String, Double>();
		
		if(stringRetorno != null) {
			int indexLatitudeInicio = ((String) stringRetorno).indexOf(" -") + 1;
			int indexLatitudeFim = ((String) stringRetorno).indexOf(")");
			String latitude = ((String) stringRetorno).substring(indexLatitudeInicio, indexLatitudeFim);
			latitude = latitude.substring(0, 10);
			
			int indexLongitudeInicio = ((String) stringRetorno).indexOf("(") + 2;
			int indexLongitudeFim = ((String) stringRetorno).indexOf(" ");
			String longitude = ((String) stringRetorno).substring(indexLongitudeInicio, indexLongitudeFim);
			longitude = longitude.substring(0, 10);
			
			mapRetorno.put("Latitude", new Double(latitude));
			mapRetorno.put("Longitude", new Double(longitude));
		} else {
			mapRetorno.put("Latitude", Double.valueOf(0.0));
			mapRetorno.put("Longitude", Double.valueOf(0.0));
		}
		
		return mapRetorno;
	}
}