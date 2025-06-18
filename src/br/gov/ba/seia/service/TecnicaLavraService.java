package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.TecnicaLavraDAOImpl;
import br.gov.ba.seia.entity.FceLicenciamentoMineral;
import br.gov.ba.seia.entity.MetodoLavra;
import br.gov.ba.seia.entity.TecnicaLavra;

/**
 * Service de {@link TecnicaLavra}
 *
 * @author eduardo.fernandes
 * @since 13/06/2016
 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TecnicaLavraService {

	@Inject
	private TecnicaLavraDAOImpl tecnicaLavraIDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TecnicaLavra> listarTecnicaLavraBy(MetodoLavra metodoLavra) throws Exception {
		return tecnicaLavraIDAO.listarTecnicaLavraBy(metodoLavra);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TecnicaLavra> listarTecnicaLavraBy(TecnicaLavra tecnicaLavra) throws Exception {
		return tecnicaLavraIDAO.listarTecnicaLavraBy(tecnicaLavra);
	}

	/**
	 * Método que vai retornar os {@link TecnicaLavra} que foram associados ao
	 * {@link FceLicenciamentoMineral}.
	 *
	 * @author eduardo.fernandes
	 * @since 11/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param licenciamentoMineral
	 * @return
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TecnicaLavra> listarTecnicaLavraBy(FceLicenciamentoMineral licenciamentoMineral) throws Exception {
		return tecnicaLavraIDAO.listarTecnicaLavraBy(licenciamentoMineral);

	}
}