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

import br.gov.ba.seia.entity.Tcca;
import br.gov.ba.seia.entity.TccaProjeto;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TccaProjetoDaoImpl {
	
	@Inject
	private IDAO<TccaProjeto> projetoDao;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(TccaProjeto projeto) {
		try {
			projetoDao.salvarOuAtualizar(projeto);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(TccaProjeto projeto) {
		try {
			projeto.setIndExcluido(true);
			projetoDao.atualizar(projeto);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Integer countConsultarProjetosPorTcca(Tcca tcca) throws Exception {
		return projetoDao.count(getCriteria(tcca));
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TccaProjeto> listarPorTcca(Tcca tcca, Integer first, Integer pageSize) throws Exception {
		if(first == null) first = 0;
		if(pageSize == null) pageSize = Integer.MAX_VALUE;
		
		return projetoDao.listarPorCriteriaDemanda(getCriteria(tcca), first.intValue(), pageSize.intValue());
	}

	private DetachedCriteria getCriteria(Tcca tcca) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(TccaProjeto.class, "proj")
			.createAlias("proj.ideTcca", "tcca", JoinType.INNER_JOIN)
			
			.setProjection(Projections.projectionList()
				.add(Projections.property("proj.ideTccaProjeto"), "ideTccaProjeto")
				.add(Projections.property("proj.indCancelado"), "indCancelado")
				.add(Projections.property("proj.indExcluido"), "indExcluido")
				.add(Projections.property("proj.nomProjeto"), "nomProjeto")
				.add(Projections.property("proj.numProjetoResolucao"), "numProjetoResolucao")
				
				.add(Projections.property("tcca.ideTcca"), "ideTcca.ideTcca"))
			.setResultTransformer(new AliasToNestedBeanResultTransformer(TccaProjeto.class))
		
			.add(Restrictions.eq("indExcluido", false));
		
		if(!Util.isNullOuVazio(tcca)) {
			criteria.add(Restrictions.eq("tcca.ideTcca", tcca.getIdeTcca()));
		}
		
		return criteria;
	}
}