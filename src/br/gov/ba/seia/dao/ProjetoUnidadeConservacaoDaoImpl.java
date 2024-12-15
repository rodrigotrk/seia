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
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.ProjetoUnidadeConservacao;
import br.gov.ba.seia.entity.TccaProjeto;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ProjetoUnidadeConservacaoDaoImpl {
	
	@Inject
	private IDAO<ProjetoUnidadeConservacao> projetoUnidadeConservacaoDao;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(ProjetoUnidadeConservacao projetoUnidadeConservacao) {
		try {
			projetoUnidadeConservacaoDao.salvarOuAtualizar(projetoUnidadeConservacao);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(ProjetoUnidadeConservacao projetoUnidadeConservacao) {
		try {
			projetoUnidadeConservacaoDao.remover(projetoUnidadeConservacao);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ProjetoUnidadeConservacao buscarPorProjetoEUnidadeConservacao(ProjetoUnidadeConservacao puc) {
		try {
			
			DetachedCriteria criteria = DetachedCriteria.forClass(ProjetoUnidadeConservacao.class, "puc");
			
			criteria.add(Restrictions.eq("puc.ideTccaProjeto.ideTccaProjeto", puc.getIdeTccaProjeto().getIdeTccaProjeto()));
			criteria.add(Restrictions.eq("puc.ideUnidadeConservacao.ideUnidadeConservacao", puc.getIdeUnidadeConservacao().getIdeUnidadeConservacao()));
			
			return projetoUnidadeConservacaoDao.buscarPorCriteria(criteria);
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProjetoUnidadeConservacao> listarPorProjeto(TccaProjeto projeto) {
		try {
			
			DetachedCriteria criteria = DetachedCriteria.forClass(ProjetoUnidadeConservacao.class, "puc")
				.createAlias("puc.ideUnidadeConservacao", "uc", JoinType.INNER_JOIN)
				.createAlias("puc.ideTccaProjeto", "projeto", JoinType.INNER_JOIN)
				
				.add(Restrictions.eq("projeto.ideTccaProjeto", projeto.getIdeTccaProjeto()));
			
			return projetoUnidadeConservacaoDao.listarPorCriteria(criteria);
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
}