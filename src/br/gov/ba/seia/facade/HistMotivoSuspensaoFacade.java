package br.gov.ba.seia.facade;



import java.util.Collection;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import br.gov.ba.seia.entity.HistoricoMotivoSuspensao;
import br.gov.ba.seia.entity.HistoricoSuspensaoCadastro;
import br.gov.ba.seia.entity.MotivoSuspensaoCadastro;
import br.gov.ba.seia.service.HistoricoMotivoSuspensaoService;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class HistMotivoSuspensaoFacade {
	
		
	@EJB
	private HistoricoMotivoSuspensaoService histMotivoSuspensaoService;	
	
	public void salvar(HistoricoSuspensaoCadastro histSuspensaoCadastro, Collection<MotivoSuspensaoCadastro> motivosSelecionados) throws Exception{
		HistoricoMotivoSuspensao objeto;
		for (MotivoSuspensaoCadastro motivo : motivosSelecionados) {
			objeto = new HistoricoMotivoSuspensao(histSuspensaoCadastro.clone(), motivo);
			histMotivoSuspensaoService.salvar(objeto);
		}
	}
	
	public HistoricoMotivoSuspensao obterPorId(Integer id) {
		return histMotivoSuspensaoService.obterPorId(id);
	}
	
	public void remover(HistoricoMotivoSuspensao objeto) {
		histMotivoSuspensaoService.remover(objeto);
	}
	
	public HistoricoMotivoSuspensao carregarTudo(final HistoricoMotivoSuspensao suspensaoCadastro) {
		return histMotivoSuspensaoService.carregarTudo(suspensaoCadastro);
	}

	
}