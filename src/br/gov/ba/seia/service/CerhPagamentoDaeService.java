package br.gov.ba.seia.service;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.CerhPagamentoDaeDAOImpl;
import br.gov.ba.seia.entity.Dae;
import br.gov.ba.seia.entity.PagamentoDae;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhPagamentoDaeService {

	@Inject
	CerhPagamentoDaeDAOImpl cerhPagamentoDaeDAOImpl;
	
	
	public void salvarCerhPagamentoDae(PagamentoDae cerhPagamentoDae) {
		cerhPagamentoDaeDAOImpl.salvarCerhPagamentoDae(cerhPagamentoDae);
	}
	
	public Dae obterCerhCnae(String nossoNumero) {
		return cerhPagamentoDaeDAOImpl.obterCerhCnae(nossoNumero);
	}
	
	public List<Dae> listarTodosCerhCnae(Date dataInicial, Date dataFinal) {
		return cerhPagamentoDaeDAOImpl.listarCerhCnaeByDataVencimento(dataInicial, dataFinal);
	}
	
}
