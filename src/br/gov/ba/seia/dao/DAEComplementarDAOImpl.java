package br.gov.ba.seia.dao;

import java.util.List;

import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.DateType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;

import br.gov.ba.seia.dto.BoletoComplementarDTO;
import br.gov.ba.seia.entity.BoletoDaeRequerimento;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Util;

public class DAEComplementarDAOImpl {

	@Inject
	private IDAO<BoletoComplementarDTO> boletoDaeRequerimentoDAO;
	
	private static final Integer TIPO_PESSOA_REQUERIMENTO = 1;
	private static final Integer TIPO_BOLETO_PAGAMENTO = 7;
	
	private void adicionarProjection(DetachedCriteria criteria) {
		Projection projection =
				Projections.projectionList()
					.add(Projections.sqlProjection("this_.ide_boleto_dae_requerimento as id", new String[] {"id"}, new Type[] { IntegerType.INSTANCE }))
					.add(Projections.sqlProjection("coalesce(pf5_.nom_pessoa, pj6_.nom_razao_social) as requerente", 
							new String[] {"requerente"}, new Type[] { StringType.INSTANCE }))
					.add(Projections.sqlProjection("null as numeroBoleto", new String[] {"numeroBoleto"}, new Type[] { StringType.INSTANCE }), "numBoleto")							
					.add(Projections.sqlProjection(montarSubQueryDataGeracao(), new String[] {"dtGeracao"}, new Type[] { DateType.INSTANCE }), "dtGeracao")
					.add(Projections.sqlProjection("null as dtVencimento", new String[] {"dtVencimento"}, new Type[] { DateType.INSTANCE }), "dtVencimento")
					.add(Projections.sqlProjection(montarSubQueryNomeStatus(), new String[] {"status"}, new Type[] { StringType.INSTANCE }), "status")
					.add(Projections.sqlProjection(montarSubQueryDataPagamentoCancelamento(), new String[] {"dtPagamentoCancelamento"}, new Type[] { DateType.INSTANCE }), "dtPagamentoCancelamento")
					.add(Projections.sqlProjection(montarSubQueryDataValidacao(), new String[] {"dtValidacao"}, new Type[] { DateType.INSTANCE }), "dtValidacao")
					.add(Projections.sqlProjection("null as valor", new String[] {"valor"}, new Type[] { BigDecimalType.INSTANCE }), "valor")
					.add(Projections.property("req.ideRequerimento"), "requerimento.ideRequerimento")
					.add(Projections.property("req.numRequerimento"), "requerimento.numRequerimento")
					.add(Projections.property("proc.ideProcesso"), "processo.ideProcesso")
					.add(Projections.property("proc.numProcesso"), "processo.numProcesso")
					.add(Projections.sqlProjection(montarSubQueryNomeTipoBoleto(), new String[] {"tipoBoleto"}, new Type[] { StringType.INSTANCE }), "tipoBoleto")
					.add(Projections.property("bpr.vlrTotalCertificado"), "vlrTotalCertificado")
					.add(Projections.property("bpr.vlrTotalVistoria"), "vlrTotalVistoria");
					
		criteria
			.setProjection(projection)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(BoletoComplementarDTO.class));
	}
	
	
	private void adicionarFiltros(DetachedCriteria criteria, Integer ideProcessoReenquadramento) {
		
		criteria.add(Restrictions.eq("req_pes.ideTipoPessoaRequerimento.ideTipoPessoaRequerimento", TIPO_PESSOA_REQUERIMENTO));
		criteria.add(Restrictions.eq("bpr.ideTipoBoletoPagamento.ideTipoBoletoPagamento", TIPO_BOLETO_PAGAMENTO));
		
		criteria.add(Restrictions.eq("bpr.ideProcessoReenquadramento.ideProcessoReenquadramento", ideProcessoReenquadramento));
	}
	
	private void adicionarJoins(DetachedCriteria criteria) {
		criteria
			.createAlias("bpr.ideProcesso", "proc", JoinType.INNER_JOIN)
			.createAlias("proc.ideRequerimento", "req", JoinType.LEFT_OUTER_JOIN)
			.createAlias("req.requerimentoPessoaCollection", "req_pes", JoinType.LEFT_OUTER_JOIN)
			.createAlias("req.requerimentoPessoaCollection.pessoa", "pes", JoinType.LEFT_OUTER_JOIN)
			.createAlias("pes.pessoaFisica", "pf", JoinType.LEFT_OUTER_JOIN)
			.createAlias("pes.pessoaJuridica", "pj", JoinType.LEFT_OUTER_JOIN)
			.createAlias("bpr.boletoDaeHistorico", "bph0", JoinType.INNER_JOIN)
			.createAlias("bph0.ideStatusBoletoPagamento", "sta0", JoinType.INNER_JOIN);
	}
	
	private String montarSubQueryDataGeracao() {
		StringBuilder sql = new StringBuilder(238);
		
		sql.append("(SELECT bph1.dtc_tramitacao "
				.concat("FROM boleto_dae_historico bph1 ")
				.concat("WHERE this_.ide_boleto_dae_requerimento = bph1.ide_boleto_dae_requerimento ")
				.concat("AND bph1.ide_status_boleto_pagamento = 1 ")
				.concat("ORDER BY bph1.ide_boleto_dae_requerimento ")
				.concat("LIMIT 1) AS dtGeracao"));
		
		return sql.toString();
	}
	
	private String montarSubQueryNomeStatus() {
		StringBuilder sql = new StringBuilder(448);
		
		sql.append("(SELECT sta.nom_status_boleto_pagamento "
				.concat("FROM status_boleto_pagamento sta, boleto_dae_historico bph2 ")
				.concat("WHERE sta.ide_status_boleto_pagamento = bph2.ide_status_boleto_pagamento ")
				.concat("AND bph2.ide_boleto_dae_requerimento = this_.ide_boleto_dae_requerimento ")
				.concat("AND bph2.ide_boleto_dae_requerimento = (SELECT MAX(ide_boleto_dae_requerimento) ")
				.concat("FROM boleto_dae_requerimento bph3 ")
				.concat("WHERE bph3.ide_boleto_dae_requerimento = this_.ide_boleto_dae_requerimento)) AS status "));
		
		return sql.toString();
	}
	
	private String montarSubQueryDataPagamentoCancelamento() {
		StringBuilder sql = new StringBuilder(254);
		
		sql.append("(SELECT max(bph4.dtc_tramitacao) "
				.concat("FROM boleto_dae_historico bph4 ")
				.concat("WHERE this_.ide_boleto_dae_requerimento = bph4.ide_boleto_dae_requerimento ")
				.concat("AND ((bph4.ide_status_boleto_pagamento = 3) ")
				.concat("OR (bph4.ide_status_boleto_pagamento = 5))) AS dtPagamentoCancelamento "));
		
		return sql.toString();
	}
	
	private String montarSubQueryDataValidacao() {
		StringBuilder sql = new StringBuilder(193);
		
		sql.append("(SELECT max(bph5.dtc_tramitacao) "
				.concat("FROM boleto_dae_historico bph5 ")
				.concat("WHERE this_.ide_boleto_dae_requerimento = bph5.ide_boleto_dae_requerimento ")
				.concat("AND bph5.ide_status_boleto_pagamento = 4) AS dtValidacao "));
		
		return sql.toString(); 
	}
	
	private String montarSubQueryNomeTipoBoleto() {
		StringBuilder sql = new StringBuilder(142);
		
		sql.append("(SELECT nom_tipo_boleto_pagamento "
				.concat("FROM tipo_boleto_pagamento ")
				.concat("WHERE ide_tipo_boleto_pagamento = this_.ide_tipo_boleto_pagamento) AS tipoBoleto "));
		
		return sql.toString();
	}
	
	private DetachedCriteria montarCriteriaDAE(Integer ideProcessoReenquadramento) {
		DetachedCriteria criteria = DetachedCriteria.forClass(BoletoDaeRequerimento.class, "bpr");
		
		adicionarJoins(criteria);
		adicionarProjection(criteria);
		adicionarFiltros(criteria, ideProcessoReenquadramento);
		
		return criteria;
	}
	
	public BoletoComplementarDTO consultarDAEPorProcessoReenquadramento(Integer ideProcessoReenquadramento) {
		
		DetachedCriteria criteria = montarCriteriaDAE(ideProcessoReenquadramento);
		
		criteria.addOrder(Order.desc("dtGeracao"));
		
		List<BoletoComplementarDTO> collRequerimentoUnicoDTO = boletoDaeRequerimentoDAO.listarPorCriteria(criteria);
		
		if (!Util.isNullOuVazio(collRequerimentoUnicoDTO)){
			return collRequerimentoUnicoDTO.get(0);
		}
		
		return  null;
	}
}
