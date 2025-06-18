package br.gov.ba.seia.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.apache.log4j.Level;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.ProdutoSaldo;
import br.gov.ba.seia.entity.ProjetoAcaoProduto;
import br.gov.ba.seia.entity.TccaProjeto;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ProdutoSaldoDaoImpl {
	
	@Inject
	private IDAO<ProdutoSaldo> produdoSaldoDAO;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(ProdutoSaldo produtoSaldo) {
		try {
			produdoSaldoDAO.salvarOuAtualizar(produtoSaldo);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(ProdutoSaldo produtoSaldo) {
		try {
			produdoSaldoDAO.remover(produtoSaldo);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ProdutoSaldo buscarUltimoPorProjetoAcaoProduto(ProjetoAcaoProduto pap) {
		try {
			DetachedCriteria criteria = DetachedCriteria.forClass(ProdutoSaldo.class)
				.createAlias("ideMovimentacaoFinanceira", "mf", JoinType.INNER_JOIN)
				.createAlias("ideProjetoAcaoProduto", "pap", JoinType.INNER_JOIN)
				.createAlias("pap.ideProjetoAcao", "pa", JoinType.INNER_JOIN)
				.createAlias("pa.ideTccaProjeto", "proj", JoinType.INNER_JOIN)
				.createAlias("proj.ideTcca", "tcca", JoinType.INNER_JOIN)
				
				.add(Property.forName("ideProdutoSaldo").eq(
					DetachedCriteria.forClass(ProdutoSaldo.class)
						.createAlias("ideMovimentacaoFinanceira", "mf", JoinType.INNER_JOIN)
						.createAlias("ideProjetoAcaoProduto", "pap", JoinType.INNER_JOIN)
						.createAlias("pap.ideProjetoAcao", "pa", JoinType.INNER_JOIN)
						.createAlias("pa.ideTccaProjeto", "proj", JoinType.INNER_JOIN)
						.createAlias("proj.ideTcca", "tcca", JoinType.INNER_JOIN)
					
					.setProjection(Projections.projectionList().add(Projections.max("ideProdutoSaldo"), "ideProdutoSaldo"))
					.add(Restrictions.eq("pap.ideProjetoAcaoProduto", pap.getIdeProjetoAcaoProduto()))
				))
				
				.setProjection(Projections.projectionList()
					.add(Projections.property("ideProdutoSaldo"),"ideProdutoSaldo")
					.add(Projections.property("valSaldoProduto"),"valSaldoProduto")
					.add(Projections.property("valSaldoRemanejado"),"valSaldoRemanejado")
					.add(Projections.property("valSaldoSuplementado"),"valSaldoSuplementado")
					.add(Projections.property("pap.ideProjetoAcaoProduto"),"ideProjetoAcaoProduto.ideProjetoAcaoProduto")
					.add(Projections.property("mf.ideMovimentacaoFinanceira"),"ideMovimentacaoFinanceira.ideMovimentacaoFinanceira"))
				.setResultTransformer(new AliasToNestedBeanResultTransformer(ProdutoSaldo.class));
			
			return produdoSaldoDAO.buscarPorCriteria(criteria);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProdutoSaldo> listarUltimosPorTccaProjeto(TccaProjeto projeto) {
		try {
			DetachedCriteria criteria = DetachedCriteria.forClass(ProdutoSaldo.class)
				.createAlias("ideMovimentacaoFinanceira", "mf", JoinType.INNER_JOIN)
				.createAlias("ideProjetoAcaoProduto", "pap", JoinType.INNER_JOIN)
				.createAlias("pap.ideProjetoAcao", "pa", JoinType.INNER_JOIN)
				.createAlias("pa.ideTccaProjeto", "proj", JoinType.INNER_JOIN)
				.createAlias("proj.ideTcca", "tcca", JoinType.INNER_JOIN)
				
				.add(Property.forName("ideProdutoSaldo").eq(
					DetachedCriteria.forClass(ProdutoSaldo.class)
						.createAlias("ideProjetoAcaoProduto", "pap", JoinType.INNER_JOIN)
						.createAlias("pap.ideProjetoAcao", "pa", JoinType.INNER_JOIN)
						.createAlias("pa.ideTccaProjeto", "proj", JoinType.INNER_JOIN)
						.createAlias("proj.ideTcca", "tcca", JoinType.INNER_JOIN)
					
					.setProjection(Projections.projectionList().add(Projections.max("ideProdutoSaldo"), "ideProdutoSaldo"))
					.add(Restrictions.eq("proj.ideTccaProjeto", projeto.getIdeTccaProjeto()))
				))
				
				.setProjection(Projections.projectionList()
					.add(Projections.property("ideProdutoSaldo"),"ideProdutoSaldo")
					.add(Projections.property("valSaldoProduto"),"valSaldoProduto")
					.add(Projections.property("pap.ideProjetoAcaoProduto"),"ideProjetoAcaoProduto.ideProjetoAcaoProduto")
					.add(Projections.property("mf.ideMovimentacaoFinanceira"),"ideMovimentacaoFinanceira.ideMovimentacaoFinanceira"))
				.setResultTransformer(new AliasToNestedBeanResultTransformer(ProdutoSaldo.class));
			
			return produdoSaldoDAO.listarPorCriteria(criteria);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProdutoSaldo> listarPorProjetoAcaoProduto(ProjetoAcaoProduto pap) {
		try {
			DetachedCriteria criteria = DetachedCriteria.forClass(ProdutoSaldo.class)
				.createAlias("ideMovimentacaoFinanceira", "mf", JoinType.INNER_JOIN)
				.createAlias("mf.ideTccaProjetoOperacao", "tpo", JoinType.INNER_JOIN)
				.createAlias("ideProjetoAcaoProduto", "pap", JoinType.INNER_JOIN)
				.createAlias("pap.ideProjetoAcao", "pa", JoinType.INNER_JOIN)
				.createAlias("pa.ideTccaProjeto", "proj", JoinType.INNER_JOIN)
				.createAlias("proj.ideTcca", "tcca", JoinType.INNER_JOIN)
				
				.setProjection(Projections.projectionList()
					.add(Projections.property("ideProdutoSaldo"),"ideProdutoSaldo")
					.add(Projections.property("valSaldoProduto"),"valSaldoProduto")
					.add(Projections.property("valSaldoRemanejado"),"valSaldoRemanejado")
					.add(Projections.property("valSaldoSuplementado"),"valSaldoSuplementado")
					
					.add(Projections.property("pap.ideProjetoAcaoProduto"),"ideProjetoAcaoProduto.ideProjetoAcaoProduto")
					
					.add(Projections.property("mf.ideMovimentacaoFinanceira"),"ideMovimentacaoFinanceira.ideMovimentacaoFinanceira")
					
					.add(Projections.property("tpo.ideTccaProjetoOperacao"),"ideMovimentacaoFinanceira.ideTccaProjetoOperacao.ideTccaProjetoOperacao")
					.add(Projections.property("tpo.nomOperacao"),"ideMovimentacaoFinanceira.ideTccaProjetoOperacao.nomOperacao"))
				.setResultTransformer(new AliasToNestedBeanResultTransformer(ProdutoSaldo.class))
				
				.add(Restrictions.eq("pap.ideProjetoAcaoProduto", pap.getIdeProjetoAcaoProduto()));
			
			return produdoSaldoDAO.listarPorCriteria(criteria);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
}