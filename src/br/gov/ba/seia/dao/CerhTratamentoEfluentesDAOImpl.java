package br.gov.ba.seia.dao;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.abstracts.AbstractDAO;
import br.gov.ba.seia.entity.CerhTratamentoEfluente;

/**
 * @author Alexandre Queiroz
 * @since 27/03/2017
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhTratamentoEfluentesDAOImpl extends AbstractDAO<CerhTratamentoEfluente>{
	private static final long serialVersionUID = 1L;

	@Override
	protected IDAO<CerhTratamentoEfluente> getDAO() {
		return dao;
	}
	
	@Inject
	private IDAO<CerhTratamentoEfluente> dao;
	
}