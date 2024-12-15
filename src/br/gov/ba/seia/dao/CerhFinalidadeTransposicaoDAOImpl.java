package br.gov.ba.seia.dao;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.entity.CerhFinalidadeTransposicao;

/**
 * @author eduardo.fernandes 
 * @since 25/03/2017
 * @see <a href="http://10.105.17.77/redmine/issues/8592">#8592</a>
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhFinalidadeTransposicaoDAOImpl {

	@Inject
	private IDAO<CerhFinalidadeTransposicao> dao;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<CerhFinalidadeTransposicao> listarTodos() {
		return dao.listarTodos();
	}
}
