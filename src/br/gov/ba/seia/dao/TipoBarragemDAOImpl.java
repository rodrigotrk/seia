package br.gov.ba.seia.dao;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.abstracts.AbstractDAO;
import br.gov.ba.seia.entity.TipoBarragem;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoBarragemDAOImpl extends AbstractDAO<TipoBarragem>{
	private static final long serialVersionUID = 1L;

	@Inject
	private IDAO<TipoBarragem> dao;
	
	@Override
	protected IDAO<TipoBarragem> getDAO() {
		return dao;
	}

}
