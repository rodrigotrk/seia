package br.gov.ba.seia.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.entity.CerhSituacaoTipoUso;

/**
 * @author eduardo.fernandes 
 * @since 22/03/2017
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhSituacaoTipoUsoDAO {
	
	@Inject
	private IDAO<CerhSituacaoTipoUso> dao;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CerhSituacaoTipoUso> listarTodos() {
		return dao.listarTodos();
	}
}
