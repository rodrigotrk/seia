package br.gov.ba.seia.dao;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.abstracts.AbstractDAO;
import br.gov.ba.seia.entity.TipoUsoRecursoHidrico;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoUsoRecursoHidricoDAOImpl extends AbstractDAO<TipoUsoRecursoHidrico>{
	private static final long serialVersionUID = 1L;

	@Override
	protected IDAO<TipoUsoRecursoHidrico> getDAO() {
		return tipoUsoRecursoHidricoDAO;
	}
	
	@Inject
	private IDAO<TipoUsoRecursoHidrico> tipoUsoRecursoHidricoDAO;
	
}