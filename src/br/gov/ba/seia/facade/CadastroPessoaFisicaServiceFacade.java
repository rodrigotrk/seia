package br.gov.ba.seia.facade;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import br.gov.ba.seia.controller.PessoaFisicaController;
import br.gov.ba.seia.entity.DocumentoIdentificacao;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.service.DocumentoIdentificacaoService;
import br.gov.ba.seia.service.GerenciaArquivoService;
import br.gov.ba.seia.service.OrgaoExpedidorService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CadastroPessoaFisicaServiceFacade {
	
	@EJB
	private DocumentoIdentificacaoService documentoIdentificacaoService;
	@EJB
	private OrgaoExpedidorService orgaoExpedidorService;
	@EJB
	private GerenciaArquivoService gerenciaArquivoService;
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarDocumento(PessoaFisicaController pessoaFisicaController) throws Exception {
		
		Pessoa pessoa = pessoaFisicaController.getPessoa();
		
		if (!pessoaFisicaController.getTemArquivo() 
			&& ((pessoaFisicaController.getCadastroIncompleto() && isUsuarioExterno()) 
			|| Util.isNullOuVazio(pessoa.getPessoaFisica().getUsuario()) 
			|| pessoa.getPessoaFisica().getUsuario().isUsuarioExterno())) {
			JsfUtil.addErrorMessage("O campo upload de documento é de preenchimento obrigatório.");
		} 
		else {
			
			DocumentoIdentificacao documentoIdentificacaoSelecionado = pessoaFisicaController.getDocumentoIdentificacaoSelecionado().clone();
			
			if (!Util.isNullOuVazio(pessoaFisicaController.getModelDocumentosIdentificacao())) {
				
				for (DocumentoIdentificacao documento : pessoaFisicaController.getModelDocumentosIdentificacao()) {
					if (isNovoDocumentoIdentificacao(documentoIdentificacaoSelecionado)) {
						if (documento.getIdeTipoIdentificacao().equals(documentoIdentificacaoSelecionado.getIdeTipoIdentificacao())) {
							JsfUtil.addErrorMessage(" Este documento já está cadastrado! Favor cadastrar outro documento");
							return;
						}
					}
				}
				
			}
			
			String msgSucesso = Util.getString("geral_msg_alteracao_sucesso");
			
			documentoIdentificacaoSelecionado.setIdePessoa(pessoa);
			documentoIdentificacaoSelecionado.setIdeOrgaoExpedidor(orgaoExpedidorService.carregarOrgaoExpedidor(documentoIdentificacaoSelecionado.getIdeOrgaoExpedidor().getIdeOrgaoExpedidor()));
			
			if(isNovoDocumentoIdentificacao(documentoIdentificacaoSelecionado)) {
				documentoIdentificacaoSelecionado.setDtcCriacao(new Date());
				documentoIdentificacaoSelecionado.setDtcExclusao(null);
				documentoIdentificacaoSelecionado.setIndExcluido(false);
				prepararSalvarNovoDocumentoIdentificacao(documentoIdentificacaoSelecionado, pessoaFisicaController.getListaArquivoParaAtualizar());
				msgSucesso = Util.getString("geral_msg_inclusao_sucesso");
			}		
			
			documentoIdentificacaoService.salvarDocumentoIdentificacao(documentoIdentificacaoSelecionado);					
			pessoaFisicaController.setDocumentoIdentificacaoSelecionado(documentoIdentificacaoSelecionado);
			
			JsfUtil.addSuccessMessage(msgSucesso);				
		}
	}

	private boolean isUsuarioExterno() {
		return ContextoUtil.getContexto().isUsuarioExterno();
	}
	
	private Boolean isNovoDocumentoIdentificacao(DocumentoIdentificacao documentoIdentificacaoSelecionado) {
		return Util.isNull(documentoIdentificacaoSelecionado.getIdeDocumentoIdentificacao());
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void prepararSalvarNovoDocumentoIdentificacao(DocumentoIdentificacao documentoIdentificacaoSelecionado, List<DocumentoIdentificacao> listaArquivoParaAtualizar) throws Exception {
		for(DocumentoIdentificacao di : listaArquivoParaAtualizar) {
			di.setIndExcluido(true);
			di.setDtcExclusao(new Date());
			documentoIdentificacaoService.salvarDocumentoIdentificacao(di);
		}		
		documentoIdentificacaoService.salvarDocumentoIdentificacao(documentoIdentificacaoSelecionado);
		String caminhoArquivo = gerenciaArquivoService.uploadArquivoDocumentoIdentificacao(documentoIdentificacaoSelecionado, documentoIdentificacaoSelecionado.getArquivoEnviado());
		documentoIdentificacaoSelecionado.setDscCaminhoArquivo(caminhoArquivo);
	}
}