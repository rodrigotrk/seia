package br.gov.ba.seia.facade;

import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import br.gov.ba.seia.entity.DocumentoIdentificacao;
import br.gov.ba.seia.entity.DocumentoObrigatorioRequerimento;
import br.gov.ba.seia.entity.Enquadramento;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.StatusRequerimento;
import br.gov.ba.seia.enumerator.StatusRequerimentoEnum;
import br.gov.ba.seia.service.DocumentoObrigatorioRequerimentoService;
import br.gov.ba.seia.service.GerenciaArquivoService;
import br.gov.ba.seia.service.StatusRequerimentoService;
import br.gov.ba.seia.service.TramitacaoRequerimentoService;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class DocumentoObrigatorioRequerimentoFacadeService {

	@EJB
	private DocumentoObrigatorioRequerimentoService docObrigatorioRequerimentoService;
	@EJB
	private StatusRequerimentoService statusRequerimentoService;
	@EJB
	private TramitacaoRequerimentoService tramitacaoRequerimentoService;
	@EJB
	private GerenciaArquivoService gerenciaArquivoService;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizar(Collection<DocumentoObrigatorioRequerimento> docs, Boolean fazerUpload, Enquadramento enquadramento, Collection<DocumentoObrigatorioRequerimento> arqvsDocumento, Collection<DocumentoIdentificacao> arqvsDocumentoIdentificacao) throws Exception {
		Boolean isAllArquivosEnviados = Boolean.TRUE;
		for (DocumentoObrigatorioRequerimento documentoObrigatorioRequerimento : docs) {
			//			documentoObrigatorioRequerimento.setDscCaminhoArquivo("null");
			//			docObrigatorioRequerimentoService.salvarOuAtualizar(documentoObrigatorioRequerimento);
			if (fazerUpload) {
				String diretorioArquivo = gerenciaArquivoService.uploadArquivoDocumentoObrigatorioRequerimento(documentoObrigatorioRequerimento);
				documentoObrigatorioRequerimento.setDscCaminhoArquivo(diretorioArquivo);
			}
			docObrigatorioRequerimentoService.salvarOuAtualizar(documentoObrigatorioRequerimento);
		}
		for (DocumentoObrigatorioRequerimento lVwDocumentoIdentificacaoObrigatorio : arqvsDocumento) {
			if (Util.isNullOuVazio(lVwDocumentoIdentificacaoObrigatorio.getDscCaminhoArquivo()) && Util.isNullOuVazio(lVwDocumentoIdentificacaoObrigatorio.getFileSize())) {
				isAllArquivosEnviados = Boolean.FALSE;
				break;
			}		
		}
		for (DocumentoIdentificacao lDocumentoIdentificacao : arqvsDocumentoIdentificacao) {
			if (Util.isNullOuVazio(lDocumentoIdentificacao.getDscCaminhoArquivo())) {
				isAllArquivosEnviados = Boolean.FALSE;
				break;
			}		
		}
		StatusRequerimento statusRequerimento = statusRequerimentoService.getMaxStatusTramitacaoRequerimantoPorData(enquadramento);
		if (statusRequerimento != null) {
			if (statusRequerimento.getIdeStatusRequerimento().equals(StatusRequerimentoEnum.ENQUADRADO.getStatus())) {
				tramitarPendenciaEnvioDocumentacao(enquadramento);
				tramitarPendenciaValidacao(enquadramento, isAllArquivosEnviados);
			}
			if (statusRequerimento.getIdeStatusRequerimento().equals(StatusRequerimentoEnum.PENDENCIA_ENVIO_DOCUMENTACAO.getStatus())) {
				tramitarPendenciaValidacao(enquadramento, isAllArquivosEnviados);
			}
		}
	}
	
	/**
	 * @param enquadramento
	 * @throws Exception
	 */
	private void tramitarPendenciaEnvioDocumentacao(Enquadramento enquadramento) throws Exception {
		// Inserir nova tramita��o com status PENDENCIA_ENVIO_DOCUMENTACAO
		StatusRequerimento status = statusRequerimentoService.carregarGet(StatusRequerimentoEnum.PENDENCIA_ENVIO_DOCUMENTACAO.getStatus());
		tramitacaoRequerimentoService.criarTramitacaoRequerimento(enquadramento.getIdeRequerimentoUnico().getRequerimento(), status, enquadramento.getIdePessoa());
	}

	/**
	 * @param enquadramento
	 * @param isAllArquivosEnviados 
	 * @throws Exception
	 */
	private void tramitarPendenciaValidacao(Enquadramento enquadramento, Boolean isAllArquivosEnviados) throws Exception {
		if (isAllArquivosEnviados) {
			// Inserir nova tramita��o com status PENDENCIA_VALIDACAO
			StatusRequerimento status = statusRequerimentoService.carregarGet(StatusRequerimentoEnum.EM_VALIDACAO_PREVIA.getStatus());
			tramitacaoRequerimentoService.criarTramitacaoRequerimento(enquadramento.getIdeRequerimentoUnico().getRequerimento(), status, enquadramento.getIdePessoa());
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void deletar(List<DocumentoObrigatorioRequerimento> arqvsADeletar) throws Exception {
		docObrigatorioRequerimentoService.deletarDocumentosObrigatorio(arqvsADeletar);
	}

	public Collection<DocumentoObrigatorioRequerimento> listarFormulariosEnviados(Requerimento requerimento) throws Exception {
		return docObrigatorioRequerimentoService.listarFormulariosEnviados(requerimento);
	}

	public Collection<DocumentoObrigatorioRequerimento> listarDocumentosEnviados(Enquadramento enquadramento) throws Exception {
		return docObrigatorioRequerimentoService.listarDocumentosEnviados(enquadramento);
	}
	
	public DocumentoObrigatorioRequerimento buscarDocumentoObrigatorioRequerimento(DocumentoObrigatorioRequerimento ideDocumentoObrigatorioRequerimento) throws Exception{
		return docObrigatorioRequerimentoService.buscarDocumentoObrigatorioRequerimentoByIde(ideDocumentoObrigatorioRequerimento);
	}
}
