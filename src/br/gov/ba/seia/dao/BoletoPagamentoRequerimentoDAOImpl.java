package br.gov.ba.seia.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.BoletoPagamentoHistorico;
import br.gov.ba.seia.entity.BoletoPagamentoRequerimento;
import br.gov.ba.seia.entity.LoteBoleto;
import br.gov.ba.seia.enumerator.StatusBoletoEnum;
import br.gov.ba.seia.enumerator.TipoBoletoPagamentoEnum;
import br.gov.ba.seia.enumerator.TipoLoteBoletoEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class BoletoPagamentoRequerimentoDAOImpl {
	
	
	@Inject
	private IDAO<BoletoPagamentoRequerimento> boletoPagamentoRequerimentoDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<BoletoPagamentoRequerimento> listarBoletosPorLote(LoteBoleto lote) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(BoletoPagamentoRequerimento.class, "bpr");
		criteria.createAlias("bpr.ideRequerimento","requerimento", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("bpr.ideProcesso","processo", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("bpr.ideTipoBoletoPagamento","tipo", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("bpr.ideProcessoReenquadramento","reenquadramento", JoinType.LEFT_OUTER_JOIN);

		
		if(TipoLoteBoletoEnum.REMESSA.getId().equals(lote.getIdeTipoLoteBoleto().getIdeTipoLoteBoleto())){
			criteria.createAlias("bpr.ideLoteRemessaBoleto","remessa", JoinType.INNER_JOIN);
			criteria.add(Restrictions.eq("remessa.ideLoteRemessaBoleto", lote.getLoteRemessaBoleto().getIdeLoteRemessaBoleto()));
		} else if(TipoLoteBoletoEnum.RETORNO.getId().equals(lote.getIdeTipoLoteBoleto().getIdeTipoLoteBoleto())){
			criteria.createAlias("bpr.ideLoteRetornoBoleto","retorno", JoinType.INNER_JOIN);
			criteria.add(Restrictions.eq("retorno.ideLoteRetornoBoleto", lote.getLoteRetornoBoleto().getIdeLoteRetornoBoleto()));
		}
		
		criteria.setProjection(Projections.projectionList()
			.add(Projections.property("bpr.numBoleto"),"numBoleto")
			.add(Projections.property("bpr.dtcVencimento"),"dtcVencimento")
			.add(Projections.property("bpr.ideBoletoPagamentoRequerimento"),"ideBoletoPagamentoRequerimento")
			.add(Projections.property("bpr.valBoleto"),"valBoleto")
			.add(Projections.property("bpr.valBoletoOutorga"),"valBoletoOutorga")
			.add(Projections.property("bpr.dtcEmissao"),"dtcEmissao")
			.add(Projections.property("bpr.indIsento"),"indIsento")
			
			.add(Projections.property("requerimento.numRequerimento"),"ideRequerimento.numRequerimento")
			.add(Projections.property("requerimento.ideRequerimento"),"ideRequerimento.ideRequerimento")
			.add(Projections.property("requerimento.desEmail"),"ideRequerimento.desEmail")
			
			.add(Projections.property("processo.numProcesso"),"ideProcesso.numProcesso")
			.add(Projections.property("processo.ideProcesso"),"ideProcesso.ideProcesso")
			.add(Projections.property("processo.ideRequerimento.ideRequerimento"),"ideProcesso.ideRequerimento.ideRequerimento")
			
			.add(Projections.property("tipo.ideTipoBoletoPagamento"),"ideTipoBoletoPagamento.ideTipoBoletoPagamento")
			.add(Projections.property("tipo.nomTipoBoletoPagamento"),"ideTipoBoletoPagamento.nomTipoBoletoPagamento")
			.add(Projections.property("tipo.indRequerimento"),"ideTipoBoletoPagamento.indRequerimento")
			.add(Projections.property("tipo.indProcesso"),"ideTipoBoletoPagamento.indProcesso")
			.add(Projections.property("tipo.indAtivo"),"ideTipoBoletoPagamento.indAtivo")
			
			.add(Projections.property("reenquadramento.ideProcessoReenquadramento"),"ideProcessoReenquadramento.ideProcessoReenquadramento")
		);
		
		criteria.addOrder(Order.asc("bpr.numBoleto"));
		criteria.setResultTransformer(new AliasToNestedBeanResultTransformer(BoletoPagamentoRequerimento.class));
		
		return boletoPagamentoRequerimentoDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<BoletoPagamentoRequerimento> listarBoletosRegistradosNaoRemetidos()  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(BoletoPagamentoRequerimento.class, "bpr");
		criteria.createAlias("bpr.ideRequerimento","requerimento", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("bpr.ideProcesso","processo", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("bpr.boletoPagamentoHistoricoCollection","historico", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("bpr.ideProcessoReenquadramento","reenquadramento", JoinType.LEFT_OUTER_JOIN);
		
		criteria.add(Restrictions.eq("bpr.indIsento", Boolean.FALSE));
		criteria.add(Restrictions.eq("bpr.indBoletoRegistrado", Boolean.TRUE));
		criteria.add(Restrictions.eq("bpr.indBoletoGeradoManualmente", Boolean.FALSE));
		criteria.add(Restrictions.isNull("bpr.ideLoteRemessaBoleto"));
		criteria.add(Restrictions.sqlRestriction(
				"this_.ide_boleto_pagamento_requerimento not in("
			  + " SELECT ide_boleto_pagamento_requerimento FROM boleto_pagamento_requerimento b "
			  + " LEFT JOIN boleto_pagamento_historico h on b.ide_boleto_pagamento_requerimento = h.ide_boleto_pagamento"
			  + " WHERE ide_tipo_boleto_pagamento = 1 AND h.ide_status_boleto_pagamento = 5) "));
		
		criteria.setProjection(Projections.projectionList()
			.add(Projections.distinct(Projections.property("bpr.ideBoletoPagamentoRequerimento")),"ideBoletoPagamentoRequerimento")
			.add(Projections.property("bpr.dtcVencimento"),"dtcVencimento")
			.add(Projections.property("bpr.valBoleto"),"valBoleto")
			.add(Projections.property("bpr.numBoleto"),"numBoleto")
			.add(Projections.property("bpr.dtcEmissao"),"dtcEmissao")
			.add(Projections.property("bpr.boletoPagamentoRequerimento"),"boletoPagamentoRequerimento")
			.add(Projections.property("bpr.indIsento"),"indIsento")
			.add(Projections.property("bpr.ideRequerimento"),"ideRequerimento")
			.add(Projections.property("bpr.ideLoteRemessaBoleto"),"ideLoteRemessaBoleto")
			.add(Projections.property("bpr.ideLoteRetornoBoleto"),"ideLoteRetornoBoleto")
			.add(Projections.property("bpr.indBoletoRegistrado"),"indBoletoRegistrado")
			.add(Projections.property("bpr.idePessoa"),"idePessoa")
			.add(Projections.property("bpr.ideTipoBoletoPagamento"),"ideTipoBoletoPagamento")
			
			.add(Projections.property("requerimento.numRequerimento"),"ideRequerimento.numRequerimento")
			.add(Projections.property("requerimento.ideRequerimento"),"ideRequerimento.ideRequerimento")
			
			.add(Projections.property("processo.numProcesso"),"ideProcesso.numProcesso")
			.add(Projections.property("processo.ideProcesso"),"ideProcesso.ideProcesso")
			
			.add(Projections.property("reenquadramento.ideProcessoReenquadramento"),"ideProcessoReenquadramento.ideProcessoReenquadramento")
		);
		
		criteria.addOrder(Order.asc("bpr.numBoleto"));
		criteria.setResultTransformer(new AliasToNestedBeanResultTransformer(BoletoPagamentoRequerimento.class));
		
		return boletoPagamentoRequerimentoDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public BoletoPagamentoRequerimento obterBoletoPorNumeroBoleto(String numeroBoleto) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(BoletoPagamentoRequerimento.class);
		criteria
		
			.add(Restrictions.eq("numBoleto", numeroBoleto))
			
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideBoletoPagamentoRequerimento"),"ideBoletoPagamentoRequerimento")
				.add(Projections.property("dtcVencimento"),"dtcVencimento")
				.add(Projections.property("valBoleto"),"valBoleto")
				.add(Projections.property("numBoleto"),"numBoleto")
				.add(Projections.property("dtcEmissao"),"dtcEmissao")
				.add(Projections.property("boletoPagamentoRequerimento"),"boletoPagamentoRequerimento")
				.add(Projections.property("valBoletoOutorga"),"valBoletoOutorga")
				.add(Projections.property("indIsento"),"indIsento")
				.add(Projections.property("indBoletoGeradoManualmente"),"indBoletoGeradoManualmente")
				.add(Projections.property("desCaminhoBoleto"),"desCaminhoBoleto")
				.add(Projections.property("indBoletoRegistrado"),"indBoletoRegistrado")
				.add(Projections.property("dtcPagamento"),"dtcPagamento")
				.add(Projections.property("ideRequerimento.ideRequerimento"),"ideRequerimento.ideRequerimento")
				.add(Projections.property("idePessoa.idePessoa"),"idePessoa.idePessoa")
				.add(Projections.property("ideMotivoIsencaoBoleto.ideMotivoIsencaoBoleto"),"ideMotivoIsencaoBoleto.ideMotivoIsencaoBoleto")
				.add(Projections.property("ideProcesso.ideProcesso"),"ideProcesso.ideProcesso")
				.add(Projections.property("ideTipoBoletoPagamento.ideTipoBoletoPagamento"),"ideTipoBoletoPagamento.ideTipoBoletoPagamento")
				.add(Projections.property("ideLoteRemessaBoleto.ideLoteRemessaBoleto"),"ideLoteRemessaBoleto.ideLoteRemessaBoleto")
				.add(Projections.property("ideLoteRetornoBoleto.ideLoteRetornoBoleto"),"ideLoteRetornoBoleto.ideLoteRetornoBoleto")
				.add(Projections.property("ideProcessoReenquadramento.ideProcessoReenquadramento"),"ideProcessoReenquadramento.ideProcessoReenquadramento")
			)
			
			.setResultTransformer(new AliasToNestedBeanResultTransformer(BoletoPagamentoRequerimento.class))
		;
		
		return boletoPagamentoRequerimentoDAO.buscarPorCriteria(criteria);
		
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<BoletoPagamentoRequerimento> listarBoletosPorIdeRequerimento(Integer ideRequerimento)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(BoletoPagamentoRequerimento.class, "bpr");
		criteria.createAlias("bpr.ideRequerimento","requerimento", JoinType.INNER_JOIN);
		criteria.createAlias("bpr.ideLoteRetornoBoleto","retorno", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("bpr.boletoPagamentoHistoricoCollection","historico", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("bpr.ideTipoBoletoPagamento","tipo", JoinType.INNER_JOIN);
		
		criteria.setProjection(Projections.projectionList()
			.add(Projections.property("bpr.ideBoletoPagamentoRequerimento"),"ideBoletoPagamentoRequerimento")
			.add(Projections.property("bpr.numBoleto"),"numBoleto")
			.add(Projections.property("bpr.dtcVencimento"),"dtcVencimento")
			.add(Projections.property("bpr.dtcPagamento"),"dtcPagamento")
			.add(Projections.property("requerimento.numRequerimento"),"ideRequerimento.numRequerimento")
			.add(Projections.property("requerimento.ideRequerimento"),"ideRequerimento.ideRequerimento")
			.add(Projections.property("retorno.ideLoteRetornoBoleto"),"ideLoteRetornoBoleto.ideLoteRetornoBoleto")
		);
		
		criteria.add(Restrictions.sqlRestriction( //exclui os boletos cancelados
				"this_.ide_boleto_pagamento_requerimento not in("
			  + " SELECT ide_boleto_pagamento_requerimento FROM boleto_pagamento_requerimento b "
			  + " LEFT JOIN boleto_pagamento_historico h on b.ide_boleto_pagamento_requerimento = h.ide_boleto_pagamento"
			  + " WHERE ide_tipo_boleto_pagamento = 1 AND h.ide_status_boleto_pagamento = 5) "));
		criteria.add(Restrictions.eq("requerimento.ideRequerimento", ideRequerimento));
		criteria.add(Restrictions.eq("tipo.ideTipoBoletoPagamento", TipoBoletoPagamentoEnum.REQUERIMENTO.getId()));
		criteria.add(Restrictions.eq("bpr.indIsento", false));
		criteria.addOrder(Order.asc("bpr.numBoleto"));
		criteria.setResultTransformer(new AliasToNestedBeanResultTransformer(BoletoPagamentoRequerimento.class));
		
		return boletoPagamentoRequerimentoDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<BoletoPagamentoRequerimento> listarBoletosComplementarPorIdeRequerimento(Integer ideRequerimento){
		
		DetachedCriteria criteria = DetachedCriteria.forClass(BoletoPagamentoRequerimento.class);
		
		criteria
			.createAlias("ideRequerimento","r", JoinType.INNER_JOIN)
			.createAlias("ideTipoBoletoPagamento","tbp", JoinType.INNER_JOIN)
			.createAlias("ideLoteRetornoBoleto","lrb", JoinType.LEFT_OUTER_JOIN)
			.createAlias("boletoPagamentoHistoricoCollection","bph", JoinType.INNER_JOIN)
			.createAlias("bph.ideStatusBoletoPagamento","sbp", JoinType.INNER_JOIN)
			
			.add(Restrictions.eq("r.ideRequerimento", ideRequerimento))
			.add(Restrictions.eq("tbp.ideTipoBoletoPagamento", TipoBoletoPagamentoEnum.REQUERIMENTO_BOLETO_COMPLEMENTAR.getId()))
			.add(Restrictions.eq("indIsento", false))
			
			.add(Property.forName("bph.ideBoletoPagamentoHistorico").eq(
					DetachedCriteria.forClass(BoletoPagamentoHistorico.class, "bph2")
					.add(Restrictions.eqProperty("bph.ideBoletoPagamento", "bph2.ideBoletoPagamento"))
					.setProjection(Projections.max("bph2.ideBoletoPagamentoHistorico"))
				)
			)
			
			.add(Restrictions.not(Restrictions.in("sbp.ideStatusBoletoPagamento", new Integer[] {StatusBoletoEnum.CANCELADO.getId(),StatusBoletoEnum.PAGO.getId()})))
			
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideBoletoPagamentoRequerimento"),"ideBoletoPagamentoRequerimento")
				.add(Projections.property("dtcVencimento"),"dtcVencimento")
				.add(Projections.property("valBoleto"),"valBoleto")
				.add(Projections.property("numBoleto"),"numBoleto")
				.add(Projections.property("dtcEmissao"),"dtcEmissao")
				.add(Projections.property("boletoPagamentoRequerimento"),"boletoPagamentoRequerimento")
				.add(Projections.property("indIsento"),"indIsento")
				.add(Projections.property("indBoletoGeradoManualmente"),"indBoletoGeradoManualmente")
				.add(Projections.property("desCaminhoBoleto"),"desCaminhoBoleto")
				.add(Projections.property("indBoletoRegistrado"),"indBoletoRegistrado")
				.add(Projections.property("dtcPagamento"),"dtcPagamento")
				.add(Projections.property("r.ideRequerimento"),"ideRequerimento.ideRequerimento")
				.add(Projections.property("idePessoa.idePessoa"),"idePessoa.idePessoa")
				.add(Projections.property("ideMotivoIsencaoBoleto.ideMotivoIsencaoBoleto"),"ideMotivoIsencaoBoleto.ideMotivoIsencaoBoleto")
				.add(Projections.property("ideProcesso.ideProcesso"),"ideProcesso.ideProcesso")
				.add(Projections.property("tbp.ideTipoBoletoPagamento"),"ideTipoBoletoPagamento.ideTipoBoletoPagamento")
				.add(Projections.property("ideLoteRemessaBoleto.ideLoteRemessaBoleto"),"ideLoteRemessaBoleto.ideLoteRemessaBoleto")
				.add(Projections.property("lrb.ideLoteRetornoBoleto"),"ideLoteRetornoBoleto.ideLoteRetornoBoleto")
				.add(Projections.property("ideProcessoReenquadramento.ideProcessoReenquadramento"),"ideProcessoReenquadramento.ideProcessoReenquadramento")
			)
			
			.setResultTransformer(new AliasToNestedBeanResultTransformer(BoletoPagamentoRequerimento.class));
		;
		
		return boletoPagamentoRequerimentoDAO.listarPorCriteria(criteria);
	}
}
