package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.MetodoLavraDAOImpl;
import br.gov.ba.seia.entity.FceLicenciamentoMineral;
import br.gov.ba.seia.entity.MetodoLavra;

/**
 * Service de {@link MetodoLavra}
 *
 * @author eduardo.fernandes
 * @since 13/06/2016
 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class MetodoLavraService {

	@Inject
	private MetodoLavraDAOImpl metodoLavraIDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<MetodoLavra> listarMetodoLavra()  {
		return metodoLavraIDAO.listarMetodoLavra();
	}

	/**
	 * Método que vai retornar os {@link MetodoLavra} que foram associados ao
	 * {@link FceLicenciamentoMineral}.
	 *
	 * @author eduardo.fernandes
	 * @since 11/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param licenciamentoMineral
	 * @return
	 * @
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<MetodoLavra> listarMetodoLavraBy(FceLicenciamentoMineral licenciamentoMineral)  {
		return metodoLavraIDAO.listarMetodoLavraBy(licenciamentoMineral);

	}
}
