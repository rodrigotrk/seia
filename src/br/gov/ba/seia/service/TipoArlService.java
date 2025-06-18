package br.gov.ba.seia.service;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.TipoArl;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoArlService {
	
	
	@Inject
	IDAO<TipoArl> tipoArlServiceDAO;
	
	
	public Collection<TipoArl> listarTipoArls() {
		return tipoArlServiceDAO.listarTodos();	
	}

}
