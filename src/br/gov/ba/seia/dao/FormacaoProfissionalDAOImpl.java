package br.gov.ba.seia.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.entity.FormacaoProfissional;

/**
 * {@link IDAO} de {@link FormacaoProfissional}
 * 
 * @author eduardo.fernandes 
 * @since 11/11/2016
 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FormacaoProfissionalDAOImpl {

	@Inject
	private IDAO<FormacaoProfissional> dao;
	
	/**
	 * Método para listar todos as {@link FormacaoProfissional} armazenadas no banco.
	 * 
	 * @author eduardo.fernandes 
	 * @since 11/11/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @return
	 * @throws Exception 
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FormacaoProfissional> listarTodasFormacoesProfissionais(){
		return dao.listarTodos();
	}

}
