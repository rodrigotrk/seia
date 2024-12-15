package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.RelatorioFinanceiroDAOImpl;
import br.gov.ba.seia.enumerator.StatusFinanceiro;


@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class RelatorioFinanceiroService {
	
	@Inject
	RelatorioFinanceiroDAOImpl relatorioFinanceiroDAO;
	
	public List<Object[]> obterRelatorioFinanceiro(String numDocumento, String nomeRequerente, String dataVencimentoInicial, String dataVencimentoFinal, String dataPagamentoInicial, String dataPagamentoFinal, List<StatusFinanceiro> listStatusFinanceiroEnum) {
		
		return relatorioFinanceiroDAO.obterRelatorioFinanceiro(numDocumento, nomeRequerente, dataVencimentoInicial, dataVencimentoFinal,dataPagamentoInicial, dataPagamentoFinal, listStatusFinanceiroEnum);
	}
}
