package br.gov.ba.seia.dao;

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
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;

import br.gov.ba.seia.entity.LoteBoleto;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class LoteBoletoDAOImpl {
	
	@Inject
	private IDAO<LoteBoleto> loteBoletoDAO;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<LoteBoleto> listarLoteBoleto(Map<String, Object> params)  {
		

		Integer first = (Integer) params.get("first");
		Integer pageSize = (Integer) params.get("pageSize");

		return loteBoletoDAO.listarPorCriteriaDemanda(getCriteriaConsultaRequerimento(params), first, pageSize);
	}
	
	public Integer countListarLoteBoleto(Map<String, Object> params)  {
		return loteBoletoDAO.listarPorCriteria(getCriteriaConsultaRequerimento(params)).size();
	}
	
	private DetachedCriteria getCriteriaConsultaRequerimento(Map<String, Object> params) {

		Boolean isCount = getIsCount(params);
		Boolean isPagination = getIsPagination(params);

		DetachedCriteria criteria = DetachedCriteria.forClass(LoteBoleto.class, "lb");

		criteria
		.createAlias("lb.ideTipoLoteBoleto","tipo", JoinType.INNER_JOIN)
		.createAlias("lb.loteRemessaBoleto", "remessa", JoinType.LEFT_OUTER_JOIN)
		.createAlias("lb.loteRetornoBoleto", "retorno", JoinType.LEFT_OUTER_JOIN)
		.createAlias("remessa.boletoPagamentoRequerimentoCollection", "boletorem", JoinType.LEFT_OUTER_JOIN)
		.createAlias("retorno.boletoPagamentoRequerimentoCollection", "boletoret", JoinType.LEFT_OUTER_JOIN)
		.createAlias("boletorem.ideRequerimento", "reqrem", JoinType.LEFT_OUTER_JOIN)
		.createAlias("boletoret.ideRequerimento", "reqret", JoinType.LEFT_OUTER_JOIN)
		;
		String numLote = (String) params.get("numLote");
		if (!Util.isNullOuVazio(numLote)) {
			criteria.add(Restrictions.ilike("lb.numLoteBoleto", numLote.trim(), MatchMode.ANYWHERE));
		}
		String requerimento = (String) params.get("numRequerimento");
		if (!Util.isNullOuVazio(requerimento)) {
			criteria.add(Restrictions.or(
					Restrictions.ilike("reqrem.numRequerimento", requerimento.trim(), MatchMode.ANYWHERE),
					Restrictions.ilike("reqret.numRequerimento", requerimento.trim(), MatchMode.ANYWHERE)
					)
				);
		}

		if (!isCount && isPagination) {
			StringBuilder quantidadeBoletos = new StringBuilder(); /**sql para contagem de boletos do lote.*/
			quantidadeBoletos
			.append(" (case when tipo1_.ide_tipo_lote_boleto = 1 then ") /**se o lote for do tipo remessa (1), contar os boletos de lote_remessa_boleto;*/
			.append("(select count(ide_boleto_pagamento_requerimento) from boleto_pagamento_requerimento b ") 
			.append(" inner join lote_remessa_boleto r on r.ide_lote_remessa_boleto = b.ide_lote_remessa_boleto")
			.append(" where r.ide_lote_boleto = this_.ide_lote_boleto)")
			
			.append(" when tipo1_.ide_tipo_lote_boleto = 2 then ") /**se o lote for do tipo retorno (2), contar os boletos de lote_retorno_boleto;*/
			.append(" (select count(ide_boleto_pagamento_requerimento) from boleto_pagamento_requerimento b ")
			.append(" inner join lote_retorno_boleto r on r.ide_lote_retorno_boleto = b.ide_lote_retorno_boleto ")
			.append(" where r.ide_lote_boleto = this_.ide_lote_boleto) ")
			
			.append(" else 0 end) as quantidadeBoletos ")
			;
			
			criteria.setProjection(Projections.projectionList()
					
					.add(Projections.groupProperty("lb.ideLoteBoleto"),"ideLoteBoleto")
					.add(Projections.groupProperty("lb.dtcCriacao"),"dtcCriacao")
					.add(Projections.groupProperty("lb.numLoteBoleto"),"numLoteBoleto")
					
					.add(Projections.groupProperty("tipo.ideTipoLoteBoleto"),"ideTipoLoteBoleto.ideTipoLoteBoleto")
					.add(Projections.groupProperty("tipo.dscTipoLoteBoleto"),"ideTipoLoteBoleto.dscTipoLoteBoleto")
					
					.add(Projections.groupProperty("remessa.ideLoteRemessaBoleto"),"loteRemessaBoleto.ideLoteRemessaBoleto")
					.add(Projections.groupProperty("remessa.dtcEnvioRemessa"),"loteRemessaBoleto.dtcEnvioRemessa")
					.add(Projections.groupProperty("retorno.ideLoteRetornoBoleto"),"loteRetornoBoleto.ideLoteRetornoBoleto")
					
					.add(Projections.sqlProjection(quantidadeBoletos.toString(), new String[] { "quantidadeBoletos" }, new Type[] { StandardBasicTypes.INTEGER }))
					
			);
			criteria.addOrder(Order.desc("lb.numLoteBoleto"));
		} else{
			criteria.setProjection(Projections.distinct(Projections.property("lb.ideLoteBoleto")));
		}
		criteria.setResultTransformer(new AliasToNestedBeanResultTransformer(LoteBoleto.class));
		return criteria;
	}
	
	private boolean getIsPagination(Map<String, Object> params) {
		Boolean b = (Boolean) params.get("isPagination");
		return b == null ? false : b;
	}

	private boolean getIsCount(Map<String, Object> params) {
		Boolean b = (Boolean) params.get("isCount");
		return b == null ? false : b;
	}
	
	public LoteBoleto carregarUltimoLoteBoleto() {
		return loteBoletoDAO.obterPorNamedQuery("LoteBoleto.findLast", new HashMap<String, Object>());
	}
}
