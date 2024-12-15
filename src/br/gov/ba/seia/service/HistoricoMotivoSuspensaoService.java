package br.gov.ba.seia.service;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.HistoricoMotivoSuspensaoDAOImpl;
import br.gov.ba.seia.entity.HistoricoMotivoSuspensao;


@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class HistoricoMotivoSuspensaoService {

	@Inject
	private HistoricoMotivoSuspensaoDAOImpl histMotivoSuspensaoDAOImpl;
	
	public void salvar(HistoricoMotivoSuspensao objeto) {
		histMotivoSuspensaoDAOImpl.getHistMotivoSuspensaoDAO().salvar(objeto);
	}
	
	public HistoricoMotivoSuspensao obterPorId(Integer id){
		return histMotivoSuspensaoDAOImpl.getHistMotivoSuspensaoDAO().carregarGet(id);
	}
	
	public void remover(HistoricoMotivoSuspensao objeto) {
		histMotivoSuspensaoDAOImpl.getHistMotivoSuspensaoDAO().remover(objeto);
	}
	
	public HistoricoMotivoSuspensao carregarTudo(final HistoricoMotivoSuspensao suspensaoCadastro) {
		return histMotivoSuspensaoDAOImpl.carregarTudo(suspensaoCadastro);
	}
	
}