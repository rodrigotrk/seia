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
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.MovimentacaoFinanceira;
import br.gov.ba.seia.entity.ProdutoExecucao;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class MovimentacaoFinanceiraDaoImpl {
	
	@Inject
	private IDAO<MovimentacaoFinanceira> movimentacaoFinanceiraDao;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(MovimentacaoFinanceira mov) {
		try {
			movimentacaoFinanceiraDao.salvarOuAtualizar(mov);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public MovimentacaoFinanceira buscarPorID(MovimentacaoFinanceira movF) {
		try {
			DetachedCriteria criteria = getCriteria().add(Restrictions.eq("ideMovimentacaoFinanceira", movF.getIdeMovimentacaoFinanceira()));
			
			return movimentacaoFinanceiraDao.buscarPorCriteria(criteria);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	private DetachedCriteria getCriteria() {
		DetachedCriteria criteria = DetachedCriteria.forClass(MovimentacaoFinanceira.class, "mov")
			.createAlias("mov.ideTcca", "tccaMov", JoinType.INNER_JOIN)
			.createAlias("mov.idePessoaFisicaOperacao", "pessoa", JoinType.INNER_JOIN)
			.createAlias("mov.ideTccaHistoricoReajusteValor", "reajuste", JoinType.LEFT_OUTER_JOIN)
			.createAlias("mov.ideTccaProjetoOperacao", "operacao", JoinType.INNER_JOIN)
			
			.createAlias("mov.ideProdutoExecucao", "execucao", JoinType.LEFT_OUTER_JOIN)
			.createAlias("execucao.ideProjetoAcaoProduto", "produto", JoinType.LEFT_OUTER_JOIN)
			.createAlias("produto.ideProjetoAcao", "acao", JoinType.LEFT_OUTER_JOIN)
			.createAlias("acao.ideTccaProjeto", "projeto", JoinType.LEFT_OUTER_JOIN)
			.createAlias("projeto.ideTcca", "tcca", JoinType.LEFT_OUTER_JOIN)
			
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideMovimentacaoFinanceira"), "ideMovimentacaoFinanceira")
				.add(Projections.property("dtcOperacao"), "dtcOperacao")
				.add(Projections.property("valOperacao"), "valOperacao")
				.add(Projections.property("numResolucao"), "numResolucao")
				
				.add(Projections.property("tccaMov.ideTcca"), "ideTcca.ideTcca")
				
				.add(Projections.property("operacao.ideTccaProjetoOperacao"), "ideTccaProjetoOperacao.ideTccaProjetoOperacao")
				.add(Projections.property("operacao.nomOperacao"), "ideTccaProjetoOperacao.nomOperacao")
				.add(Projections.property("operacao.indOculto"), "ideTccaProjetoOperacao.indOculto")
				
				.add(Projections.property("pessoa.idePessoaFisica"), "idePessoaFisicaOperacao.idePessoaFisica")
				
				.add(Projections.property("reajuste.ideTccaHistoricoReajusteValor"), "ideTccaHistoricoReajusteValor.ideTccaHistoricoReajusteValor")
				
				.add(Projections.property("execucao.ideProdutoExecucao"), "ideProdutoExecucao.ideProdutoExecucao")
				.add(Projections.property("produto.ideProjetoAcaoProduto"), "ideProdutoExecucao.ideProjetoAcaoProduto.ideProjetoAcaoProduto")
				.add(Projections.property("acao.ideProjetoAcao"), "ideProdutoExecucao.ideProjetoAcaoProduto.ideProjetoAcao.ideProjetoAcao")
				.add(Projections.property("projeto.ideTccaProjeto"), "ideProdutoExecucao.ideProjetoAcaoProduto.ideProjetoAcao.ideTccaProjeto.ideTccaProjeto")
				.add(Projections.property("tcca.ideTcca"), "ideProdutoExecucao.ideProjetoAcaoProduto.ideProjetoAcao.ideTccaProjeto.ideTcca.ideTcca"))
			.setResultTransformer(new AliasToNestedBeanResultTransformer(MovimentacaoFinanceira.class));
		
		return criteria;
	}

	public List<MovimentacaoFinanceira> listarPorProdutoExecucao(ProdutoExecucao pe) {
		try {
			DetachedCriteria criteria = getCriteria().add(Restrictions.eq("execucao.ideProdutoExecucao", pe.getIdeProdutoExecucao()));
			
			return movimentacaoFinanceiraDao.listarPorCriteria(criteria);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
}