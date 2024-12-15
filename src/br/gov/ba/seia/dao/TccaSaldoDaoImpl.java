package br.gov.ba.seia.dao;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.apache.log4j.Level;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.Tcca;
import br.gov.ba.seia.entity.TccaSaldo;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TccaSaldoDaoImpl {
	
	@Inject
	private IDAO<TccaSaldo> tccaSaldoDao;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(TccaSaldo tccaSaldo) {
		try {
			tccaSaldoDao.salvarOuAtualizar(tccaSaldo);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public TccaSaldo buscarUltimoPorTcca(Tcca tcca)  {
		try {
			DetachedCriteria criteria = DetachedCriteria.forClass(TccaSaldo.class)
				.createAlias("ideTcca", "ideTcca" , JoinType.INNER_JOIN)
				.createAlias("ideMovimentacaoFinanceira", "movF" , JoinType.INNER_JOIN)
				
				.add(Property.forName("ideTccaSaldo").eq(
					DetachedCriteria.forClass(TccaSaldo.class)
					.createAlias("ideTcca", "ideTcca" , JoinType.INNER_JOIN)
					.createAlias("ideMovimentacaoFinanceira", "movF" , JoinType.INNER_JOIN)
					
					.setProjection(Projections.projectionList().add(Projections.max("ideTccaSaldo"), "ideTccaSaldo"))
					.add(Restrictions.eq("ideTcca.ideTcca", tcca.getIdeTcca()))
				))
				
				.setProjection(Projections.projectionList()
					.add(Projections.property("ideTccaSaldo"), "ideTccaSaldo")
					.add(Projections.property("valSaldoNaoDestinadoProjeto"), "valSaldoNaoDestinadoProjeto")
					.add(Projections.property("valSaldoSuplementado"), "valSaldoSuplementado")
					.add(Projections.property("ideTcca"), "ideTcca")
					
					.add(Projections.property("movF.ideMovimentacaoFinanceira"), "ideMovimentacaoFinanceira.ideMovimentacaoFinanceira"))
					
				.setResultTransformer(new AliasToNestedBeanResultTransformer(TccaSaldo.class));
			
			return tccaSaldoDao.buscarPorCriteria(criteria);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
}