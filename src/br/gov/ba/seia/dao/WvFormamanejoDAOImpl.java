package br.gov.ba.seia.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import br.gov.ba.seia.entity.VwFormaManejo;



public class WvFormamanejoDAOImpl {
	
	@Inject
	IDAO<VwFormaManejo> vwFormaManejoDAO;	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<VwFormaManejo> listarVwFormaManejo(Integer ideTipologia) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideTipologia", ideTipologia);
		return vwFormaManejoDAO.buscarPorNamedQuery("VwFormaManejo.findByIdeTipologia",params);
	}
	

}