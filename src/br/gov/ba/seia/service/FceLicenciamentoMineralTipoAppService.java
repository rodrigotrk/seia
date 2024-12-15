package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.FceLicenciamentoMineralTipoAppDAOImpl;
import br.gov.ba.seia.entity.FceLicenciamentoMineral;
import br.gov.ba.seia.entity.FceLicenciamentoMineralTipoApp;
import br.gov.ba.seia.entity.FceLicenciamentoMineralTipoAppPK;

/**
 * @author eduardo.fernandes
 * @since 08/07/2016
 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceLicenciamentoMineralTipoAppService {

	@Inject
	private FceLicenciamentoMineralTipoAppDAOImpl iDAO;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarListaFceLicenciamentoMineralTipoApp(List<FceLicenciamentoMineralTipoApp> lista)  {
		for(FceLicenciamentoMineralTipoApp tipoApp : lista){
			iDAO.salvarFceLicenciamentoMineralTipoApp(tipoApp);
		}
	}

	/**
	 * Método que vai retornar os {@link FceLicenciamentoMineralTipoApp} que
	 * foram associados ao {@link FceLicenciamentoMineral}.
	 *
	 * @author eduardo.fernandes
	 * @since 11/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param fceLicenciamentoMineral
	 * @return
	 * @
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceLicenciamentoMineralTipoApp> listarFceLicenciamentoMineralTipoAppBy(FceLicenciamentoMineral fceLicenciamentoMineral)  {
		List<FceLicenciamentoMineralTipoApp> lista = iDAO.listarFceLicenciamentoMineralTipoAppBy(fceLicenciamentoMineral);
		for(FceLicenciamentoMineralTipoApp fceLicenciamentoMineralTipoApp : lista){
			fceLicenciamentoMineralTipoApp.setIdeFceLicenciamentoMineralTipoAppPK(new FceLicenciamentoMineralTipoAppPK(fceLicenciamentoMineral, fceLicenciamentoMineralTipoApp.getTipoApp()));
		}
		return lista;
	}

	/**
	 *
	 * @author eduardo.fernandes
	 * @since 27/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/">#</a> ADICIONAR TICKET
	 * @param fceLicenciamentoMineral
	 * @
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirListaFceLicenciamentoMineralTipoApp(FceLicenciamentoMineral fceLicenciamentoMineral)  {
		iDAO.excluir(fceLicenciamentoMineral);
	}

}
