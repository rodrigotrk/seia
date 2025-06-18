package br.gov.ba.seia.controller;

import javax.ejb.EJB;
import javax.inject.Named;

import br.gov.ba.seia.faces.ViewScoped;

import org.primefaces.event.FileUploadEvent;

import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.service.GerenciaArquivoService;
import br.gov.ba.seia.util.JsfUtil;

@Named("arquivosController")
@ViewScoped
public class ArquivosController {
	@EJB
	private GerenciaArquivoService gerenciaArquivoService;

	public void handleFileUpload(FileUploadEvent event) {
		try {
			this.gerenciaArquivoService.uploadArquivo(event);
		} catch (Exception exception) {
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	public void handleFileUploadEmpreendimento(FileUploadEvent event) {
		try {
			Empreendimento empreendimento = new Empreendimento(1);
			this.gerenciaArquivoService.uploadArquivoEmpreendimento(event, empreendimento);
		} catch (Exception exception) {
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

}
