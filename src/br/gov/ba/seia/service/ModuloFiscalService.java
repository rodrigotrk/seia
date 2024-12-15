package br.gov.ba.seia.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.ModuloFiscal;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ModuloFiscalService {

	@Inject
	IDAO<ModuloFiscal> dao;
	
	public List<ModuloFiscal> filtrarByCodIbge(Double pCodIbge) {
		Map<String,Object> paramModuloFiscal = new HashMap<String, Object>();
		paramModuloFiscal.put("codIbgeMunicipio", pCodIbge);
		
		return dao.buscarPorNamedQuery("ModuloFiscal.findByCodIbgeMunicipio", paramModuloFiscal);
	}
}
