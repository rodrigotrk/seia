package br.gov.ba.seia.service;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.PagamentoDaeDAOImpl;
import br.gov.ba.seia.entity.Dae;
import br.gov.ba.seia.entity.PagamentoDae;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PagamentoDaeService {

	@Inject
	PagamentoDaeDAOImpl pagamentoDaeDAOImpl;
	
	
	public void salvarPagamentoDae(PagamentoDae cerhPagamentoDae) {
		pagamentoDaeDAOImpl.salvarPagamentoDae(cerhPagamentoDae);
	}
	
	public Dae obterDae(String nossoNumero) {
		return pagamentoDaeDAOImpl.obterDae(nossoNumero);
	}
	
	public List<Dae> listarTodosDaesDoPeriodo(Date dataFinal) {
		return pagamentoDaeDAOImpl.listarDaeByDataVencimento(dataFinal);
	}
	
}
