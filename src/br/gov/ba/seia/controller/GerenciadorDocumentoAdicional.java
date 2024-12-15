package br.gov.ba.seia.controller;

import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.DocumentoObrigatorioRequerimento;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum;
import br.gov.ba.seia.util.Util;

/**
 * @author eduardo.fernandes
 *
 */
public class GerenciadorDocumentoAdicional {

	@Inject
	public UploadDocumentoAdicionalController documentoAdicionalController;

	public StreamedContent getDadosAdicionais(String nomeDoArquivo, String nomeDeSaida) throws FileNotFoundException{
		InputStream stream = ((ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream("/resources/fce/documento-adicional/"+nomeDoArquivo);
		DefaultStreamedContent file = new DefaultStreamedContent(stream, "resources/fce/documento-adicional/doc", nomeDeSaida);
		return file;
	}

	public void excluirDocumentoUpadoAnteriormente(String caminho){
		if(!Util.isNullOuVazio(caminho)){
			documentoAdicionalController.setCaminhoArquivoDocFceDeletar(caminho);
		}
		documentoAdicionalController.deletarUploadSalvoAnteriormente();
	}

	public StreamedContent getDocumentoAdicionalUpado(String string) {
		return documentoAdicionalController.getDocumentoAdicionalUpado(string);
	}

	public void salvarDocumentoAdicional(Requerimento requerimento, DocumentoObrigatorio documentoObrigatorio) throws Exception {
		documentoAdicionalController.salvarUploadDocumentoObrigatorioRequerimento(requerimento, documentoObrigatorio);
	}

	public void carregarDocumentoAdicionalByDocumentoObrigatorio(Requerimento requerimento, DocumentoObrigatorioEnum documentoObrigatorio) throws Exception {
		documentoAdicionalController.carregarDocumentoObrigatorioAdicional(requerimento, documentoObrigatorio);
	}

	public void carregarDocumentoAdicionalByDocumentoObrigatorioRequerimento(DocumentoObrigatorioRequerimento documentoObrigatorioRequerimento) throws Exception{
		documentoAdicionalController.carregarExrtatoHidrografico(documentoObrigatorioRequerimento);
	}

	public void limparDocumentoUpado(){
		documentoAdicionalController.limparListaArquivo();
		excluirDocumentoUpadoAnteriormente(null);
	}

	public DocumentoObrigatorioRequerimento getDocumentoUpado(){
		return documentoAdicionalController.getDocObrigatorioRequerimentoUpado();
	}

	public void inicarDocumentoAdicional(Requerimento requerimento){
		documentoAdicionalController.setDocObrigatorioRequerimentoUpado(new DocumentoObrigatorioRequerimento(requerimento));
	}

	public boolean isArquivoUpado(){
		return documentoAdicionalController.isUploadRealizado();
	}

	public void setDesabilitarDocumentoAdicional(Boolean desabilita){
		documentoAdicionalController.setDesabilitar(desabilita);
	}
}