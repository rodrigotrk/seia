package br.gov.ba.seia.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.entity.Cerh;
import br.gov.ba.seia.entity.CerhCadastroCancelado;
import br.gov.ba.seia.entity.Telefone;
import br.gov.ba.seia.enumerator.DiretorioArquivoEnum;
import br.gov.ba.seia.facade.CancelarCerhServiceFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.util.FileUploadUtil;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.Util;

/**
 * @author eduardo.fernandes 
 * @since 30/04/2017
 *
 */
@Named("cancelarCerhController")
@ViewScoped
public class CancelarCerhController {

	@EJB
	private CancelarCerhServiceFacade facade;
	
	private Cerh cerh;
	private CerhCadastroCancelado cadastroCancelado;

	
	public void carregarDialog(ActionEvent event) {
		try {

			String acao = (String) event.getComponent().getAttributes().get("acao");
			cerh = facade.carregarDadosBasicos(((Cerh) event.getComponent().getAttributes().get("cerh")));

			CerhCadastroCancelado cerhCadastroCancelado = facade.carregarCerhCadastroCancelado(cerh);
			if(cerhCadastroCancelado ==null){
				cadastroCancelado = new CerhCadastroCancelado(cerh);
			}else{
				cadastroCancelado = cerhCadastroCancelado;
				cadastroCancelado.setIdeCerh(cerh);
			}

			if(acao.equals("cancelar")){
				cadastroCancelado.setVisualizar(true);
			}else if(acao.equals("excluir")){
				cadastroCancelado.setVisualizar(false);
			}
			
			Html.getCurrency().update("formCancelarCerh").show("dialogCancelar");
		}
		catch (Exception e) {
			MensagemUtil.erro(Util.getString("")); 
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void salvar() {
		if(validar()) {
			try {
				facade.cancelar(cadastroCancelado);
				MensagemUtil.sucesso(Util.getString("cerh_cancelar_msg_cancelamento_realizado"));
				Html.getCurrency().update("frmConsultaCerh").hide("dialogCancelar");
			} 
			catch (Exception e) {
				MensagemUtil.erro(Util.getString("")); 
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e, Util.SEIA_EXCEPTION);
				
			}
		}
	}
	
	public void prepararExclusao(ActionEvent event){

		try {
			cerh =  (Cerh) event.getComponent().getAttributes().get("cerh");
			cerh = facade.carregarDadosBasicos(cerh);
			cerh.setIdeEmpreendimento(facade.carregarEmpreendimento(cerh));
			cerh.setIdePessoaRequerente(facade.carregarPessoaRequerente(cerh));
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		
		Html.getCurrency().update("excluir_cadastro").show("confirma_exclusao");
		
	}
	
	
	public void excluir(ActionEvent event) {
		
		try {
			cerh.setIndExcluido(true);
			facade.salvar(cerh);

			MensagemUtil.sucesso("Excluido com sucesso");
			Html.getCurrency().update("frmConsultaCerh").hide("confirma_exclusao");
		} 
		catch (Exception e) {
			MensagemUtil.erro(Util.getString("")); 
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	private boolean validar() {
		boolean valido = true;
		if(Util.isNullOuVazio(cadastroCancelado.getDtcPessoaFisicaCancelamento())) {
			MensagemUtil.msg0003("A " + Util.getString("cerh_cancelar_data_cancelamento"));
			valido = false;
		}
		if(Util.isNullOuVazio(cadastroCancelado.getDscObservacao())) {
			MensagemUtil.msg0003("Justificativa");
			valido = false;
		}
		return valido;
	}
	
	public void uploadArquivo(FileUploadEvent event) {
		try {
			facade.deletarArquivo(cadastroCancelado.getUrlDocumento());
			cadastroCancelado.setUrlDocumento(FileUploadUtil.Enviar(event, DiretorioArquivoEnum.CERH.toString() + cerh.getIdeCerh()));
			MensagemUtil.sucesso(Util.getString("msg_generica_arquivo_enviado"));
		}
		catch (Exception e) {
			MensagemUtil.sucesso(Util.getString("msg_generica_erro_ao_carregar") + " o documento.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void excluirDocumento() {
		facade.deletarArquivo(cadastroCancelado.getUrlDocumento());
		cadastroCancelado.setUrlDocumento(null);
	}
	
	public StreamedContent getBaixarDocumento() {
		try {
			return facade.baixarArquivo(cadastroCancelado.getUrlDocumento());
		} 
		catch (Exception e) {
			MensagemUtil.erro(Util.getString("msg_generica_erro_ao_carregar") + " o documento.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public String getDataHoje() {
		return new SimpleDateFormat("dd/MM/yyyy").format(new Date(System.currentTimeMillis()));
	}
	
	public boolean isExisteArquivo() {
		return !Util.isNullOuVazio(cadastroCancelado.getUrlDocumento());
	}
	
	public Cerh getCerh() {
		return cerh;
	}

	public void setCerh(Cerh cerh) {
		this.cerh = cerh;
	}

	public CerhCadastroCancelado getCadastroCancelado() {
		return cadastroCancelado;
	}

	public void setCadastroCancelado(CerhCadastroCancelado cadastroCancelado) {
		this.cadastroCancelado = cadastroCancelado;
	}

	public List<String> getArquivo(){
		if(isExisteArquivo()) {
			return Arrays.asList(FileUploadUtil.getFileName(cadastroCancelado.getUrlDocumento()));
		}
		return new ArrayList<String>();
	}

	public String getTelefone() {
		for (Telefone telefone : cadastroCancelado.getIdeCerh().getIdePessoaRequerente().getTelefoneCollection()) {
			return telefone.getNumTelefone();
		}
		return "";
	}

	
}
