package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.TipoCombustivel;


@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoCombustivelService {

	@Inject
	private IDAO<TipoCombustivel> tipoCombustivelDAO;
	

	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoCombustivel> listarTodos() {
		return tipoCombustivelDAO.listarTodos();
	}
	
	
}
