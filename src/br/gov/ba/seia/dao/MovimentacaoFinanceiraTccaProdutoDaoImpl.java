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
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.MovimentacaoFinanceiraTccaProduto;
import br.gov.ba.seia.entity.ProjetoAcaoProduto;
import br.gov.ba.seia.entity.Tcca;
import br.gov.ba.seia.entity.TccaProjeto;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class MovimentacaoFinanceiraTccaProdutoDaoImpl {
	
	@Inject
	private IDAO<MovimentacaoFinanceiraTccaProduto> movimentacaoFinanceiraTccaProdutoDao;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(MovimentacaoFinanceiraTccaProduto mov) {
		try {
			movimentacaoFinanceiraTccaProdutoDao.salvarOuAtualizar(mov);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<MovimentacaoFinanceiraTccaProduto> listarPorTccaProjeto(TccaProjeto proj) {
		try{
			DetachedCriteria criteria = DetachedCriteria.forClass(MovimentacaoFinanceiraTccaProduto.class, "movFTP")
				.createAlias("movFTP.ideTipoOrigem", "tipoOrigem", JoinType.LEFT_OUTER_JOIN)
				.createAlias("movFTP.ideTipoDestino", "tipoDestino", JoinType.LEFT_OUTER_JOIN)
				
				.createAlias("movFTP.ideProjetoAcaoProdutoOrigem", "papOrigem", JoinType.LEFT_OUTER_JOIN)
				.createAlias("movFTP.ideProjetoAcaoProdutoDestino", "papDestino", JoinType.LEFT_OUTER_JOIN)
				
				.createAlias("movFTP.ideTccaOrigem", "tccaOrigem", JoinType.LEFT_OUTER_JOIN)
				.createAlias("movFTP.ideTccaDestino", "tccaDestino", JoinType.LEFT_OUTER_JOIN)
				
				.createAlias("movFTP.ideMovimentacaoFinanceira","movF", JoinType.INNER_JOIN)
				.createAlias("movF.ideProdutoExecucao", "execucao", JoinType.LEFT_OUTER_JOIN)
				.createAlias("movF.ideTccaProjetoOperacao", "operacao", JoinType.INNER_JOIN)
				.createAlias("movF.ideProdutoSaldo", "produtoSaldo", JoinType.INNER_JOIN)
				
				.createAlias("produtoSaldo.ideProjetoAcaoProduto", "produto", JoinType.INNER_JOIN)
				.createAlias("produto.ideProjetoAcao", "acao", JoinType.INNER_JOIN)
				.createAlias("acao.ideTccaProjeto", "projeto", JoinType.INNER_JOIN)
				
				.setProjection(Projections.projectionList()
					.add(Projections.property("movFTP.ideMovimentacaoFinanceiraTccaProduto"), "ideMovimentacaoFinanceiraTccaProduto")
					
					.add(Projections.property("tipoOrigem.ideTipoOrigemDestino"), "ideTipoOrigem.ideTipoOrigemDestino")
					.add(Projections.property("tipoOrigem.nomTipoOrigemDestino"), "ideTipoOrigem.nomTipoOrigemDestino")
					.add(Projections.property("tipoDestino.ideTipoOrigemDestino"), "ideTipoDestino.ideTipoOrigemDestino")
					.add(Projections.property("tipoDestino.nomTipoOrigemDestino"), "ideTipoDestino.nomTipoOrigemDestino")
					
					.add(Projections.property("papOrigem.ideProjetoAcaoProduto"), "ideProjetoAcaoProdutoOrigem.ideProjetoAcaoProduto")
					.add(Projections.property("papOrigem.nomProduto"), "ideProjetoAcaoProdutoOrigem.nomProduto")	
					.add(Projections.property("papDestino.ideProjetoAcaoProduto"), "ideProjetoAcaoProdutoDestino.ideProjetoAcaoProduto")
					.add(Projections.property("papDestino.nomProduto"), "ideProjetoAcaoProdutoDestino.nomProduto")
					
					.add(Projections.property("tccaOrigem.ideTcca"), "ideTccaOrigem.ideTcca")
					.add(Projections.property("tccaOrigem.numTcca"), "ideTccaOrigem.numTcca")
					.add(Projections.property("tccaDestino.ideTcca"), "ideTccaDestino.ideTcca")
					.add(Projections.property("tccaDestino.numTcca"), "ideTccaDestino.numTcca")
					
					.add(Projections.property("movF.ideMovimentacaoFinanceira"), "ideMovimentacaoFinanceira.ideMovimentacaoFinanceira")
					.add(Projections.property("movF.dtcOperacao"), "ideMovimentacaoFinanceira.dtcOperacao")
					.add(Projections.property("movF.valOperacao"), "ideMovimentacaoFinanceira.valOperacao")
					
					.add(Projections.property("operacao.ideTccaProjetoOperacao"), "ideMovimentacaoFinanceira.ideTccaProjetoOperacao.ideTccaProjetoOperacao")
					.add(Projections.property("operacao.nomOperacao"), "ideMovimentacaoFinanceira.ideTccaProjetoOperacao.nomOperacao")
					.add(Projections.property("operacao.indOculto"), "ideMovimentacaoFinanceira.ideTccaProjetoOperacao.indOculto")
					
					.add(Projections.property("execucao.ideProdutoExecucao"), "ideMovimentacaoFinanceira.ideProdutoExecucao.ideProdutoExecucao")
					.add(Projections.property("execucao.valContratado"), "ideMovimentacaoFinanceira.ideProdutoExecucao.valContratado")
					.add(Projections.property("execucao.valExecutado"), "ideMovimentacaoFinanceira.ideProdutoExecucao.valExecutado")
					.add(Projections.property("execucao.valPrevisto"), "ideMovimentacaoFinanceira.ideProdutoExecucao.valPrevisto")
					
					.add(Projections.property("produtoSaldo.ideProdutoSaldo"), "ideMovimentacaoFinanceira.ideProdutoSaldo.ideProdutoSaldo")
					.add(Projections.property("produtoSaldo.valSaldoProduto"), "ideMovimentacaoFinanceira.ideProdutoSaldo.valSaldoProduto")
					.add(Projections.property("produtoSaldo.valSaldoRemanejado"), "ideMovimentacaoFinanceira.ideProdutoSaldo.valSaldoRemanejado")
					.add(Projections.property("produtoSaldo.valSaldoSuplementado"), "ideMovimentacaoFinanceira.ideProdutoSaldo.valSaldoSuplementado")
					
					.add(Projections.property("produto.ideProjetoAcaoProduto"), "ideMovimentacaoFinanceira.ideProdutoExecucao.ideProjetoAcaoProduto.ideProjetoAcaoProduto")
					.add(Projections.property("acao.ideProjetoAcao"), "ideMovimentacaoFinanceira.ideProdutoExecucao.ideProjetoAcaoProduto.ideProjetoAcao.ideProjetoAcao")
					.add(Projections.property("projeto.ideTccaProjeto"), "ideMovimentacaoFinanceira.ideProdutoExecucao.ideProjetoAcaoProduto.ideProjetoAcao.ideTccaProjeto.ideTccaProjeto")
					
				).setResultTransformer(new AliasToNestedBeanResultTransformer(MovimentacaoFinanceiraTccaProduto.class))
				
				.add(Restrictions.eq("projeto.ideTccaProjeto", proj.getIdeTccaProjeto()));
			
			return movimentacaoFinanceiraTccaProdutoDao.listarPorCriteria(criteria, Order.desc("ideMovimentacaoFinanceiraTccaProduto"));
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<MovimentacaoFinanceiraTccaProduto> listarPorTcca(Tcca tcca) {
		try{
			DetachedCriteria criteria = DetachedCriteria.forClass(MovimentacaoFinanceiraTccaProduto.class, "movFTP")
				.createAlias("movFTP.ideTipoOrigem", "tipoOrigem", JoinType.LEFT_OUTER_JOIN)
				.createAlias("movFTP.ideTipoDestino", "tipoDestino", JoinType.LEFT_OUTER_JOIN)
				
				.createAlias("movFTP.ideProjetoAcaoProdutoOrigem", "papOrigem", JoinType.LEFT_OUTER_JOIN)
				.createAlias("movFTP.ideProjetoAcaoProdutoDestino", "papDestino", JoinType.LEFT_OUTER_JOIN)
				
				.createAlias("movFTP.ideTccaOrigem", "tccaOrigem", JoinType.LEFT_OUTER_JOIN)
				.createAlias("movFTP.ideTccaDestino", "tccaDestino", JoinType.LEFT_OUTER_JOIN)
				
				.createAlias("movFTP.ideMovimentacaoFinanceira","movF", JoinType.INNER_JOIN)
				.createAlias("movF.ideTccaProjetoOperacao", "operacao", JoinType.INNER_JOIN)
				.createAlias("movF.ideTccaSaldo", "tccaSaldo", JoinType.LEFT_OUTER_JOIN)
				.createAlias("tccaSaldo.ideTcca", "tcca", JoinType.LEFT_OUTER_JOIN)
				
				.setProjection(Projections.projectionList()
					.add(Projections.property("movFTP.ideMovimentacaoFinanceiraTccaProduto"), "ideMovimentacaoFinanceiraTccaProduto")
					
					.add(Projections.property("tipoOrigem.ideTipoOrigemDestino"), "ideTipoOrigem.ideTipoOrigemDestino")
					.add(Projections.property("tipoOrigem.nomTipoOrigemDestino"), "ideTipoOrigem.nomTipoOrigemDestino")
					.add(Projections.property("tipoDestino.ideTipoOrigemDestino"), "ideTipoDestino.ideTipoOrigemDestino")
					.add(Projections.property("tipoDestino.nomTipoOrigemDestino"), "ideTipoDestino.nomTipoOrigemDestino")
					
					.add(Projections.property("papOrigem.ideProjetoAcaoProduto"), "ideProjetoAcaoProdutoOrigem.ideProjetoAcaoProduto")
					.add(Projections.property("papOrigem.nomProduto"), "ideProjetoAcaoProdutoOrigem.nomProduto")	
					.add(Projections.property("papDestino.ideProjetoAcaoProduto"), "ideProjetoAcaoProdutoDestino.ideProjetoAcaoProduto")
					.add(Projections.property("papDestino.nomProduto"), "ideProjetoAcaoProdutoDestino.nomProduto")
					
					.add(Projections.property("tccaOrigem.ideTcca"), "ideTccaOrigem.ideTcca")
					.add(Projections.property("tccaOrigem.numTcca"), "ideTccaOrigem.numTcca")
					.add(Projections.property("tccaDestino.ideTcca"), "ideTccaDestino.ideTcca")
					.add(Projections.property("tccaDestino.numTcca"), "ideTccaDestino.numTcca")
					
					.add(Projections.property("movF.ideMovimentacaoFinanceira"), "ideMovimentacaoFinanceira.ideMovimentacaoFinanceira")
					.add(Projections.property("movF.dtcOperacao"), "ideMovimentacaoFinanceira.dtcOperacao")
					.add(Projections.property("movF.valOperacao"), "ideMovimentacaoFinanceira.valOperacao")
					
					.add(Projections.property("operacao.ideTccaProjetoOperacao"), "ideMovimentacaoFinanceira.ideTccaProjetoOperacao.ideTccaProjetoOperacao")
					.add(Projections.property("operacao.nomOperacao"), "ideMovimentacaoFinanceira.ideTccaProjetoOperacao.nomOperacao")
					.add(Projections.property("operacao.indOculto"), "ideMovimentacaoFinanceira.ideTccaProjetoOperacao.indOculto")
					
					.add(Projections.property("tccaSaldo.ideTccaSaldo"), "ideMovimentacaoFinanceira.ideTccaSaldo.ideTccaSaldo")
					.add(Projections.property("tccaSaldo.valSaldoNaoDestinadoProjeto"), "ideMovimentacaoFinanceira.ideTccaSaldo.valSaldoNaoDestinadoProjeto")
					.add(Projections.property("tccaSaldo.valSaldoSuplementado"), "ideMovimentacaoFinanceira.ideTccaSaldo.valSaldoSuplementado")
					
					.add(Projections.property("tcca.ideTcca"), "ideMovimentacaoFinanceira.ideTccaSaldo.ideTcca.ideTcca")
					.add(Projections.property("tcca.numTcca"), "ideMovimentacaoFinanceira.ideTccaSaldo.ideTcca.numTcca")
					
				).setResultTransformer(new AliasToNestedBeanResultTransformer(MovimentacaoFinanceiraTccaProduto.class))
						
				.add(Restrictions.eq("tcca.ideTcca", tcca.getIdeTcca()))
				.add(Restrictions.eq("operacao.indOculto", false));
			
			return movimentacaoFinanceiraTccaProdutoDao.listarPorCriteria(criteria, Order.desc("ideMovimentacaoFinanceiraTccaProduto"));
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<MovimentacaoFinanceiraTccaProduto> listarPorProjetoAcaoProduto(ProjetoAcaoProduto pap) {
		try{
			DetachedCriteria criteria = DetachedCriteria.forClass(MovimentacaoFinanceiraTccaProduto.class, "movFTP")
				.createAlias("movFTP.ideTipoOrigem", "tipoOrigem", JoinType.LEFT_OUTER_JOIN)
				.createAlias("movFTP.ideTipoDestino", "tipoDestino", JoinType.LEFT_OUTER_JOIN)
				
				.createAlias("movFTP.ideProjetoAcaoProdutoOrigem", "papOrigem", JoinType.LEFT_OUTER_JOIN)
				.createAlias("movFTP.ideProjetoAcaoProdutoDestino", "papDestino", JoinType.LEFT_OUTER_JOIN)
				
				.createAlias("movFTP.ideTccaOrigem", "tccaOrigem", JoinType.LEFT_OUTER_JOIN)
				.createAlias("movFTP.ideTccaDestino", "tccaDestino", JoinType.LEFT_OUTER_JOIN)
				
				.createAlias("movFTP.ideMovimentacaoFinanceira","movF", JoinType.INNER_JOIN)
				.createAlias("movF.ideProdutoExecucao", "execucao", JoinType.LEFT_OUTER_JOIN)
				.createAlias("movF.ideTccaProjetoOperacao", "operacao", JoinType.INNER_JOIN)
				.createAlias("movF.ideProdutoSaldo", "produtoSaldo", JoinType.INNER_JOIN)
				
				.createAlias("produtoSaldo.ideProjetoAcaoProduto", "produto", JoinType.INNER_JOIN)
//				.createAlias("execucao.ideProjetoAcaoProduto", "produto", JoinType.INNER_JOIN)
				.createAlias("produto.ideProjetoAcao", "acao", JoinType.INNER_JOIN)
				.createAlias("acao.ideTccaProjeto", "projeto", JoinType.INNER_JOIN)
				
				.setProjection(Projections.projectionList()
					.add(Projections.property("movFTP.ideMovimentacaoFinanceiraTccaProduto"), "ideMovimentacaoFinanceiraTccaProduto")
					
					.add(Projections.property("tipoOrigem.ideTipoOrigemDestino"), "ideTipoOrigem.ideTipoOrigemDestino")
					.add(Projections.property("tipoOrigem.nomTipoOrigemDestino"), "ideTipoOrigem.nomTipoOrigemDestino")
					.add(Projections.property("tipoDestino.ideTipoOrigemDestino"), "ideTipoDestino.ideTipoOrigemDestino")
					.add(Projections.property("tipoDestino.nomTipoOrigemDestino"), "ideTipoDestino.nomTipoOrigemDestino")
					
					.add(Projections.property("papOrigem.ideProjetoAcaoProduto"), "ideProjetoAcaoProdutoOrigem.ideProjetoAcaoProduto")
					.add(Projections.property("papOrigem.nomProduto"), "ideProjetoAcaoProdutoOrigem.nomProduto")	
					.add(Projections.property("papDestino.ideProjetoAcaoProduto"), "ideProjetoAcaoProdutoDestino.ideProjetoAcaoProduto")
					.add(Projections.property("papDestino.nomProduto"), "ideProjetoAcaoProdutoDestino.nomProduto")
					
					.add(Projections.property("tccaOrigem.ideTcca"), "ideTccaOrigem.ideTcca")
					.add(Projections.property("tccaOrigem.numTcca"), "ideTccaOrigem.numTcca")
					.add(Projections.property("tccaDestino.ideTcca"), "ideTccaDestino.ideTcca")
					.add(Projections.property("tccaDestino.numTcca"), "ideTccaDestino.numTcca")
					
					.add(Projections.property("movF.ideMovimentacaoFinanceira"), "ideMovimentacaoFinanceira.ideMovimentacaoFinanceira")
					.add(Projections.property("movF.dtcOperacao"), "ideMovimentacaoFinanceira.dtcOperacao")
					.add(Projections.property("movF.valOperacao"), "ideMovimentacaoFinanceira.valOperacao")
					
					.add(Projections.property("operacao.ideTccaProjetoOperacao"), "ideMovimentacaoFinanceira.ideTccaProjetoOperacao.ideTccaProjetoOperacao")
					.add(Projections.property("operacao.nomOperacao"), "ideMovimentacaoFinanceira.ideTccaProjetoOperacao.nomOperacao")
					.add(Projections.property("operacao.indOculto"), "ideMovimentacaoFinanceira.ideTccaProjetoOperacao.indOculto")
					
					.add(Projections.property("execucao.ideProdutoExecucao"), "ideMovimentacaoFinanceira.ideProdutoExecucao.ideProdutoExecucao")
					.add(Projections.property("execucao.valContratado"), "ideMovimentacaoFinanceira.ideProdutoExecucao.valContratado")
					.add(Projections.property("execucao.valExecutado"), "ideMovimentacaoFinanceira.ideProdutoExecucao.valExecutado")
					.add(Projections.property("execucao.valPrevisto"), "ideMovimentacaoFinanceira.ideProdutoExecucao.valPrevisto")
					
					.add(Projections.property("produtoSaldo.ideProdutoSaldo"), "ideMovimentacaoFinanceira.ideProdutoSaldo.ideProdutoSaldo")
					.add(Projections.property("produtoSaldo.valSaldoProduto"), "ideMovimentacaoFinanceira.ideProdutoSaldo.valSaldoProduto")
					.add(Projections.property("produtoSaldo.valSaldoRemanejado"), "ideMovimentacaoFinanceira.ideProdutoSaldo.valSaldoRemanejado")
					.add(Projections.property("produtoSaldo.valSaldoSuplementado"), "ideMovimentacaoFinanceira.ideProdutoSaldo.valSaldoSuplementado")
					
					.add(Projections.property("produto.ideProjetoAcaoProduto"), "ideMovimentacaoFinanceira.ideProdutoExecucao.ideProjetoAcaoProduto.ideProjetoAcaoProduto")
					.add(Projections.property("acao.ideProjetoAcao"), "ideMovimentacaoFinanceira.ideProdutoExecucao.ideProjetoAcaoProduto.ideProjetoAcao.ideProjetoAcao")
					.add(Projections.property("projeto.ideTccaProjeto"), "ideMovimentacaoFinanceira.ideProdutoExecucao.ideProjetoAcaoProduto.ideProjetoAcao.ideTccaProjeto.ideTccaProjeto")
					
				).setResultTransformer(new AliasToNestedBeanResultTransformer(MovimentacaoFinanceiraTccaProduto.class))
				
				.add(Restrictions.eq("produto.ideProjetoAcaoProduto", pap.getIdeProjetoAcaoProduto()));
			
			return movimentacaoFinanceiraTccaProdutoDao.listarPorCriteria(criteria, Order.desc("ideMovimentacaoFinanceiraTccaProduto"));
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
}