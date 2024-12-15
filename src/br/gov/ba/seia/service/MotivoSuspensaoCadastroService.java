package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.MotivoSuspensaoCadastroDAOImpl;
import br.gov.ba.seia.entity.MotivoSuspensaoCadastro;


@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class MotivoSuspensaoCadastroService {

	@Inject
	private MotivoSuspensaoCadastroDAOImpl motivoSuspensaoCadastroDAOImpl;
	
	
	public List<MotivoSuspensaoCadastro> listarTodosMotivos() {
		return motivoSuspensaoCadastroDAOImpl.getMotivoSuspensaoCadastroDAO().listarTodos();
	}
}