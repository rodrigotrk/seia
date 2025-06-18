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
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.ProjetoAcao;
import br.gov.ba.seia.entity.ProjetoAcaoProduto;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ProjetoAcaoProdutoDaoImpl {
	
	@Inject
	private IDAO<ProjetoAcaoProduto> projetoAcaoProdutoDao;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(ProjetoAcaoProduto projetoAcaoProduto) {
		try {
			projetoAcaoProdutoDao.salvarOuAtualizar(projetoAcaoProduto);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(ProjetoAcaoProduto projetoAcaoProduto) {
		try {
			projetoAcaoProdutoDao.remover(projetoAcaoProduto);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ProjetoAcaoProduto buscarPorAcaoEProduto(ProjetoAcaoProduto pap) {
		try {
			
			DetachedCriteria criteria = DetachedCriteria.forClass(ProjetoAcaoProduto.class, "pap");
			
			if(pap.getIdeProjetoAcaoProduto() != null) {
				criteria.add(Restrictions.eq("pap.ideProjetoAcaoProduto", pap.getIdeProjetoAcaoProduto()));
			} else {
				criteria.add(Restrictions.eq("pap.ideProjetoAcao.ideProjetoAcao", pap.getIdeProjetoAcao().getIdeProjetoAcao()));
				criteria.add(Restrictions.like("pap.nomProduto", pap.getNomProduto(), MatchMode.EXACT));
			}
			
			return projetoAcaoProdutoDao.buscarPorCriteria(criteria);
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProjetoAcaoProduto> listarPorProjetoAcao(ProjetoAcao acao) {
		try {
			DetachedCriteria criteria = DetachedCriteria.forClass(ProjetoAcaoProduto.class)
				.createAlias("ideProjetoAcao", "acao", JoinType.INNER_JOIN)
				.add(Restrictions.eq("acao.ideProjetoAcao", acao.getIdeProjetoAcao()))
				.add(Restrictions.eq("indExcluido", false))
				
				.setProjection(
					Projections.projectionList()
						.add(Projections.property("ideProjetoAcaoProduto"), "ideProjetoAcaoProduto")
						.add(Projections.property("indExcluido"), "indExcluido")
						.add(Projections.property("nomProduto"), "nomProduto")
						.add(Projections.property("ideProjetoAcao"), "ideProjetoAcao"))
				.setResultTransformer(new AliasToNestedBeanResultTransformer(ProjetoAcaoProduto.class));
			
			return projetoAcaoProdutoDao.listarPorCriteria(criteria);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	
	/**@author alexandre.queiroz
	 * @since 05/10/2016
	 **/
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProjetoAcaoProduto> listarPorProjetoAcaoBy(ProjetoAcao acao) {
		try {
			DetachedCriteria criteria = DetachedCriteria.forClass(ProjetoAcaoProduto.class)
				.createAlias("ideProjetoAcao", "acao", JoinType.INNER_JOIN)
				.createAlias("ideProdutoExecucao", "ideProdutoExecucao", JoinType.LEFT_OUTER_JOIN)
				.add(Restrictions.eq("acao.ideProjetoAcao", acao.getIdeProjetoAcao()))
				.add(Restrictions.eq("indExcluido", false))
				
				.setProjection(
					Projections.projectionList()
						.add(Projections.property("ideProjetoAcaoProduto"), "ideProjetoAcaoProduto")
						.add(Projections.property("indExcluido"), "indExcluido")
						.add(Projections.property("nomProduto"), "nomProduto")
						.add(Projections.property("nomProduto"), "nomProduto")
						.add(Projections.property("ideProdutoExecucao.ideProdutoExecucao"), "ideProdutoExecucao.ideProdutoExecucao")
						.add(Projections.property("ideProdutoExecucao.valContratado"), "ideProdutoExecucao.valContratado")
						.add(Projections.property("ideProdutoExecucao.valExecutado"), "ideProdutoExecucao.valExecutado")
						.add(Projections.property("ideProdutoExecucao.valPrevisto"), "ideProdutoExecucao.valPrevisto")
						.add(Projections.property("ideProjetoAcao"), "ideProjetoAcao"))
				.setResultTransformer(new AliasToNestedBeanResultTransformer(ProjetoAcaoProduto.class));
			
			return projetoAcaoProdutoDao.listarPorCriteria(criteria);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	
}