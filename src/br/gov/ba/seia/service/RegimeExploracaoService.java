package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.RegimeExploracaoDAOImpl;
import br.gov.ba.seia.entity.FceLicenciamentoMineral;
import br.gov.ba.seia.entity.RegimeExploracao;

/**
 * Service de {@link RegimeExploracao}
 *
 * @author eduardo.fernandes
 * @since 10/06/2016
 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class RegimeExploracaoService {

	@Inject
	private RegimeExploracaoDAOImpl regimeExploracaoIDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<RegimeExploracao> listarRegimeExploracao()  {
		return regimeExploracaoIDAO.listarRegimeExploracao();
	}

	/**
	 * Método que vai retornar os {@link RegimeExploracao} que foram associados
	 * ao {@link FceLicenciamentoMineral}.
	 *
	 * @author eduardo.fernandes
	 * @since 11/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param fceLicenciamentoMineral
	 * @return
	 * @
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<RegimeExploracao> listarRegimeExploracao(FceLicenciamentoMineral fceLicenciamentoMineral)  {
		return regimeExploracaoIDAO.listarRegimeExploracao(fceLicenciamentoMineral);

	}

}
