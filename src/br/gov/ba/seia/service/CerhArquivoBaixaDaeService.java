package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.CerhArquivoBaixaDaeDAOImpl;
import br.gov.ba.seia.entity.ArquivoBaixaDae;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhArquivoBaixaDaeService {

	@Inject
	CerhArquivoBaixaDaeDAOImpl cerhArquivoBaixaDaeDAOImpl;

	public void salvarArquivoBaixa(ArquivoBaixaDae cerhArquivoBaixaDae) {
		cerhArquivoBaixaDaeDAOImpl.salvarProcessodeBaixa(cerhArquivoBaixaDae);
	}
	
	public void atualizarArquivoBaixa(ArquivoBaixaDae cerhArquivoBaixaDae) {
		cerhArquivoBaixaDaeDAOImpl.atualizarProcessoBaixa(cerhArquivoBaixaDae);
	}

	public List<ArquivoBaixaDae> carregarArquivosBaixaDae()  {
		return cerhArquivoBaixaDaeDAOImpl.carregarArquivosBaixaDae();
	}
	
}
