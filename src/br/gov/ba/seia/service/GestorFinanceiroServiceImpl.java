package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.GestorFinanceiroCefir;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class GestorFinanceiroServiceImpl implements GestorFinanceiroCefirService {

	@Inject
	private IDAO<GestorFinanceiroCefir> gestorFinanceiroDAO;
	
	@Override
	public List<GestorFinanceiroCefir> listGestorFinanceiro() {
		return gestorFinanceiroDAO.listarTodos();
	}

}
