package br.gov.ba.seia.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.entity.CadastroAtividadeNaoSujeitaLicTipoStatus;

/**
 * 
 * @author eduardo.fernandes 
 * @since 07/11/2016
 * @see <a href="http://10.105.17.77/redmine/issues/8187">#8187</a>
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CadastroAtividadeNaoSujeitaLicTipoStatusDAOImpl {

	@Inject
	private IDAO<CadastroAtividadeNaoSujeitaLicTipoStatus> dao;
	
	/**
	 * 
	 * Método que vai retornar todos os {@link CadastroAtividadeNaoSujeitaLicTipoStatus} inseridos no banco. 
	 * 
	 * @author eduardo.fernandes 
	 * @since 07/11/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8187">#8187</a>
	 * @return
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CadastroAtividadeNaoSujeitaLicTipoStatus> listarTodosCadastrosAtividadesNaoSujeitaLicTipoStatus() {
		return dao.listarTodos();
	}
}
