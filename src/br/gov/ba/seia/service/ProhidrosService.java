package br.gov.ba.seia.service;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.ProhidrosDAOImpl;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ProhidrosService {
	
	@Inject
	private ProhidrosDAOImpl prohidrosDAOImpl;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Double getIBGEMunicipio(Double longitude, Double latitude, String srid) {
		if(Util.isNullOuVazio(srid)){
			return prohidrosDAOImpl.getIBGEMunicipio(longitude, latitude);
		} else {
			return prohidrosDAOImpl.getIBGEMunicipio(longitude, latitude, srid);
		}
	}
	
}
