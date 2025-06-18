package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.MetodoRecuperacaoIntervencaoDAOImpl;
import br.gov.ba.seia.entity.FceLicenciamentoMineral;
import br.gov.ba.seia.entity.MetodoRecuperacaoIntervencao;

/**
 * Service de {@link MetodoRecuperacaoIntervencao}
 *
 * @author eduardo.fernandes
 * @since 13/06/2016
 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class MetodoRecuperacaoIntervencaoService {

	@Inject
	private MetodoRecuperacaoIntervencaoDAOImpl metodoRecuperacaoIntervencaoIDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<MetodoRecuperacaoIntervencao> listarMetodoRecuperacaoIntervencao()  {
		return metodoRecuperacaoIntervencaoIDAO.listarMetodoRecuperacaoIntervencao();
	}

	/**
	 * Método que vai retornar os {@link MetodoRecuperacaoIntervencao} que foram
	 * associados ao {@link FceLicenciamentoMineral}.
	 *
	 * @author eduardo.fernandes
	 * @since 11/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param licenciamentoMineral
	 * @return
	 * @
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<MetodoRecuperacaoIntervencao> listarMetodoRecuperacaoIntervencaoBy(FceLicenciamentoMineral licenciamentoMineral)  {
		return metodoRecuperacaoIntervencaoIDAO.listarMetodoRecuperacaoIntervencaoBy(licenciamentoMineral);
	}
}
