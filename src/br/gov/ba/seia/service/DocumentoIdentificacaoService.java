package br.gov.ba.seia.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.primefaces.model.UploadedFile;

import br.gov.ba.seia.dao.DocumentoIdentificacaoDAOImpl;
import br.gov.ba.seia.entity.DocumentoIdentificacao;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class DocumentoIdentificacaoService {

	@Inject
	private DocumentoIdentificacaoDAOImpl documentoIdentificacaoDAOImpl;

	@Inject
	private GerenciaArquivoService gerenciaArquivoService;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<DocumentoIdentificacao> listar(Pessoa pessoa)  {
		return documentoIdentificacaoDAOImpl.listar(pessoa);
	}	
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public List<DocumentoIdentificacao> listarDocumentosIdentificacaoPorPessoa(Pessoa pessoa) throws Exception {
		List<DocumentoIdentificacao>  lista = new ArrayList<DocumentoIdentificacao>();
		
		if(!Util.isNullOuVazio(pessoa)){
			
			lista = documentoIdentificacaoDAOImpl.listarDocumentosIdentificacaoPorPessoa(pessoa);
		}
		return lista;
	}	

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarDocumentoIdentificacao(DocumentoIdentificacao documentoIdentificacao)  {
		documentoIdentificacaoDAOImpl.salvarDocumentoIdentificacao(documentoIdentificacao);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizarDocumentoIdenticacao(DocumentoIdentificacao documentoIdentificacao, UploadedFile file) throws Exception {
		
		File arquivoAntigo = new File(documentoIdentificacao.getDscCaminhoArquivo());
		if(!Util.isNullOuVazio(arquivoAntigo)){
			arquivoAntigo.delete();
		}
		arquivoAntigo=null;
		String caminho = gerenciaArquivoService.uploadArquivoDocumentoIdentificacao(documentoIdentificacao, file);
		documentoIdentificacao.setDscCaminhoArquivo(caminho);
		documentoIdentificacaoDAOImpl.salvarDocumentoIdentificacao(documentoIdentificacao);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirDocumentoIdentificacao(DocumentoIdentificacao documentoIdentificacao)  {
		documentoIdentificacaoDAOImpl.excluirDocumentoIdentificacao(documentoIdentificacao);
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public DocumentoIdentificacao recuperarDocumentoById(DocumentoIdentificacao documentoIdentificacao) {
		return documentoIdentificacaoDAOImpl.recuperarDocumentoById(documentoIdentificacao);
	}

	public List<DocumentoIdentificacao> listarDocumentosIdentificacaoRequerente(Integer ideRequerimento) {
		return this.documentoIdentificacaoDAOImpl.listarDocumentosIdentificacaoRequerente(ideRequerimento);
	}
}
