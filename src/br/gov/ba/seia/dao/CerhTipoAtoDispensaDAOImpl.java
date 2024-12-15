package br.gov.ba.seia.dao;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.abstracts.AbstractDAO;
import br.gov.ba.seia.entity.CerhTipoAtoDispensa;


@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhTipoAtoDispensaDAOImpl extends AbstractDAO<CerhTipoAtoDispensa>{
	private static final long serialVersionUID = 1L;

	@Inject
	private IDAO<CerhTipoAtoDispensa> cerhTipoAtoDispensaDAO;
	
	@Override
	protected IDAO<CerhTipoAtoDispensa> getDAO() {
		return cerhTipoAtoDispensaDAO;
	}
}