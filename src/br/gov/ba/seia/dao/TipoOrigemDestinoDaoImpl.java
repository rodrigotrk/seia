package br.gov.ba.seia.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.entity.TipoOrigemDestino;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoOrigemDestinoDaoImpl {
	
	@Inject
	private IDAO<TipoOrigemDestino> tipoOrigemDestinoDao;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoOrigemDestino> listarTodos() {
		return tipoOrigemDestinoDao.listarTodos();
	}
}