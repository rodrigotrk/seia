package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.FormaDisposicaoRejeitoDAOImpl;
import br.gov.ba.seia.entity.FceLicenciamentoMineral;
import br.gov.ba.seia.entity.FormaDisposicaoRejeito;

/**
 * @author eduardo.fernandes
 * @since 08/07/2016
 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FormaDisposicaoRejeitoService {

	@Inject
	private FormaDisposicaoRejeitoDAOImpl IDAO;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarListaFormaDisposicaoRejeito(List<FormaDisposicaoRejeito> lista)  {
		for(FormaDisposicaoRejeito formaDisposicaoRejeito : lista){
			IDAO.salvarFormaDisposicaoRejeito(formaDisposicaoRejeito);
		}
	}

	/**
	 * Método que retorna a lista de {@link FormaDisposicaoRejeito} através do
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
	public List<FormaDisposicaoRejeito> listarFormaDisposicaoRejeitoBy(FceLicenciamentoMineral licenciamentoMineral)  {
		return IDAO.listarFormaDisposicaoRejeitoBy(licenciamentoMineral);
	}

	/**
	 *
	 * @author eduardo.fernandes
	 * @since 27/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/">#</a> 
	 * @param fceLicenciamentoMineral
	 * @
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirListaFormaDisposicaoRejeito(FceLicenciamentoMineral fceLicenciamentoMineral)  {
		IDAO.excluir(fceLicenciamentoMineral);
	}
	
	/**
	 *
	 * @author eduardo.fernandes
	 * @since 27/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/">#</a>
	 * @param fceLicenciamentoMineral
	 * @
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFormaDisposicaoRejeito(FormaDisposicaoRejeito disposicaoRejeito)  {
		IDAO.excluir(disposicaoRejeito);
	}

}
