/**
 * 		07/01/14
 * @author eduardo.fernandes
 */
package br.gov.ba.seia.service;

import java.util.ArrayList;
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

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.FceSesCoordenadasLancamentoLocalizacaoGeografica;
import br.gov.ba.seia.entity.LancamentoCaracterizacaoEfluente;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Log4jUtil;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class LancamentoCaracterizacaoEfluenteService {

	@Inject
	private IDAO<LancamentoCaracterizacaoEfluente> lancamentoCaracterizacaoIDAO;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarCaracterizacaoEfluente(LancamentoCaracterizacaoEfluente lancamentoCaracterizacao) {
		lancamentoCaracterizacaoIDAO.salvarOuAtualizar(lancamentoCaracterizacao);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void deletarCaracterizacaoEfluente(FceSesCoordenadasLancamentoLocalizacaoGeografica fceLancamentoEfluente) {
		
		String sql = "delete from LancamentoCaracterizacaoEfluente lce where lce.ideCoordenadaFceLancamentoLocalizacaoGeografica.ideFceSesLancamentoLocalizacaoGeografica = :ideCoordenadaFceLancamentoLocalizacaoGeografica";
		
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ideCoordenadaFceLancamentoLocalizacaoGeografica", fceLancamentoEfluente.getIdeFceSesLancamentoLocalizacaoGeografica());
		
		lancamentoCaracterizacaoIDAO.executarQuery(sql, params);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<LancamentoCaracterizacaoEfluente> listarCaracterizacaoEfluente(FceSesCoordenadasLancamentoLocalizacaoGeografica fceLancamentoEfluente){
		
		List<LancamentoCaracterizacaoEfluente> lista = new ArrayList<LancamentoCaracterizacaoEfluente>();
		
		try {
		DetachedCriteria criteria = DetachedCriteria.forClass(LancamentoCaracterizacaoEfluente.class)
				.createAlias("ideCaracterizacaoEfluente", "caracterizacao", JoinType.INNER_JOIN)
				.createAlias("ideCoordenadaFceLancamentoLocalizacaoGeografica", "fceEfluente", JoinType.INNER_JOIN);
				
		
				criteria.add(Restrictions.eq("fceEfluente.ideFceSesLancamentoLocalizacaoGeografica", fceLancamentoEfluente.getIdeFceSesLancamentoLocalizacaoGeografica()))
				.setProjection(
						Projections.projectionList()
						.add(Projections.property("ideLancamentoCaracterizacaoEfluente"), "ideLancamentoCaracterizacaoEfluente")
						.add(Projections.property("fceEfluente.ideFceSesLancamentoLocalizacaoGeografica"), "ideCoordenadaFceLancamentoLocalizacaoGeografica.ideFceSesLancamentoLocalizacaoGeografica")
						.add(Projections.property("caracterizacao.ideCaracterizacaoEfluente"), "ideCaracterizacaoEfluente.ideCaracterizacaoEfluente")
						.add(Projections.property("caracterizacao.dscCaracterizacaoEfluente"), "ideCaracterizacaoEfluente.dscCaracterizacaoEfluente"))
						.setResultTransformer(new AliasToNestedBeanResultTransformer(LancamentoCaracterizacaoEfluente.class));
				
		
				lista=  lancamentoCaracterizacaoIDAO.listarPorCriteria(criteria, Order.asc("ideLancamentoCaracterizacaoEfluente"));
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		
		return lista;
	}
	
}