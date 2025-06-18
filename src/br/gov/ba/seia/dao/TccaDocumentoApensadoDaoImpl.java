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

import br.gov.ba.seia.entity.Tcca;
import br.gov.ba.seia.entity.TccaDocumentoApensado;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TccaDocumentoApensadoDaoImpl {
	
	@Inject
	private IDAO<TccaDocumentoApensado> tccaDocumentoApensadoDao;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(TccaDocumentoApensado tda) {
		try {
			tccaDocumentoApensadoDao.salvarOuAtualizar(tda);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public List<TccaDocumentoApensado> listarPorTCCA(Tcca tcca)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(TccaDocumentoApensado.class, "tda")
				.createAlias("tda.ideTcca", "tcca", JoinType.INNER_JOIN);
		
		ProjectionList projecao = Projections.projectionList()
				.add(Projections.property("tda.ideTccaDocumentoApensado"), "ideTccaDocumentoApensado")
				.add(Projections.property("tda.indExcluido"), "indExcluido")
				.add(Projections.property("tda.urlTccaDocumento"), "urlTccaDocumento")
				.add(Projections.property("tcca.ideTcca"), "ideTcca.ideTcca");
		
		criteria.setProjection(projecao)
				.setResultTransformer(new AliasToNestedBeanResultTransformer(TccaDocumentoApensado.class));
		
		criteria.add(Restrictions.eq("ideTcca.ideTcca", tcca.getIdeTcca()));
		criteria.add(Restrictions.eq("indExcluido", false));
		
		return tccaDocumentoApensadoDao.listarPorCriteria(criteria);
	}
}