package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.Finalidade;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FinalidadeService {

	@Inject
	IDAO<Finalidade> daoFinalidade;
	
	public List<Finalidade> listarFinalidade() {
		return daoFinalidade.listarTodos();
	}
}
