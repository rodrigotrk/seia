package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.FceLicenciamentoMineralProducaoProdutoDAOImpl;
import br.gov.ba.seia.entity.FceLicenciamentoMineral;
import br.gov.ba.seia.entity.FceLicenciamentoMineralProducaoProduto;
import br.gov.ba.seia.entity.FceLicenciamentoMineralProducaoProdutoPK;

/**
 * @author eduardo.fernandes
 * @since 08/07/2016
 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceLicenciamentoMineralProducaoProdutoService {

	@Inject
	private FceLicenciamentoMineralProducaoProdutoDAOImpl iDAO;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarListaFceLicenciamentoMineralProducaoProduto(List<FceLicenciamentoMineralProducaoProduto> lista)  {
		iDAO.salvarListaFceLicenciamentoMineralProducaoProduto(lista);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceLicenciamentoMineralProducaoProduto> listarFceLicenciamentoMineralProducaoProdutoBy(FceLicenciamentoMineral fceLicenciamentoMineral)  {
		List<FceLicenciamentoMineralProducaoProduto> lista = iDAO.listarFceLicenciamentoMineralProducaoProdutoBy(fceLicenciamentoMineral);
		for(FceLicenciamentoMineralProducaoProduto produto : lista){
			produto.setEdicao();
			produto.setConfirmado(true);
			produto.setIdeFceLicenciamentoMineralProducaoProdutoPK(new FceLicenciamentoMineralProducaoProdutoPK(fceLicenciamentoMineral, produto.getProducaoProduto()));
		}
		return lista;
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
	public void excluirListaFceLicenciamentoMineralProducaoProduto(FceLicenciamentoMineral fceLicenciamentoMineral)  {
		iDAO.excluir(fceLicenciamentoMineral);
	}
}
