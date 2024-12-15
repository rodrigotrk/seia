package br.gov.ba.seia.controller;

import javax.ejb.EJB;

import br.gov.ba.seia.dto.identificarPapel.SolicitacaoDTO;
import br.gov.ba.seia.facade.IdentificarPapelSolicitacaoFacade;
import br.gov.ba.seia.interfaces.PessoaFisicaAbaSolicitacao;
import br.gov.ba.seia.util.Html;

public class IdentificarSolicitacaoPessoaFisicaDocumentoController implements PessoaFisicaAbaSolicitacao {

	
	@EJB
	private IdentificarPapelSolicitacaoFacade facade;
	private SolicitacaoDTO socitacao;
	
	
	@Override
	public void load(SolicitacaoDTO socitacao) {
		this.socitacao = socitacao;	
		socitacao.getRequerente().setDocumentoIdentificacaoCollection(facade.carregarDocumentos(socitacao.getRequerente()));
	}

	@Override
	public void prepararDialog() {
		Html.getCurrency()
			.update(":pessoaFisicaDocumento:panelPessoaFisicaDialogDocumento")
			.show("dialogPessoaFisicaIncluirDocumento");
	}

	
	
	

}
