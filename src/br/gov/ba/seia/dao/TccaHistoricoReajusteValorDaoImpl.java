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

import br.gov.ba.seia.entity.Tcca;
import br.gov.ba.seia.entity.TccaHistoricoReajusteValor;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TccaHistoricoReajusteValorDaoImpl {
	
	@Inject
	private IDAO<TccaHistoricoReajusteValor> tccaHistoricoReajusteValorDao;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(TccaHistoricoReajusteValor tda) {
		try {
			tccaHistoricoReajusteValorDao.salvarOuAtualizar(tda);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TccaHistoricoReajusteValor> listarPorTcca(Tcca tcca) {
		try{
			DetachedCriteria criteria = DetachedCriteria.forClass(TccaHistoricoReajusteValor.class, "thrv")
				.createAlias("thrv.ideTcca", "tcca", JoinType.INNER_JOIN)
				
				.setProjection(Projections.projectionList()
					.add(Projections.property("thrv.ideTccaHistoricoReajusteValor"), "ideTccaHistoricoReajusteValor")
					.add(Projections.property("thrv.dtcReajuste"), "dtcReajuste")
					.add(Projections.property("thrv.indiceReajuste"), "indiceReajuste")
					.add(Projections.property("thrv.valTccaAnterior"), "valTccaAnterior")
					.add(Projections.property("thrv.valReajuste"), "valReajuste")
					.add(Projections.property("thrv.valTcca"), "valTcca")
					
					.add(Projections.property("tcca.ideTcca"), "ideTcca.ideTcca")
					.add(Projections.property("tcca.numTcca"), "ideTcca.numTcca")
					
				).setResultTransformer(new AliasToNestedBeanResultTransformer(TccaHistoricoReajusteValor.class))
						
				.add(Restrictions.eq("tcca.ideTcca", tcca.getIdeTcca()));
			
			return tccaHistoricoReajusteValorDao.listarPorCriteria(criteria, Order.desc("ideTccaHistoricoReajusteValor"));
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
}
