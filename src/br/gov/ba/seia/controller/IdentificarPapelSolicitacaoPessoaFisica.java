package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;

import org.primefaces.event.TabChangeEvent;

import br.gov.ba.seia.dto.AbaDTO;
import br.gov.ba.seia.dto.identificarPapel.SolicitacaoDTO;
import br.gov.ba.seia.entity.DocumentoIdentificacao;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.enumerator.IdentificarPessoaFisicaAbaEnum;
import br.gov.ba.seia.facade.IdentificarPapelSolicitacaoFacade;
import br.gov.ba.seia.interfaces.PessoaFisicaAbaSolicitacao;
import br.gov.ba.seia.interfaces.PessoaSolicitacao;

public class IdentificarPapelSolicitacaoPessoaFisica implements PessoaSolicitacao {

	@EJB
	private IdentificarPapelSolicitacaoFacade facade;
	private SolicitacaoDTO solicitacao;
	private List<AbaDTO> abas;

	private PessoaFisicaAbaSolicitacao abaSolicitacao;
	private DocumentoIdentificacao documentoIdentificacao;

	@PostConstruct
	public void init(){
		inicialializarVariaveis();
	}

	@Override
	public void load(SolicitacaoDTO solicitacao) {
		this.solicitacao = solicitacao;

		solicitacao.setRequerente(new Pessoa());
		solicitacao.getRequerente().setPessoaFisica(new PessoaFisica());

		inicialializarVariaveis();
	}



	private void inicialializarVariaveis() {
		int id = 0;
		abas = new  ArrayList<AbaDTO>();
		for (IdentificarPessoaFisicaAbaEnum abasPessoa : IdentificarPessoaFisicaAbaEnum.values()) {
			AbaDTO aba = new AbaDTO();
			aba.setId(id++);
			aba.setNome(abasPessoa.getNome());
			aba.setDisabled(!abasPessoa.equals(IdentificarPessoaFisicaAbaEnum.DADOS_BASICOS));
			aba.setAbaAtual(abasPessoa.equals(IdentificarPessoaFisicaAbaEnum.DADOS_BASICOS));
			prepararControllerAba(aba);
			abas.add(aba);
		}

		if(solicitacao != null){
			solicitacao.setCadastroCompleto(false);
		}

		documentoIdentificacao = new DocumentoIdentificacao();
	}


	private void prepararControllerAba(AbaDTO aba){
		if(aba.getNome().equals(IdentificarPessoaFisicaAbaEnum.DOCUMENTOS.getNome())){
			abaSolicitacao =  new IdentificarSolicitacaoPessoaFisicaDocumentoController();
		}

	}

	@Override
	public void buscarRequerente() {
		solicitacao.setRequerente(facade.buscarPessoa(solicitacao.getRequerente()));
	}

	@Override
	public void onChange(TabChangeEvent e) {

	}

	@Override
	public boolean isCadastroCompleto() {
		return false;
	}

	@Override
	public void avancarAba(){

		for(int x=0; x<abas.size();x++){
			if(abas.get(x).isAbaAtual()){
				abas.get(x).setAbaAtual(false);
				abas.get(x+1).setDisabled(false);
				abas.get(x+1).setAbaAtual(true);
				return;
			}
		}


		throw new RuntimeException("Aba não encontrada");
	}

	@Override
	public int getAbaAtiva(){
		for (AbaDTO aba : abas) {
			if(aba.isAbaAtual()){
				return aba.getId();
			}
		}

		throw new RuntimeException("Aba não encontrada");
	}

	@Override
	public void setAbaAtiva(){
	}

	@Override
	public boolean validarBuscarRequerente() {
		return true;
	}

	@Override
	public AbaDTO aba(IdentificarPessoaFisicaAbaEnum pessoaFisica){
		for (AbaDTO aba : abas) {
			if(aba.getNome().equals(pessoaFisica.getNome())){
				return aba;
			}
		}
		throw new RuntimeException("deu mole na passagem de parametros");
	}

	@Override
	public void prepararDialog() {
		abaSolicitacao.prepararDialog();
		documentoIdentificacao = new DocumentoIdentificacao();
	}

	@Override
	public DocumentoIdentificacao getDocumentoIdentificacao() {
		return documentoIdentificacao;
	}

	@Override
	public void setDocumentoIdentificacao(DocumentoIdentificacao documentoIdentificacao) {
		this.documentoIdentificacao = documentoIdentificacao;
	}

	@Override
	public boolean isPossuiRequerente() {
		return (solicitacao.getRequerente() != null) && (solicitacao.getRequerente().getPessoaFisica().getNomPessoa()!=null);
	}

	@Override
	public boolean validarDocumentoConsulta() {
		return false;
	}

	@Override
	public boolean validarPreencimentoAba() {
		return false;
	}

}
