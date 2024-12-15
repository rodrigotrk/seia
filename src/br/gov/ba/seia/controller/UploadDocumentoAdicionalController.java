package br.gov.ba.seia.controller;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.DocumentoAto;
import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.DocumentoObrigatorioRequerimento;
import br.gov.ba.seia.entity.EnquadramentoAtoAmbiental;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.enumerator.DiretorioArquivoEnum;
import br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.DocumentoAtoAmbientalService;
import br.gov.ba.seia.service.DocumentoObrigatorioRequerimentoService;
import br.gov.ba.seia.service.EnquadramentoService;
import br.gov.ba.seia.service.GerenciaArquivoService;
import br.gov.ba.seia.util.FileUploadUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

/**
 *
 * Classe que controla o Upload/Download dos Documentos Adicionais.
 *
 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
 * @since 17/03/2015
 */
@Named("uploadDocumentoAdicionalController")
@ViewScoped
public class UploadDocumentoAdicionalController {

	@EJB
	private GerenciaArquivoService gerenciaArquivoService;
	@EJB
	private DocumentoObrigatorioRequerimentoService documentoObrigatorioRequerimentoService;
	@EJB
	private EnquadramentoService enquadramentoService;
	@EJB
	private DocumentoAtoAmbientalService documentoAtoAmbientalService;

	private List<String> listaArquivo;
	private DocumentoObrigatorioRequerimento docObrigatorioRequerimentoUpado;
	private String caminhoArquivoDocFceDeletar;

	private boolean desabilitar;

	// INI - Implementação de FCE INTERVENCAO BARRAGEM
	public void salvarUploadDocumentoObrigatorioRequerimento(Requerimento requerimento, DocumentoObrigatorio docObrigatorio) throws Exception {
		if(!Util.isNullOuVazio(docObrigatorioRequerimentoUpado.getFileUpload())) {
			deletarNovoUpload();
			StreamedContent file = new DefaultStreamedContent(new DataInputStream(new BufferedInputStream(docObrigatorioRequerimentoUpado.getFileUpload().getFile().getInputstream())), docObrigatorioRequerimentoUpado.getFileUpload().getFile().getContentType(), docObrigatorioRequerimentoUpado.getFileUpload().getFile().getFileName());
			montarDocumentoObrigatorioRequerimento(requerimento, docObrigatorio, file);
			documentoObrigatorioRequerimentoService.salvarOuAtualizar(docObrigatorioRequerimentoUpado);
		}
	}

	private void montarDocumentoObrigatorioRequerimento(Requerimento requerimento, DocumentoObrigatorio docObrigatorio, StreamedContent file) throws Exception {
		docObrigatorioRequerimentoUpado.setFile(file);
		docObrigatorioRequerimentoUpado.setFileSize(docObrigatorioRequerimentoUpado.getFileUpload().getFile().getSize());
		docObrigatorioRequerimentoUpado.setIdeDocumentoObrigatorio(docObrigatorio);
		docObrigatorioRequerimentoUpado.setIdeRequerimento(requerimento);
		docObrigatorioRequerimentoUpado.setIndDocumentoValidado(false);
		docObrigatorioRequerimentoUpado.setIndSigiloso(false);

		DocumentoAto documentoAto = null;
		EnquadramentoAtoAmbiental enquadramentoAtoAmbiental = null;
		List<EnquadramentoAtoAmbiental> listaEnquadramentoAto = getIdeEnquadramentoAtoAmbientalParaDocumentoObrigatorioRequerimento(requerimento);
		if(listaEnquadramentoAto.size() > 1){
			for(EnquadramentoAtoAmbiental enq : listaEnquadramentoAto){
				List<DocumentoAto> listaDocumentoAto = getDocumentoAtoParaDocumentoObrigatorioRequerimento(docObrigatorio, enq.getAtoAmbiental());
				for(DocumentoAto docAto: listaDocumentoAto){
					if(enq.getAtoAmbiental().equals(docAto.getIdeAtoAmbiental())){
						enquadramentoAtoAmbiental = enq;
						documentoAto = docAto;
						break;
					}
				}
			}
		}
		else {
			enquadramentoAtoAmbiental = listaEnquadramentoAto.get(0);
			List<DocumentoAto> listaDocumentoAto = getDocumentoAtoParaDocumentoObrigatorioRequerimento(docObrigatorio, enquadramentoAtoAmbiental.getAtoAmbiental());
			for(DocumentoAto docAto: listaDocumentoAto){
				if(enquadramentoAtoAmbiental.getAtoAmbiental().equals(docAto.getIdeAtoAmbiental())){
					documentoAto = docAto;
					break;
				}
			}
		}
		docObrigatorioRequerimentoUpado.setIdeEnquadramentoAtoAmbiental(enquadramentoAtoAmbiental);
		docObrigatorioRequerimentoUpado.setIdeDocumentoAto(documentoAto);
		documentoObrigatorioRequerimentoService.salvarOuAtualizar(docObrigatorioRequerimentoUpado);
		String caminho = DiretorioArquivoEnum.DOCUMENTOOBRIGATORIO + docObrigatorioRequerimentoUpado.getIdeDocumentoObrigatorioRequerimento().toString();
		docObrigatorioRequerimentoUpado.setDscCaminhoArquivo(FileUploadUtil.Enviar(docObrigatorioRequerimentoUpado.getFileUpload(), caminho));
	}

	private List<EnquadramentoAtoAmbiental> getIdeEnquadramentoAtoAmbientalParaDocumentoObrigatorioRequerimento(Requerimento requerimento) throws Exception {
		return enquadramentoService.buscarEnquadramentoAtoAmbientalByRequerimento(requerimento.getIdeRequerimento());
	}

	private List<DocumentoAto> getDocumentoAtoParaDocumentoObrigatorioRequerimento(DocumentoObrigatorio documentoObrigatorio, AtoAmbiental atoAmbiental) throws Exception {
		return documentoAtoAmbientalService.buscarByAtoAmbientalAndDocumentoObrigatorio(documentoObrigatorio.getIdeDocumentoObrigatorio(), atoAmbiental.getIdeAtoAmbiental());
	}

	public void deletarUploadSalvoAnteriormente(){
		if(existeDocumentoParaDeletar()){
			try {
				DocumentoObrigatorioRequerimento docParaDeletar = documentoObrigatorioRequerimentoService.buscarDocumentoObrigatorioRequerimentoByCaminhoArquivo(caminhoArquivoDocFceDeletar);
				if(!Util.isNullOuVazio(docParaDeletar)){
					documentoObrigatorioRequerimentoService.excluirDocumentoObrigatorioRequerimentoByIdeDocObrReq(docParaDeletar);
					gerenciaArquivoService.deletarArquivo(caminhoArquivoDocFceDeletar);
					caminhoArquivoDocFceDeletar = null;
				}
			} catch (Exception e) {
				JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_excluir") + " ao excluir o Documento Adicional: \"" + caminhoArquivoDocFceDeletar + "\".");
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
	}

	protected boolean existeDocumentoParaDeletar() {
		return !Util.isNullOuVazio(caminhoArquivoDocFceDeletar);
	}

	private void deletarNovoUpload(){
		if(!Util.isNullOuVazio(docObrigatorioRequerimentoUpado.getDscCaminhoArquivo())) {
			gerenciaArquivoService.deletarArquivo(docObrigatorioRequerimentoUpado.getDscCaminhoArquivo());
		}
	}

	public void prepararUploadDocumentoAdicional(FileUploadEvent event) {
		listaArquivo = new ArrayList<String>();
		String caminhoArquivo = FileUploadUtil.Enviar(event, DiretorioArquivoEnum.DOCUMENTOOBRIGATORIO.toString());
		listaArquivo.add(FileUploadUtil.getFileName(caminhoArquivo));
		docObrigatorioRequerimentoUpado = new DocumentoObrigatorioRequerimento();
		docObrigatorioRequerimentoUpado.setFileUpload(event);
		docObrigatorioRequerimentoUpado.setDscCaminhoArquivo(caminhoArquivo);
		JsfUtil.addSuccessMessage("Arquivo Enviado com Sucesso.");
	}

	private void iniciarLista(){
		listaArquivo = new ArrayList<String>();
	}

	public boolean isUploadRealizado() {
		return !Util.isNullOuVazio(listaArquivo);
	}

	public void limparListaArquivo(){
		if(!Util.isNullOuVazio(listaArquivo)){
			listaArquivo.clear();
		}
	}

	public StreamedContent getDocumentoObrigatorioRequerimentoUpado() {
		try {
			return gerenciaArquivoService.getContentFile(docObrigatorioRequerimentoUpado.getDscCaminhoArquivo());
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Arquivo não encontrado.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public void carregarExrtatoHidrografico(DocumentoObrigatorioRequerimento ideDocumentoObrigatorioRequerimento) throws Exception{
		docObrigatorioRequerimentoUpado = documentoObrigatorioRequerimentoService.buscarDocumentoObrigatorioRequerimentoByIde(ideDocumentoObrigatorioRequerimento);
		iniciarLista();
		if(!Util.isNullOuVazio(docObrigatorioRequerimentoUpado) && !Util.isNullOuVazio(docObrigatorioRequerimentoUpado.getDscCaminhoArquivo())){
			listaArquivo.add(docObrigatorioRequerimentoUpado.getDscCaminhoArquivo());
		}
	}

	public void carregarDocumentoObrigatorioAdicional(Requerimento requerimento, DocumentoObrigatorioEnum documentoObrigatorio) throws Exception{
		docObrigatorioRequerimentoUpado = documentoObrigatorioRequerimentoService.buscarDocObrigatorioRequerimentoByRequerimentoAndIdeDocumentoObrigatorioEnum(requerimento, documentoObrigatorio.getId());
		iniciarLista();
		if(!Util.isNullOuVazio(docObrigatorioRequerimentoUpado) && !Util.isNullOuVazio(docObrigatorioRequerimentoUpado.getDscCaminhoArquivo())){
			listaArquivo.add(docObrigatorioRequerimentoUpado.getDscCaminhoArquivo());
		}
	}

	public StreamedContent getDocumentoAdicionalUpado() {
		try {
			return gerenciaArquivoService.getContentFile(docObrigatorioRequerimentoUpado.getDscCaminhoArquivo());
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_arquivo_nao_encontrado"));
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public StreamedContent getDocumentoAdicionalUpado(String caminho) {
		try {
			return gerenciaArquivoService.getContentFile(caminho);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_arquivo_nao_encontrado"));
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public boolean isDocumentoValidado(){
		return !Util.isNull(docObrigatorioRequerimentoUpado) && !Util.isNull(docObrigatorioRequerimentoUpado.getIndDocumentoValidado()) && docObrigatorioRequerimentoUpado.getIndDocumentoValidado();
	}

	public List<String> getListaArquivo() {
		return listaArquivo;
	}

	public String getCaminhoArquivoDocFceDeletar() {
		return caminhoArquivoDocFceDeletar;
	}

	public void setCaminhoArquivoDocFceDeletar(String caminhoArquivoDocFceDeletar) {
		this.caminhoArquivoDocFceDeletar = caminhoArquivoDocFceDeletar;
	}

	public DocumentoObrigatorioRequerimento getDocObrigatorioRequerimentoUpado() {
		return docObrigatorioRequerimentoUpado;
	}

	public void setDocObrigatorioRequerimentoUpado(DocumentoObrigatorioRequerimento docObrigatorioRequerimentoUpado) {
		this.docObrigatorioRequerimentoUpado = docObrigatorioRequerimentoUpado;
	}

	public boolean isDesabilitar() {
		return desabilitar;
	}

	public void setDesabilitar(boolean desabilitarTudo) {
		this.desabilitar = desabilitarTudo;
	}
}