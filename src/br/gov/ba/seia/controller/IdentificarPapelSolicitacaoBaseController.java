package br.gov.ba.seia.controller;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.apache.log4j.Level;

import br.gov.ba.seia.dto.identificarPapel.SolicitacaoDTO;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.enumerator.PaginaEnum;
import br.gov.ba.seia.enumerator.PapelSolicitacaoEnum;
import br.gov.ba.seia.facade.IdentificarPapelSolicitacaoFacade;
import br.gov.ba.seia.interfaces.PessoaSolicitacao;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MetodoUtil;

	
public class IdentificarPapelSolicitacaoBaseController{
	
	@EJB
	private IdentificarPapelSolicitacaoFacade facade;
	
	@Inject
	private IdentificarPapelSolicitacaoPessoaFisica pessoaFisicaSolicitacao;

	@Inject
	private IdentificarPapelSolicitacaoPessoaJuridica pessoaJuridicaSolicitacao;
	
	@Inject
	private IdentificarPapelSolicitacaoRepresentanteLegal representanteLegal;
	
	
	
	protected PessoaSolicitacao pessoaSolicitacao;
	protected SolicitacaoDTO solicitacao;
	protected MetodoUtil metodoSelecionarSolicitante;
	protected PapelSolicitacaoEnum[] papelSolicitacaoEnum;
	
	protected void inicializarSolicitacao() {
		if(ContextoUtil.getContexto().getSolicitacao()==null){
			solicitacao = new SolicitacaoDTO();
		}
		else{
			solicitacao = ContextoUtil.getContexto().getSolicitacao();
			ContextoUtil.getContexto().setSolicitacao(null);
		}

		solicitacao.setPaginaEnum(PaginaEnum.CADASTRO_CERH);
		solicitacao.setUsuarioLogado(ContextoUtil.getContexto().getUsuarioLogado());
	}
	

	protected void inicializarVariaveis(){
		papelSolicitacaoEnum = PapelSolicitacaoEnum.values();
		try {
			metodoSelecionarSolicitante = new MetodoUtil(this, this.getClass().getDeclaredMethod("receberSolicitante", Pessoa.class));
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		} 
	}


	protected void selecionarPapelSolicitacao() {
		
		if(solicitacao.getPapelSolicitacaoEnum() == PapelSolicitacaoEnum.REQUERENTE){
			solicitacao.setRequerente(solicitacao.getSolicitante());
			solicitacao.setCadastroCompleto(true);
			return;
		}
		
		else if(solicitacao.getPapelSolicitacaoEnum() == PapelSolicitacaoEnum.PROCURADOR_PF){
			pessoaSolicitacao = pessoaFisicaSolicitacao;
		}
		
		else if(solicitacao.getPapelSolicitacaoEnum() == PapelSolicitacaoEnum.PROCURADOR_PJ){
			pessoaSolicitacao = pessoaJuridicaSolicitacao;
		}
		
		else if(solicitacao.getPapelSolicitacaoEnum() == PapelSolicitacaoEnum.REPRESENTANTE_LEGAL_PJ){
			pessoaSolicitacao = representanteLegal;
		}

		pessoaSolicitacao.load(solicitacao);	
	}
	
	
	public void finalizarSolicitacao(){
		Html.redirecionarPagina(solicitacao.getPaginaEnum());
	}
	
	public IdentificarPapelSolicitacaoFacade getFacade() {
		return facade;
	}

	public void buscarRequerente(){
		pessoaSolicitacao.buscarRequerente();
	}
	
	public PessoaSolicitacao getPessoaSolicitacao() {
		return pessoaSolicitacao;
	}

	public void setPessoaSolicitacao(PessoaSolicitacao pessoaSolicitacao) {
		this.pessoaSolicitacao = pessoaSolicitacao;
	}

	public void limpar(){
		solicitacao.setRequerente(new Pessoa());
		solicitacao.getRequerente().setPessoaFisica(new PessoaFisica());
		solicitacao.setSolicitante(new Pessoa());
		solicitacao.getSolicitante().setPessoaFisica(new PessoaFisica());
		
		solicitacao.setPapelSolicitacaoEnum(null);
		solicitacao.setCadastroCompleto(false);
		
		Html.atualizar("identificarPapelSolicitacao");
	}
	
}
	