package br.gov.ba.seia.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.AtividadeIntervencaoApp;
import br.gov.ba.seia.entity.CaracteristicaAtividadeIntervencaoApp;
import br.gov.ba.seia.entity.CaracteristicaIntervencaoApp;
import br.gov.ba.seia.entity.DeclaracaoIntervencaoApp;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

/**
 * DAO da classe {@link AtividadeIntervencaoApp}.
 * 
 * @author eduardo.fernandes 
 * @since 11/01/2017
 * @see <a href="http://10.105.17.77/redmine/issues/8263">#8263</a>
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class AtividadeIntervencaoAppDAOImpl {

	@Inject
	private IDAO<AtividadeIntervencaoApp> dao;

	/**
	 * Método que retorna uma lista de {@link AtividadeIntervencaoApp} com <b>ind_excluido = false</b> associados à {@link CaracteristicaIntervencaoApp}.
	 * 
	 * @author eduardo.fernandes 
	 * @since 11/01/2017
	 * @see <a href="http://10.105.17.77/redmine/issues/8263">#8263</a>
	 * @return
	 * @throws Exception 
	 */
	public List<AtividadeIntervencaoApp> listarBy(CaracteristicaIntervencaoApp caracteristicaIntervencaoApp) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CaracteristicaAtividadeIntervencaoApp.class)
				.createAlias("atividadeIntervencaoApp", "at")
				.createAlias("caracteristicaIntervencaoApp", "ca")
				.add(Restrictions.eq("ca.ideCaracteristicaIntervencaoApp", caracteristicaIntervencaoApp.getIdeCaracteristicaIntervencaoApp()))
				.add(Restrictions.eq("at.indExcluido", false))
				.setProjection(Projections.projectionList()
						.add(Projections.property("at.ideAtividadeIntervencaoApp"), "ideAtividadeIntervencaoApp")
						.add(Projections.property("at.dtcCriacao"), "dtcCriacao")
						.add(Projections.property("at.desAtividadeIntervencaoApp"), "desAtividadeIntervencaoApp")
						).setResultTransformer(new AliasToNestedBeanResultTransformer(AtividadeIntervencaoApp.class))
				;
		return dao.listarPorCriteria(criteria , Order.asc("desAtividadeIntervencaoApp"));
	}
	
	public List <AtividadeIntervencaoApp> listarAtividadePor(Requerimento requerimento) {
		DetachedCriteria criteria = DetachedCriteria.forClass(DeclaracaoIntervencaoApp.class)
				
				
				.createAlias("ideAtoDeclaratorio", "ad", JoinType.INNER_JOIN)
				.createAlias("declaracaoIntervencaoAppCaracteristcas", "diac", JoinType.INNER_JOIN)
				.createAlias("diac.caracteristicaAtividadeIntervencaoApp", "caia", JoinType.INNER_JOIN)
				.createAlias("caia.atividadeIntervencaoApp", "aia", JoinType.INNER_JOIN)
				.createAlias("caia.caracteristicaIntervencaoApp", "cia", JoinType.INNER_JOIN)
				.createAlias("ad.ideRequerimento", "r", JoinType.INNER_JOIN)				
				
				.add(Restrictions.eq("r.ideRequerimento", requerimento.getIdeRequerimento()))
				
				.setProjection(Projections.projectionList()
					.add(Projections.property("aia.ideAtividadeIntervencaoApp"),"ideAtividadeIntervencaoApp")
					.add(Projections.property("aia.desAtividadeIntervencaoApp"),"desAtividadeIntervencaoApp")
				)
				
				.setResultTransformer(new AliasToNestedBeanResultTransformer(AtividadeIntervencaoApp.class))
				
				;
		return dao.listarPorCriteria(criteria);
	}
	
}
