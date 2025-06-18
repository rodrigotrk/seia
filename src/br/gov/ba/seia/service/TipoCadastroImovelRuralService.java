package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.TipoCadastroImovelRural;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoCadastroImovelRuralService {

	@Inject
	IDAO<TipoCadastroImovelRural> tipoCadastroImovelRuralDAO;

	public List<TipoCadastroImovelRural> listarTipoCadastroImovelRural() {
		return tipoCadastroImovelRuralDAO.listarTodos();
	}

}
