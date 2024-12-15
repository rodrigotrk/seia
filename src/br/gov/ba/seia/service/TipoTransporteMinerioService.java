package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.TipoTransporteMinerioDAOImpl;
import br.gov.ba.seia.entity.FceLicenciamentoMineral;
import br.gov.ba.seia.entity.TipoTransporteMinerio;

/**
 * Service de {@link TipoTransporteMinerio}
 *
 * @author eduardo.fernandes
 * @since 13/06/2016
 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoTransporteMinerioService {

	@Inject
	private TipoTransporteMinerioDAOImpl tipoTransporteMinerioIDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoTransporteMinerio> listarTipoTransporteMinerio() throws Exception {
		return tipoTransporteMinerioIDAO.listarTipoTransporteMinerio();
	}

	/**
	 * Método que vai retornar os {@link TipoTransporteMinerio} que foram
	 * associados ao {@link FceLicenciamentoMineral}.
	 *
	 * @author eduardo.fernandes
	 * @since 11/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param licenciamentoMineral
	 * @return
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoTransporteMinerio> listarTipoTransporteMinerioBy(FceLicenciamentoMineral licenciamentoMineral) throws Exception {
		return tipoTransporteMinerioIDAO.listarTipoTransporteMinerioBy(licenciamentoMineral);

	}
}
