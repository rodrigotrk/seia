package br.gov.ba.seia.controller;

import java.io.File;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Named;

import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.entity.ArquivoProcesso;
import br.gov.ba.seia.entity.Notificacao;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.ArquivoProcessoService;
import br.gov.ba.seia.service.GerenciaArquivoService;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.SeiaControllerAb;
import br.gov.ba.seia.util.Util;

@Named("lstDocumentosDaNotificacaoController")
@ViewScoped
public class LstDocumentosDaNotificacaoController extends SeiaControllerAb {
	
	@EJB
	private ArquivoProcessoService arquivoProcessoService;
	@EJB
	private GerenciaArquivoService gerenciaArquivoService;
	
	private Notificacao notificacao;
	private ArquivoProcesso documentoSelecionado;
	private List<ArquivoProcesso> listaDocumentosDaNotificacao;
	private StreamedContent arquivoProcessoSC;
		
	public void carregarDocumentosDaNotificacao() {
		
		try {
			listaDocumentosDaNotificacao = arquivoProcessoService.listarArquivosProcessoPorNotificacao(notificacao);
			
			for(ArquivoProcesso arq : listaDocumentosDaNotificacao) {	
				
				if(!Util.isNull(arq.getDscCaminhoArquivo())){
					
					String caminhoArquivo = arq.getDscCaminhoArquivo().trim();
					File file = new File(caminhoArquivo);
					arq.setFileSize(file.length());
					arq.setFileName(file.getName());
				}
			}
		} catch(Exception e){
			listaDocumentosDaNotificacao = null;
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public StreamedContent getArquivoProcessoSC() {
		try {			
			arquivoProcessoSC = gerenciaArquivoService.getContentFile(documentoSelecionado.getDscCaminhoArquivo());
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Arquivo não encontrado!");
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
		
		return arquivoProcessoSC;
	}
	
	public Notificacao getNotificacao() {
		return notificacao;
	}
	
	public void setNotificacao(Notificacao notificacao) {
		this.notificacao = notificacao;
	}
	
	public List<ArquivoProcesso> getListaDocumentosDaNotificacao() {
		return listaDocumentosDaNotificacao;
	}
	
	public void setListaDocumentosDaNotificacao(List<ArquivoProcesso> listaDocumentosDaNotificacao) {
		this.listaDocumentosDaNotificacao = listaDocumentosDaNotificacao;
	}

	public ArquivoProcesso getDocumentoSelecionado() {
		return documentoSelecionado;
	}

	public void setDocumentoSelecionado(ArquivoProcesso documentoSelecionado) {
		this.documentoSelecionado = documentoSelecionado;
	}
	
}