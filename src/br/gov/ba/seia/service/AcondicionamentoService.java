package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.AcondicionamentoDAOImpl;
import br.gov.ba.seia.entity.Acondicionamento;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class AcondicionamentoService {
	
	@Inject
	AcondicionamentoDAOImpl acondicionamentoDAOImpl;
	
	public List<Acondicionamento> listarTodos() {
		return this.acondicionamentoDAOImpl.listarTodos();
	}

	
}
