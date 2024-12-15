package br.gov.ba.seia.dao;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.entity.CerhStatus;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhTipoStatusDAOImpl {

	@Inject
	private IDAO<CerhStatus> cerhTipoStatusDaoImplementacao;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<CerhStatus> listaCerhTipoStatus() {
		return cerhTipoStatusDaoImplementacao.listarTodos();
	}
}