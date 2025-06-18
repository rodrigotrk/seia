/**
 * 		02/04/14
 * @author eduardo.fernandes
 */
package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.CaracteristicaEfluente;
import br.gov.ba.seia.enumerator.CaracteristicaEfluenteEnum;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CaracteristicaEfluenteService {

	@Inject
	private IDAO<CaracteristicaEfluente> caracteristicaEfluenteIDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CaracteristicaEfluente> listarCaracteristicaEfluenteSemFosforo() {
		DetachedCriteria criteria = DetachedCriteria.forClass(CaracteristicaEfluente.class);
		List<CaracteristicaEfluente> lista = caracteristicaEfluenteIDAO.listarPorCriteria(criteria, Order.asc("ideCaracteristicaEfluente"));
		lista.remove(new CaracteristicaEfluente(CaracteristicaEfluenteEnum.FOSFORO.getId()));
		return lista;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CaracteristicaEfluente> listarTodosCaracteristicaEfluente() {
		return caracteristicaEfluenteIDAO.listarTodos();
	}
}