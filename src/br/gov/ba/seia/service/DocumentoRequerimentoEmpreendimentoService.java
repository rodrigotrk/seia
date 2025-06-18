package br.gov.ba.seia.service;

import java.io.File;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.controller.ValidacaoPreviaController;
import br.gov.ba.seia.dao.DocumentoRequerimentoEmpreendimentoDAOImpl;
import br.gov.ba.seia.entity.DocumentoRequerimentoEmpreendimento;
import br.gov.ba.seia.entity.EmpreendimentoRequerimento;
import br.gov.ba.seia.enumerator.DiretorioArquivoEnum;
import br.gov.ba.seia.util.FileUploadUtil;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class DocumentoRequerimentoEmpreendimentoService {

	@Inject
	private DocumentoRequerimentoEmpreendimentoDAOImpl daoImpl;
	
	@Inject
	private GerenciaArquivoService gerenciaArquivoService;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarDocumentoRequerimentoEmpreendimento(DocumentoRequerimentoEmpreendimento documentoRequerimentoEmpreendimento){
		daoImpl.salvarDocumentoRequerimentoEmpreendimento(documentoRequerimentoEmpreendimento);
		
	}
	
	public List<DocumentoRequerimentoEmpreendimento> listarDocumentoRequerimentoEmpreendimento(EmpreendimentoRequerimento ideEmpreendimentoRequerimento){
		return daoImpl.listarDocumentoRequerimentoEmpreendimento(ideEmpreendimentoRequerimento);
	}
	
	public void copiarArquivoArt(DocumentoRequerimentoEmpreendimento documentoRequerimentoEmpreendimento) throws Exception{
		
		String caminhoNovo = DiretorioArquivoEnum.DOCUMENTO_REQUERIMENTO_EMPREENDIMENTO.toString() +
				documentoRequerimentoEmpreendimento.getIdePessoaFisica() + File.separator + 
				FileUploadUtil.getFileName(documentoRequerimentoEmpreendimento.getDscCaminhoArquivoAntigo());
		
		documentoRequerimentoEmpreendimento.setDscCaminhoArquivo(caminhoNovo);
		
		gerenciaArquivoService.copyFile(documentoRequerimentoEmpreendimento.getDscCaminhoArquivoAntigo(),gerenciaArquivoService.criarSubDiretorioRetornandoPath(caminhoNovo));
		
		documentoRequerimentoEmpreendimento.getTamanhoDoc();
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarDocumentoRequerimentoEmpreendimentoEmLote(List<DocumentoRequerimentoEmpreendimento> documentoRequerimentoEmpreendimentos) {
	
		daoImpl.salvarDocumentoRequerimentoEmpreendimentoEmLote(documentoRequerimentoEmpreendimentos);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerDocumentoRequerimentoEmpreendimento(DocumentoRequerimentoEmpreendimento documentoRequerimentoEmpreendimento) {
		
		daoImpl.removerDocumentoRequerimentoEmpreendimento(documentoRequerimentoEmpreendimento);
		removerArquivo(documentoRequerimentoEmpreendimento);
	}	
	
	public void removerArquivo(DocumentoRequerimentoEmpreendimento documentoRequerimentoEmpreendimento){
	
		gerenciaArquivoService.deletarArquivo(documentoRequerimentoEmpreendimento.getDscCaminhoArquivo());
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void carregarDocumentoRequerimento(ValidacaoPreviaController previaController) throws Exception{
		previaController.verificarDocumentoArt();
	}
	
	public List<DocumentoRequerimentoEmpreendimento> listarDocumentoRequerimentoPorRequerimento(Integer ideRequerimento) {
		return daoImpl.listarDocumentoRequerimentoPorRequerimento(ideRequerimento);
	}
}
