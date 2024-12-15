package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.CaracteristicaIntervencaoAppDAOImpl;
import br.gov.ba.seia.entity.CaracteristicaIntervencaoApp;

/**
 * Service da classe {@link CaracteristicaIntervencaoApp}.
 * 
 * @author eduardo.fernandes 
 * @since 11/01/2017
 * @see <a href="http://10.105.17.77/redmine/issues/8263">#8263</a>
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CaracteristicaIntervencaoAppService {
	
	@Inject
	private CaracteristicaIntervencaoAppDAOImpl dao;

	/**
	 * Método que retorna uma lista de {@link CaracteristicaIntervencaoApp} com <b>ind_excluido = false</b>
	 * 
	 * @author eduardo.fernandes 
	 * @since 11/01/2017
	 * @see <a href="http://10.105.17.77/redmine/issues/8263">#8263</a>
	 * @return [1 - 'Utilidade pública', 2 - 'Interesse social', 3 - 'Baixo impacto ambiental']
	 * @throws Exception 
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CaracteristicaIntervencaoApp> listarAtivos(){
		return dao.listarAtivos();
	}

}
