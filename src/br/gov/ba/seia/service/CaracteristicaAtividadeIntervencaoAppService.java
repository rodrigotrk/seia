package br.gov.ba.seia.service;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.CaracteristicaAtividadeIntervencaoAppDAOImpl;
import br.gov.ba.seia.entity.AtividadeIntervencaoApp;
import br.gov.ba.seia.entity.CaracteristicaAtividadeIntervencaoApp;
import br.gov.ba.seia.entity.CaracteristicaIntervencaoApp;

/**
 * @author eduardo.fernandes 
 * @since 11/01/2017
 * @see <a href="http://10.105.17.77/redmine/issues/8263">#8263</a>
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CaracteristicaAtividadeIntervencaoAppService {
	
	@Inject
	private CaracteristicaAtividadeIntervencaoAppDAOImpl dao;

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
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CaracteristicaAtividadeIntervencaoApp buscar(CaracteristicaIntervencaoApp caracteristicaIntervencaoApp, AtividadeIntervencaoApp atividadeIntervencaoApp) {
		return dao.buscar(caracteristicaIntervencaoApp, atividadeIntervencaoApp);
	}
}

