package br.gov.ba.seia.service;

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
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.Dae;
import br.gov.ba.seia.entity.MemoriaCalculoDaeParcela;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class MemoriaCalculoDaeParcelaService {
	
	@Inject
	private IDAO<MemoriaCalculoDaeParcela> memoriaCalculoDaeParcelaIDAO;
	
	@Inject
	private IDAO<MemoriaCalculoDaeParcela> memoriaCalculoDaeParcelaDAO;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizar(MemoriaCalculoDaeParcela memoriaCalculoDaeParcela)  {
		memoriaCalculoDaeParcelaDAO.salvarOuAtualizar(memoriaCalculoDaeParcela);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public MemoriaCalculoDaeParcela obterMemoriaCalculoDaeParcela(Integer ideMemoriaCalculoDaeParcela)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(MemoriaCalculoDaeParcela.class);
		
		criteria.createAlias("ideMemoriaCalculoDae", "mcd", JoinType.INNER_JOIN);
		criteria.createAlias("mcd.ideCumprimentoReposicaoFlorestal", "crf", JoinType.INNER_JOIN);
		criteria.createAlias("mcd.ideParametroCalculo", "pca", JoinType.INNER_JOIN);
		criteria.createAlias("crf.requerimento", "req", JoinType.INNER_JOIN);
		
		criteria.add(Restrictions.eq("ideMemoriaCalculoDaeParcela", ideMemoriaCalculoDaeParcela));
		
		criteria.setProjection(
				Projections.projectionList()
					.add(Projections.property("ideMemoriaCalculoDaeParcela"), "ideMemoriaCalculoDaeParcela")
					.add(Projections.property("numParcelaReferencia"), "numParcelaReferencia")
					.add(Projections.property("mcd.ideMemoriaCalculoDae"), "ideMemoriaCalculoDae.ideMemoriaCalculoDae")
					.add(Projections.property("mcd.qtdParcelas"), "ideMemoriaCalculoDae.qtdParcelas")
					.add(Projections.property("valor"), "valor")
					.add(Projections.property("mcd.valorTotal"), "ideMemoriaCalculoDae.valorTotal")
					.add(Projections.property("crf.ideCumprimentoReposicaoFlorestal"), "ideMemoriaCalculoDae.ideCumprimentoReposicaoFlorestal.ideCumprimentoReposicaoFlorestal")
					.add(Projections.property("pca.ideParametroCalculo"), "ideMemoriaCalculoDae.ideParametroCalculo.ideParametroCalculo")
					.add(Projections.property("req.ideRequerimento"), "ideMemoriaCalculoDae.ideCumprimentoReposicaoFlorestal.requerimento.ideRequerimento")
					
				).setResultTransformer(new AliasToNestedBeanResultTransformer(MemoriaCalculoDaeParcela.class));
		
		return memoriaCalculoDaeParcelaIDAO.buscarPorCriteria(criteria);
		
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public MemoriaCalculoDaeParcela obterMemoriaCalculoDaeParcelaPorParcela(Integer ideMemoriaCalculoDae, Integer parcela)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(MemoriaCalculoDaeParcela.class);
		
		criteria.createAlias("ideDae", "dae", JoinType.INNER_JOIN);
		criteria.createAlias("ideMemoriaCalculoDae", "mcd", JoinType.INNER_JOIN);
		criteria.createAlias("mcd.ideCumprimentoReposicaoFlorestal", "crf", JoinType.INNER_JOIN);
		criteria.createAlias("mcd.ideParametroCalculo", "pca", JoinType.INNER_JOIN);
		criteria.createAlias("crf.requerimento", "req", JoinType.INNER_JOIN);
		
		criteria.add(Restrictions.eq("mcd.ideMemoriaCalculoDae", ideMemoriaCalculoDae));
		criteria.add(Restrictions.eq("numParcelaReferencia", parcela));
		
		criteria.setProjection(
				Projections.projectionList()
					.add(Projections.property("ideMemoriaCalculoDaeParcela"), "ideMemoriaCalculoDaeParcela")
					.add(Projections.property("numParcelaReferencia"), "numParcelaReferencia")
					.add(Projections.property("mcd.ideMemoriaCalculoDae"), "ideMemoriaCalculoDae.ideMemoriaCalculoDae")
					.add(Projections.property("mcd.qtdParcelas"), "ideMemoriaCalculoDae.qtdParcelas")
					.add(Projections.property("valor"), "valor")
					.add(Projections.property("mcd.valorTotal"), "ideMemoriaCalculoDae.valorTotal")
					.add(Projections.property("crf.ideCumprimentoReposicaoFlorestal"), "ideMemoriaCalculoDae.ideCumprimentoReposicaoFlorestal.ideCumprimentoReposicaoFlorestal")
					.add(Projections.property("pca.ideParametroCalculo"), "ideMemoriaCalculoDae.ideParametroCalculo.ideParametroCalculo")
					.add(Projections.property("req.ideRequerimento"), "ideMemoriaCalculoDae.ideCumprimentoReposicaoFlorestal.requerimento.ideRequerimento")
					
					.add(Projections.property("dae.ideDae"), "ideDae.ideDae")
					.add(Projections.property("dae.dtVencimento"), "ideDae.dtVencimento")
					
				).setResultTransformer(new AliasToNestedBeanResultTransformer(MemoriaCalculoDaeParcela.class));
		
		return memoriaCalculoDaeParcelaIDAO.buscarPorCriteria(criteria);
		
	}
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public MemoriaCalculoDaeParcela obterMemoriaCalculoDaeParcelaPorDae(Dae dae)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(MemoriaCalculoDaeParcela.class);
		
		criteria.createAlias("ideMemoriaCalculoDae", "mcd", JoinType.INNER_JOIN);
		
		criteria.add(Restrictions.eq("ideDae.ideDae", dae.getIdeDae()));
		
		criteria.setProjection(
				Projections.projectionList()
				.add(Projections.property("numParcelaReferencia"), "numParcelaReferencia")
				.add(Projections.property("mcd.ideMemoriaCalculoDae"), "ideMemoriaCalculoDae.ideMemoriaCalculoDae")
					.add(Projections.property("mcd.qtdParcelas"), "ideMemoriaCalculoDae.qtdParcelas")
					.add(Projections.property("valor"), "valor")
				).setResultTransformer(new AliasToNestedBeanResultTransformer(MemoriaCalculoDaeParcela.class));
		
		return memoriaCalculoDaeParcelaIDAO.buscarPorCriteria(criteria);
		
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<MemoriaCalculoDaeParcela> listarMemoriaCalculoDaeParcelaPorMCD(Integer ideMemoriaCalculoDae)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(MemoriaCalculoDaeParcela.class);
		
		criteria.createAlias("ideMemoriaCalculoDae", "mcd", JoinType.INNER_JOIN);
		criteria.createAlias("ideDae", "dae", JoinType.LEFT_OUTER_JOIN);
		
		criteria.add(Restrictions.eq("mcd.ideMemoriaCalculoDae", ideMemoriaCalculoDae));
		criteria.add(Restrictions.eq("indExcluido", false));
		
		criteria.setProjection(
				Projections.projectionList()
					.add(Projections.property("ideMemoriaCalculoDaeParcela"), "ideMemoriaCalculoDaeParcela")
					.add(Projections.property("mcd.ideMemoriaCalculoDae"), "ideMemoriaCalculoDae.ideMemoriaCalculoDae")
					.add(Projections.property("dae.ideDae"), "ideDae.ideDae")
					.add(Projections.property("dae.dtVencimento"), "ideDae.dtVencimento")
					.add(Projections.property("dae.urlDae"), "ideDae.urlDae")
					.add(Projections.property("valor"), "valor")
					.add(Projections.property("numParcelaReferencia"), "numParcelaReferencia")
					.add(Projections.property("indParcelaUnica"), "indParcelaUnica")
				)
				.addOrder(Order.asc("ideMemoriaCalculoDaeParcela"))
				.setResultTransformer(new AliasToNestedBeanResultTransformer(MemoriaCalculoDaeParcela.class));
		
		
		
		return memoriaCalculoDaeParcelaDAO.listarPorCriteria(criteria);
		
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public MemoriaCalculoDaeParcela obterMemoriaCalculoDaeParcelaPorIdeReposicao(Integer ideCumprimentoReposicaoFlorestal)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(MemoriaCalculoDaeParcela.class);
		
		criteria.createAlias("ideMemoriaCalculoDae", "mcd", JoinType.INNER_JOIN);
		criteria.createAlias("mcd.ideCumprimentoReposicaoFlorestal", "crf", JoinType.INNER_JOIN);
		
		criteria.add(Restrictions.eq("crf.ideCumprimentoReposicaoFlorestal", ideCumprimentoReposicaoFlorestal));
		
		criteria.setProjection(
				Projections.projectionList()
					.add(Projections.property("ideMemoriaCalculoDaeParcela"), "ideMemoriaCalculoDaeParcela")
					.add(Projections.property("numParcelaReferencia"), "numParcelaReferencia")
					.add(Projections.property("mcd.ideMemoriaCalculoDae"), "ideMemoriaCalculoDae.ideMemoriaCalculoDae")
					.add(Projections.property("mcd.qtdParcelas"), "ideMemoriaCalculoDae.qtdParcelas")
					.add(Projections.property("valor"), "valor")
					.add(Projections.property("mcd.valorTotal"), "ideMemoriaCalculoDae.valorTotal")
					.add(Projections.property("crf.ideCumprimentoReposicaoFlorestal"), "ideMemoriaCalculoDae.ideCumprimentoReposicaoFlorestal.ideCumprimentoReposicaoFlorestal")
					
				).setResultTransformer(new AliasToNestedBeanResultTransformer(MemoriaCalculoDaeParcela.class));
		
		return memoriaCalculoDaeParcelaIDAO.buscarPorCriteria(criteria);
		
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<MemoriaCalculoDaeParcela> obterMemoriaCalculoDaeParcelaPorIdeRequerimento(Integer ideRequerimento)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(MemoriaCalculoDaeParcela.class);
		
		criteria.createAlias("ideMemoriaCalculoDae", "mcd", JoinType.INNER_JOIN);
		criteria.createAlias("mcd.ideCumprimentoReposicaoFlorestal", "crf", JoinType.INNER_JOIN);
		criteria.createAlias("crf.requerimento", "r", JoinType.INNER_JOIN);
		
		criteria.add(Restrictions.eq("r.ideRequerimento", ideRequerimento));
		
		criteria.add(Restrictions.eq("indExcluido", Boolean.FALSE));
		
		criteria.setProjection(
				Projections.projectionList()
					.add(Projections.property("ideMemoriaCalculoDaeParcela"), "ideMemoriaCalculoDaeParcela")
					.add(Projections.property("numParcelaReferencia"), "numParcelaReferencia")
					.add(Projections.property("mcd.ideMemoriaCalculoDae"), "ideMemoriaCalculoDae.ideMemoriaCalculoDae")
					.add(Projections.property("mcd.qtdParcelas"), "ideMemoriaCalculoDae.qtdParcelas")
					.add(Projections.property("valor"), "valor")
					.add(Projections.property("mcd.valorTotal"), "ideMemoriaCalculoDae.valorTotal")
					.add(Projections.property("crf.ideCumprimentoReposicaoFlorestal"), "ideMemoriaCalculoDae.ideCumprimentoReposicaoFlorestal.ideCumprimentoReposicaoFlorestal")
					.add(Projections.property("ideDae"), "ideDae")
//					.add(Projections.property("ideDae.sefazCodigoReceita"), "ideDae.sefazCodigoReceita")
					
				).setResultTransformer(new AliasToNestedBeanResultTransformer(MemoriaCalculoDaeParcela.class));
		
		return memoriaCalculoDaeParcelaIDAO.listarPorCriteria(criteria);
		
	}

}
