package br.gov.ba.seia.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.DadoGeografico;
import br.gov.ba.seia.entity.FceCanal;
import br.gov.ba.seia.entity.FceCanalTrecho;
import br.gov.ba.seia.entity.FceCanalTrechoSecaoGeometrica;
import br.gov.ba.seia.entity.TipoRevestimento;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceCanalTrechoService {

	@Inject
	private IDAO<FceCanalTrecho> idao;
	
	@Inject
	private IDAO<DadoGeografico> dadoGeoIdao;
	
	@Inject
	private IDAO<TipoRevestimento> tiporevestimentoIdao;
	
	@Inject
	private IDAO<FceCanalTrechoSecaoGeometrica> fceCanalTrechoSecaoGeometricaIdao;
	
	@EJB
	private LocalizacaoGeograficaService localizacaoGeograficaService;
	@EJB
	private FceCanalTrechoSecaoGeometricaService fceCanalTrechoSecaoGeometricaService;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceCanalTrecho> listarPorFceCanal(FceCanal fceCanal) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceCanalTrecho.class)
				.createAlias("localizacaoGeograficaInicio", "localizacaoGeograficaInicio", JoinType.LEFT_OUTER_JOIN)
				.createAlias("localizacaoGeograficaFim","localizacaoGeograficaFim", JoinType.LEFT_OUTER_JOIN)
				.createAlias("fceCanalTrechoSecaoGeometrica", "fceCanalTrechoSecaoGeometrica", JoinType.LEFT_OUTER_JOIN)
				.createAlias("tipoCanal", "tipoCanal", JoinType.LEFT_OUTER_JOIN)
				.add(Restrictions.eq("canal", fceCanal));
		
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<FceCanalTrecho> trechos = idao.listarPorCriteria(criteria, Order.asc("idFceCanalTrecho"));
		
		if(!Util.isNullOuVazio(trechos)){
			for(FceCanalTrecho trecho : trechos){
				Map<String, Object> parametros = new HashMap<String, Object>();
				//Localização inicio
				parametros.put("ideLocalizacaoGeografica", trecho.getLocalizacaoGeograficaInicio().getIdeLocalizacaoGeografica());
				trecho.getLocalizacaoGeograficaInicio().setDadoGeograficoCollection(dadoGeoIdao.buscarPorNamedQuery("DadoGeografico.findByIdeLocalizacaoGeografica", parametros));
				//Localização  Fim
				parametros.put("ideLocalizacaoGeografica", trecho.getLocalizacaoGeograficaFim().getIdeLocalizacaoGeografica());
				trecho.getLocalizacaoGeograficaFim().setDadoGeograficoCollection(dadoGeoIdao.buscarPorNamedQuery("DadoGeografico.findByIdeLocalizacaoGeografica", parametros));
				//Tipos revestimentos
				parametros = new HashMap<String, Object>();
				parametros.put("fceCanalTrecho", trecho);
				trecho.setTiposRevestimentos(tiporevestimentoIdao.buscarPorNamedQuery("TipoRevestimento.findByFceCanalTrecho", parametros));
				//secao geometrica
				trecho.setFceCanalTrechoSecaoGeometrica(getFceCanalTrechoSecaoGeometrica(trecho));
			}
		}
		return trechos;	
	}
	
	public List<FceCanalTrechoSecaoGeometrica> getFceCanalTrechoSecaoGeometrica(FceCanalTrecho trecho) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceCanalTrechoSecaoGeometrica.class)
				.createAlias("fceCanalTrecho", "fceCanalTrecho", JoinType.LEFT_OUTER_JOIN)
				.createAlias("secaoGeometrica","secaoGeometrica", JoinType.LEFT_OUTER_JOIN)
				.add(Restrictions.eq("fceCanalTrecho", trecho));
		return fceCanalTrechoSecaoGeometricaIdao.listarPorCriteria(criteria, Order.asc("ideFceCanalTrechoSecaoGeometrica"));
	}
	
	public List<FceCanalTrechoSecaoGeometrica> getFceCanalTrechoSecaoGeometricaProjetado(FceCanalTrecho trecho) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceCanalTrechoSecaoGeometrica.class)
				.createAlias("fceCanalTrecho", "fceCanalTrecho", JoinType.LEFT_OUTER_JOIN)
				.createAlias("secaoGeometrica","secaoGeometrica", JoinType.LEFT_OUTER_JOIN)
				.add(Restrictions.eq("fceCanalTrecho", trecho));
		
		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("ideFceCanalTrechoSecaoGeometrica"), "ideFceCanalTrechoSecaoGeometrica")
				.add(Projections.property("base_maior"), "base_maior")
				.add(Projections.property("base_menor"), "base_menor")
				.add(Projections.property("altura"), "altura")
				.add(Projections.property("largura"), "largura")
				.add(Projections.property("diametro"), "diametro")
				
				.add(Projections.property("secaoGeometrica.ideSecaoGeometrica"), "secaoGeometrica.ideSecaoGeometrica")
				.add(Projections.property("secaoGeometrica.dscSecaoGeometrica"), "secaoGeometrica.dscSecaoGeometrica")
			)
			
			.setResultTransformer(new AliasToNestedBeanResultTransformer(FceCanalTrechoSecaoGeometrica.class)); 
		
		return fceCanalTrechoSecaoGeometricaIdao.listarPorCriteria(criteria, Order.asc("ideFceCanalTrechoSecaoGeometrica"));
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void deleteFceCanalTrecho(FceCanalTrecho trecho) {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("idFceCanalTrecho", trecho.getIdFceCanalTrecho());
		idao.executarNamedQuery("FceCanalTrecho.removeByIdFceCanalTrecho", params);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceCanalTrecho(FceCanalTrecho trecho) {
		idao.salvar(trecho);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizarFceCanalTrecho(FceCanalTrecho trecho) {
		idao.atualizar(trecho);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceCanalTrecho(List<FceCanalTrecho> trechos) {
		idao.salvarEmLote(trechos);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerFceCanalTrecho(FceCanalTrecho trecho) {
		localizacaoGeograficaService.excluirByIdLocalizacaoGeografica(trecho.getLocalizacaoGeograficaInicio());
		localizacaoGeograficaService.excluirByIdLocalizacaoGeografica(trecho.getLocalizacaoGeograficaFim());
		
		for(FceCanalTrechoSecaoGeometrica secao : trecho.getFceCanalTrechoSecaoGeometrica()){
			fceCanalTrechoSecaoGeometricaService.removerFceCanalTrechoSecaoGeometrica(secao);
		}
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideFceCanalTrecho", trecho.getIdFceCanalTrecho());
		String delSQL = "DELETE FROM fce_canal_trecho_tipo_revestimento WHERE ide_fce_canal_trecho = :ideFceCanalTrecho";
		idao.executarNativeQuery(delSQL, params);
		
		deleteFceCanalTrecho(trecho);
	}
}
