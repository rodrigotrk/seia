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
import br.gov.ba.seia.entity.TccaProjeto;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ProjetoAcaoDaoImpl {
	
	@Inject
	private IDAO<ProjetoAcao> projetoAcaoDao;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(ProjetoAcao projetoAcao) {
		try {
			projetoAcaoDao.salvarOuAtualizar(projetoAcao);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(List<ProjetoAcao> projetoAcao) {
		try {
			projetoAcaoDao.salvarEmLote(projetoAcao);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(ProjetoAcao projetoAcao) {
		try {
			projetoAcaoDao.remover(projetoAcao);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ProjetoAcao buscarPorProjetoEAcao(ProjetoAcao pa) {
		try {
			
			DetachedCriteria criteria = DetachedCriteria.forClass(ProjetoAcao.class, "pa");
			
			if(pa.getIdeProjetoAcao() != null) {
				criteria.add(Restrictions.eq("pa.ideProjetoAcao", pa.getIdeProjetoAcao()));
			} else {
				criteria.add(Restrictions.eq("pa.ideTccaProjeto.ideTccaProjeto", pa.getIdeTccaProjeto().getIdeTccaProjeto()));
				criteria.add(Restrictions.like("pa.nomAcao", pa.getNomAcao(), MatchMode.EXACT));
			}
			
			return projetoAcaoDao.buscarPorCriteria(criteria);
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProjetoAcao> listarPorProjeto(TccaProjeto projeto) {
		
		try {
			DetachedCriteria criteria = DetachedCriteria.forClass(ProjetoAcao.class, "pa")
			
				.createAlias("pa.ideTccaProjeto", "projeto", JoinType.INNER_JOIN)
				.createAlias("projeto.ideTcca", "tcca", JoinType.INNER_JOIN)
				
				.add(Restrictions.eq("projeto.ideTccaProjeto", projeto.getIdeTccaProjeto()))
				.add(Restrictions.eq("indExcluido", false));
		
			return projetoAcaoDao.listarPorCriteria(criteria);
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
		
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProjetoAcao> listarPorProjetoComProduto(TccaProjeto projeto) {
		
		try {
			
			DetachedCriteria criteria = DetachedCriteria.forClass(ProjetoAcao.class)
				
				.createAlias("ideTccaProjeto", "ideTccaProjeto", JoinType.INNER_JOIN)
				.setProjection(
					Projections.projectionList()
						.add(Projections.property("ideProjetoAcao"),"ideProjetoAcao")
						.add(Projections.property("indExcluido"),"indExcluido")
						.add(Projections.property("nomAcao"),"nomAcao")
						.add(Projections.property("ideTccaProjeto"),"ideTccaProjeto")
						.add(Projections.property("ideTccaProjeto.ideTccaProjeto"),"ideTccaProjeto.ideTccaProjeto")
						.add(Projections.property("ideTccaProjeto.indCancelado"),"ideTccaProjeto.indCancelado")
						.add(Projections.property("ideTccaProjeto.indExcluido"),"ideTccaProjeto.indExcluido")
						.add(Projections.property("ideTccaProjeto.nomProjeto"),"ideTccaProjeto.nomProjeto")
						.add(Projections.property("ideTccaProjeto.numProjetoResolucao"),"ideTccaProjeto.numProjetoResolucao")
						.add(Projections.property("ideTccaProjeto.ideTcca"),"ideTccaProjeto.ideTcca")						)	
				
				.add(Restrictions.eq("ideTccaProjeto.ideTccaProjeto", projeto.getIdeTccaProjeto()))
				.add(Restrictions.eq("indExcluido", false))
				
				.setResultTransformer(new AliasToNestedBeanResultTransformer(ProjetoAcao.class));
			
			return projetoAcaoDao.listarPorCriteria(criteria);
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProjetoAcao> listarTccaProjetoBy(TccaProjeto tccaProjeto) {
		try {
			DetachedCriteria criteria = DetachedCriteria.forClass(ProjetoAcao.class)
				.createAlias("projetoAcaoProdutoCollection", "projetoAcaoProdutoCollection");
			return projetoAcaoDao.listarPorCriteria(criteria);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Integer countProjetoAcaoBy(TccaProjeto tccaProjeto) {
		return null;
	}
}