package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.GeoRpgaDAOImpl;
import br.gov.ba.seia.entity.GeoRpga;


@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class GeoRpgaService {

	@Inject
	private GeoRpgaDAOImpl geoRpgaDAOImpl;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<GeoRpga> listarRpgaComPrecoPublicoUnitario() {
		
		return geoRpgaDAOImpl.listarRpgaComPrecoPublicoUnitario();
	}
}
