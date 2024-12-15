package br.gov.ba.seia.dao;

import java.util.List;
import java.util.Properties;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.AtividadeIntervencaoApp;
import br.gov.ba.seia.entity.CaracteristicaAtividadeIntervencaoApp;
import br.gov.ba.seia.entity.CaracteristicaIntervencaoApp;
import br.gov.ba.seia.entity.DeclaracaoIntervencaoApp;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

/**
 * @author eduardo.fernandes 
 * @since 11/01/2017
 * @see <a href="http://10.105.17.77/redmine/issues/8263">#8263</a>
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CaracteristicaAtividadeIntervencaoAppDAOImpl {

	@Inject
	private IDAO<CaracteristicaAtividadeIntervencaoApp> dao;

	/**
	 * Método que retorna a {@link CaracteristicaAtividadeIntervencaoApp} de acordo com os parâmetros passados.
	 * 
	 * @author eduardo.fernandes 
	 * @since 13/01/2017
	 * @see <a href="http://10.105.17.77/redmine/issues/8263">#8263</a>
	 * @param caracteristicaIntervencaoApp
	 * @param atividadeIntervencaoApp
	 * @return
	 * @throws Exception 
	 */
	public CaracteristicaAtividadeIntervencaoApp buscar(CaracteristicaIntervencaoApp caracteristicaIntervencaoApp, AtividadeIntervencaoApp atividadeIntervencaoApp) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CaracteristicaAtividadeIntervencaoApp.class)
				.add(Restrictions.eq("caracteristicaIntervencaoApp", caracteristicaIntervencaoApp))
				.add(Restrictions.eq("atividadeIntervencaoApp", atividadeIntervencaoApp))
				;
		return dao.buscarPorCriteria(criteria);
	}
	
}
