package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.CaepogCampo;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CaepogCampoService {
	
	@Inject
	IDAO<CaepogCampo> caepogCampoDAO;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CaepogCampo> listarTodos() {
		return caepogCampoDAO.listarTodos();
	}
}