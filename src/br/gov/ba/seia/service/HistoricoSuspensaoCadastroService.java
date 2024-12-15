package br.gov.ba.seia.service;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.HistoricoSuspensaoCadastroDAOImpl;
import br.gov.ba.seia.entity.HistoricoSuspensaoCadastro;
import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.historico.suspensao.FiltroHistoricoSuspensao;
import br.gov.ba.seia.util.auditoria.AuditoriaUtil;


@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class HistoricoSuspensaoCadastroService {

	@Inject
	private HistoricoSuspensaoCadastroDAOImpl histSuspensaoCadastroDAOImpl;
	
	@Inject
	private UsuarioService usuarioService;
	
	public void salvarOuAtualizar (HistoricoSuspensaoCadastro objeto) {
		histSuspensaoCadastroDAOImpl.getHistSuspensaoCadastroDAO().salvarOuAtualizar(objeto);
	}
	
	public HistoricoSuspensaoCadastro obterPorId(Integer id){
		return histSuspensaoCadastroDAOImpl.getHistSuspensaoCadastroDAO().carregarGet(id);
	}
	
	public void remover(HistoricoSuspensaoCadastro objeto) {
		histSuspensaoCadastroDAOImpl.getHistSuspensaoCadastroDAO().remover(objeto);
	}
	
	public HistoricoSuspensaoCadastro carregarTudo(final HistoricoSuspensaoCadastro suspensaoCadastro) {
		return histSuspensaoCadastroDAOImpl.carregarTudo(suspensaoCadastro);
	}
	
	public PessoaFisica obterPessoaLogada() {
		Usuario user = usuarioService.carregar(AuditoriaUtil.obterUsuario());
		return user.getPessoaFisica();
	}
	
	public List<FiltroHistoricoSuspensao> filtrarHistoricoSuspensaoCadastro(Date dataInicio, Date dataFim, ImovelRural ideImovelRural, int first, int pageSize, Integer ...ides)  {
		return histSuspensaoCadastroDAOImpl.filtrarHistoricoSuspensaoCadastro(dataInicio, dataFim, ideImovelRural, first, pageSize, ides);
	}
		
	public List<FiltroHistoricoSuspensao> visualizarSuspensaoCadastro(ImovelRural ideImovelRural)  {
		return histSuspensaoCadastroDAOImpl.visualizarSuspensaoCadastro(ideImovelRural);
	}
	
	public HistoricoSuspensaoCadastro obterHistoricoSuspensaoCadastroPorIdeImovelRural(ImovelRural imovelRural) {
		return histSuspensaoCadastroDAOImpl.obterHistoricoSuspensaoCadastroPorIdeImovelRural(imovelRural);
	}
	
}