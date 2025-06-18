package br.gov.ba.seia.service;

import java.util.Collection;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.CerhConsultarDaeGeradoDaoImpl;
import br.gov.ba.seia.dto.CerhConsultarDaeDto;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhConsultarDaeGeradoService {

	@Inject
	private CerhConsultarDaeGeradoDaoImpl cerhConsultarDaeGeradoDaoImpl;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<CerhConsultarDaeDto> consultarDaeGerado(Map<String, Object> params)  {
		
		return cerhConsultarDaeGeradoDaoImpl.consultarDaeGerado(params);
	}
}
