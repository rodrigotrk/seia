package br.gov.ba.seia.service;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.TipoModalidadeErbDAOImpl;
import br.gov.ba.seia.entity.TipoModalidadeErb;

/***
 * 
 * @author luis
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoModalidadeErbService {
	
	@Inject
	TipoModalidadeErbDAOImpl tipoModalidadeErbDAOImpl;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<TipoModalidadeErb> listar() throws Exception {
		return this.tipoModalidadeErbDAOImpl.listar();
	}
	
}
