package br.gov.ba.seia.service;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.VwTipologiaCnaeDAOImpl;
import br.gov.ba.seia.entity.VwTipologiaCnae;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class VwTipologiaCnaeService {
	
	@Inject
	VwTipologiaCnaeDAOImpl vwTipologiaCnaeDAOImpl;
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<VwTipologiaCnae> listarVwTipologiaCnae() throws Exception {
		return vwTipologiaCnaeDAOImpl.getListBuscarQuery();

	}
	

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<VwTipologiaCnae> filtrarListaVwTipologiaCnae(VwTipologiaCnae pVwTipologiaCnae) throws Exception {
		return vwTipologiaCnaeDAOImpl.getVwTipologiaCnaes(pVwTipologiaCnae);
	}


		
	
	
}