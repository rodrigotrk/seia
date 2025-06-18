package br.gov.ba.seia.dao;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.entity.GeoReferenciavel;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class GeoReferenciavelDAOImpl {

	@Inject
	private IDAO<GeoReferenciavel> geoReferenciavelDAO;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarAtualizar(GeoReferenciavel geoReferenciavel)  {
		geoReferenciavelDAO.salvarOuAtualizar(geoReferenciavel);
	}
	
}