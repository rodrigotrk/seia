package br.gov.ba.seia.service;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.CerhTipoPrestadorServico;

/**
 * @author eduardo.fernandes 
 * @since 29/03/2017
 * @see <a href="http://10.105.17.77/redmine/issues/8592">#8592</a>
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhTipoPrestadorServicoService {

	@Inject
	private IDAO<CerhTipoPrestadorServico> dao;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<CerhTipoPrestadorServico> listarTodos() {
		return dao.listarTodos();
	}
}
