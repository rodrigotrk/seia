package br.gov.ba.seia.service;

import java.util.Collection;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.NaturezaJuridica;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class NaturezaJuridicaService {

	@Inject
	IDAO<NaturezaJuridica> naturezaJuridicaDAO;

	public Collection<NaturezaJuridica> listarTodos() {
		return naturezaJuridicaDAO.listarTodos();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<NaturezaJuridica> listarNaturezaJuridica() {
		return naturezaJuridicaDAO.listarTodos();		
	}
}
