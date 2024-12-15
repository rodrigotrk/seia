package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.OutorgaMineracaoDAOImpl;
import br.gov.ba.seia.entity.FceLicenciamentoMineral;
import br.gov.ba.seia.entity.OutorgaMineracao;

/**
 * @author eduardo.fernandes, alexandre.queiroz
 * @since 15/07/2016
 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class OutorgaMineracaoService {

	@Inject
	private OutorgaMineracaoDAOImpl outorgaMineracaoDAO;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarListaOutorgaMineracao(List<OutorgaMineracao> lista) throws Exception {
		outorgaMineracaoDAO.salvarListaOutorgaMineracao(lista);
	}

	/**
	 * ADICIONAR COMENTÁRIO
	 *
	 * @author eduardo.fernandes
	 * @since 15/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param object
	 * @return
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<OutorgaMineracao> listarOutorgaMineracaoBy(Object object) throws Exception {
		List<OutorgaMineracao> lista = outorgaMineracaoDAO.listarOutorgaMineracaoBy(object);
		for(OutorgaMineracao outorgaMineracao : lista){
			outorgaMineracao.setEdicao();
			outorgaMineracao.setConfirmado(true);
		}
		return lista;
	}

}
