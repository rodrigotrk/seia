package br.gov.ba.seia.facade;



import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import br.gov.ba.seia.entity.HistoricoSuspensaoCadastro;
import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.historico.suspensao.FiltroHistoricoSuspensao;
import br.gov.ba.seia.service.HistoricoSuspensaoCadastroService;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class HistSuspensaoCadastroFacade {
	
		
	@EJB
	private HistoricoSuspensaoCadastroService histSuspensaoCadastroService;	
	
	public void salvarOuAtualizar (HistoricoSuspensaoCadastro objeto) {
			histSuspensaoCadastroService.salvarOuAtualizar(objeto);
	}
	
	public HistoricoSuspensaoCadastro obterPorId(Integer id) {
		return histSuspensaoCadastroService.obterPorId(id);
	}
	
	public void remover(HistoricoSuspensaoCadastro objeto) {
		histSuspensaoCadastroService.remover(objeto);
	}
	
	public HistoricoSuspensaoCadastro carregarTudo(final HistoricoSuspensaoCadastro suspensaoCadastro) {
		return histSuspensaoCadastroService.carregarTudo(suspensaoCadastro);
	}
	
	public PessoaFisica obterPessoaLogada() {
		return histSuspensaoCadastroService.obterPessoaLogada();
	}

	public List<FiltroHistoricoSuspensao> filtrarHistoricoSuspensaoCadastro(Date dataInicio, Date dataFim, ImovelRural ideImovelRural, int first, int pageSize, Integer ...ides)  {
		return histSuspensaoCadastroService.filtrarHistoricoSuspensaoCadastro(dataInicio, dataFim, ideImovelRural, first, pageSize, ides);
	}
	
	public FiltroHistoricoSuspensao visualizarSuspensaoCadastro(ImovelRural ideImovelRural)  {
		List<FiltroHistoricoSuspensao>  lista = histSuspensaoCadastroService.visualizarSuspensaoCadastro(ideImovelRural);
		FiltroHistoricoSuspensao filtro = new FiltroHistoricoSuspensao();
		filtro.setDataSuspensao(lista.get(0).getDataSuspensao());
		filtro.setPessoa(lista.get(0).getPessoa());
		filtro.setNotificacaoOuAuto(lista.get(0).getNotificacaoOuAuto());
		filtro.setObservacao(lista.get(0).getObservacao());
		filtro.setListaMotivos(lista.get(0).getListaMotivos());
		return filtro;
	}
	
	public HistoricoSuspensaoCadastro obterHistoricoSuspensaoCadastroPorIdeImovelRural(ImovelRural imovelRural)  {
		return histSuspensaoCadastroService.obterHistoricoSuspensaoCadastroPorIdeImovelRural(imovelRural);
	}
	
}