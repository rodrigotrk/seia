package br.gov.ba.seia.service;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.TipoCanalErbDAOImpl;
import br.gov.ba.seia.entity.TipoCanalErb;

/***
 * 
 * @author luis
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoCanalErbService {
	
	@Inject
	TipoCanalErbDAOImpl tipoCanalErbDAOImpl;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<TipoCanalErb> listar() throws Exception {
		return tipoCanalErbDAOImpl.listar();
	}
	
}
