package br.gov.ba.seia.facade;

import java.util.Collection;

import javax.ejb.EJB;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import br.gov.ba.seia.entity.DocumentoIdentificacaoRequerimento;
import br.gov.ba.seia.service.DocumentoIdentificacaoRequerimentoService;
import br.gov.ba.seia.service.GerenciaArquivoService;

public class DocumentoIdentificacaoRequerimentoFacadeService {

	@EJB
	private DocumentoIdentificacaoRequerimentoService docObrigatorioRequerimentoService;
	@EJB
	private GerenciaArquivoService gerenciaArquivoService;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizar(Collection<DocumentoIdentificacaoRequerimento> docs, Boolean fazerUpload) throws Exception {
		for (DocumentoIdentificacaoRequerimento documentoObrigatorioRequerimento : docs) {
			documentoObrigatorioRequerimento.getIdeDocumentoIdentificacao().setDscCaminhoArquivo("null");
			docObrigatorioRequerimentoService.salvarOuAtualizar(documentoObrigatorioRequerimento, fazerUpload);
			String diretorioArquivo = gerenciaArquivoService.uploadArquivoDocumentoIdentificacaoRequerimento(documentoObrigatorioRequerimento);
			documentoObrigatorioRequerimento.getIdeDocumentoIdentificacao().setDscCaminhoArquivo(diretorioArquivo);
			docObrigatorioRequerimentoService.salvarOuAtualizar(documentoObrigatorioRequerimento, fazerUpload);
		}
	}

}
