package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.FormacaoProfissionalDAOImpl;
import br.gov.ba.seia.entity.FormacaoProfissional;

/**
 * Serviço da classe {@link FormacaoProfissional}.
 * 
 * @author eduardo.fernandes 
 * @since 11/11/2016
 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FormacaoProfissionalService {

	@Inject
	private FormacaoProfissionalDAOImpl dao;
	
	/**
	 * Método que lista todas as {@link FormacaoProfissional}.
	 * 
	 * @author eduardo.fernandes 
	 * @since 11/11/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @return
	 * @
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FormacaoProfissional> listarFormacaoProfissional() {
		return dao.listarTodasFormacoesProfissionais();
	}
}
