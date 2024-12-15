package br.gov.ba.seia.service;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.WvFormamanejoDAOImpl;
import br.gov.ba.seia.entity.VwFormaManejo;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class VwFormaManejoService {
	
	@Inject
	WvFormamanejoDAOImpl wvFormamanejoDAOImpl;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<VwFormaManejo> listarVwFormaManejo(Integer ideTipologia) throws Exception {
		return wvFormamanejoDAOImpl.listarVwFormaManejo(ideTipologia);
	}
	
}