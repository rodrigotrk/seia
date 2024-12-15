package br.gov.ba.seia.dao;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.abstracts.AbstractDAO;
import br.gov.ba.seia.entity.TipoCorpoHidrico;

/**
 * @author eduardo.fernandes 
 * @since 18/04/2017
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoCorpoHidricoDAOImpl extends AbstractDAO<TipoCorpoHidrico> {
	private static final long serialVersionUID = 1L;

	@Inject
	private IDAO<TipoCorpoHidrico> dao;
	
	@Override
	protected IDAO<TipoCorpoHidrico> getDAO() {
		return dao;
	}
}
