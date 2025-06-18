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
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.ProdutoExecucao;
import br.gov.ba.seia.entity.ProjetoAcaoProduto;
import br.gov.ba.seia.entity.Tcca;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ProdutoExecucaoDaoImpl {
	
	@Inject
	private IDAO<ProdutoExecucao> produtoExecucaoDao;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ProdutoExecucao buscarPorID(Integer ideProdutoExecucao) {
		
		DetachedCriteria criteria = getCriteriaBuscarProdutoExecucao()
			.add(Restrictions.eq("execucao.ideProdutoExecucao", ideProdutoExecucao));		
		
		return produtoExecucaoDao.buscarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ProdutoExecucao buscarPorProduto(ProjetoAcaoProduto pap)  {
		
		DetachedCriteria criteria = getCriteriaBuscarProdutoExecucao()
			.add(Restrictions.eq("ideProjetoAcaoProduto.ideProjetoAcaoProduto", pap.getIdeProjetoAcaoProduto()));		
		
		return produtoExecucaoDao.buscarPorCriteria(criteria);
	}
	
	private DetachedCriteria getCriteriaBuscarProdutoExecucao() {
		return DetachedCriteria.forClass(ProdutoExecucao.class, "execucao")
			.createAlias("execucao.ideProjetoAcaoProduto", "pap", JoinType.INNER_JOIN)
		
		.setProjection(Projections.projectionList()
			.add(Projections.property("execucao.ideProdutoExecucao"), "ideProdutoExecucao")
			.add(Projections.property("execucao.valContratado"), "valContratado")
			.add(Projections.property("execucao.valExecutado"), "valExecutado")
			.add(Projections.property("execucao.valPrevisto"), "valPrevisto")
			.add(Projections.property("pap.ideProjetoAcaoProduto"), "ideProjetoAcaoProduto.ideProjetoAcaoProduto"))
		.setResultTransformer(new AliasToNestedBeanResultTransformer(ProdutoExecucao.class));
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProdutoExecucao> listarPorTcca(Tcca tcca) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(ProdutoExecucao.class, "execucao")
				.createAlias("execucao.ideProjetoAcaoProduto", "pap", JoinType.INNER_JOIN)
				.createAlias("pap.ideProjetoAcao", "pa", JoinType.INNER_JOIN)
				.createAlias("pa.ideTccaProjeto", "tp", JoinType.INNER_JOIN)
				.createAlias("tp.ideTcca", "tcca", JoinType.INNER_JOIN);
				
		ProjectionList projecao = Projections.projectionList()
				.add(Projections.property("execucao.ideProdutoExecucao"), "ideProdutoExecucao")
				.add(Projections.property("execucao.valContratado"), "valContratado")
				.add(Projections.property("execucao.valExecutado"), "valExecutado")
				.add(Projections.property("execucao.valPrevisto"), "valPrevisto")
				
				.add(Projections.property("pap.ideProjetoAcaoProduto"), "ideProjetoAcaoProduto.ideProjetoAcaoProduto")
				.add(Projections.property("pa.ideProjetoAcao"), "ideProjetoAcaoProduto.ideProjetoAcao.ideProjetoAcao")
				.add(Projections.property("tp.ideTccaProjeto"), "ideProjetoAcaoProduto.ideProjetoAcao.ideTccaProjeto.ideTccaProjeto")
				.add(Projections.property("tcca.ideTcca"), "ideProjetoAcaoProduto.ideProjetoAcao.ideTccaProjeto.ideTcca.ideTcca");
		
		criteria.setProjection(projecao).setResultTransformer(new AliasToNestedBeanResultTransformer(ProdutoExecucao.class));	
		criteria.add(Restrictions.eq("tcca.ideTcca", tcca.getIdeTcca()));
		
		return produtoExecucaoDao.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(ProdutoExecucao produtoExecucao) {
		try {
			produtoExecucaoDao.salvarOuAtualizar(produtoExecucao);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
}