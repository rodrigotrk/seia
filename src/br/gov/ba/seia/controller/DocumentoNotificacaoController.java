package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Named;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.RowEditEvent;

import br.gov.ba.seia.entity.ArquivoProcesso;
import br.gov.ba.seia.entity.CategoriaDocumento;
import br.gov.ba.seia.entity.Notificacao;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.enumerator.CategoriaDocumentoEnum;
import br.gov.ba.seia.enumerator.DiretorioArquivoEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.ArquivoProcessoService;
import br.gov.ba.seia.service.CategoriaDocumentoService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.FileUploadUtil;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.SeiaControllerAb;
import br.gov.ba.seia.util.Util;

@Named("documentoNotificacaoController")
@ViewScoped
public class DocumentoNotificacaoController extends SeiaControllerAb {
	
	@EJB
	private ArquivoProcessoService arquivoProcessoService;
	@EJB
	private CategoriaDocumentoService categoriaDocumentoService;	
	private Notificacao notificacao;
	private List<ArquivoProcesso> listaDeDocumentosDaNotificacao;
	private ArquivoProcesso documentoNotificacaoSelecionado;
	

	public boolean isDisabledApensarDocumentos() {
		return !Util.isNullOuVazio(listaDeDocumentosDaNotificacao);
	}

	public boolean isRenderedApensarDocumentos() {
		return Util.isNullOuVazio(listaDeDocumentosDaNotificacao);
	}

	
	public boolean existeDocumentoSemDescricao(){
		for(ArquivoProcesso arq : listaDeDocumentosDaNotificacao) {
			if(!arq.isDscConfirmada()){
				return true;
			}
		}
		return false;
	}
		
	public void onRowEdit(RowEditEvent event) {
	 	ArquivoProcesso arquivoEditado = ((ArquivoProcesso) event.getObject());
	 	if(!Util.isNullOuVazio(arquivoEditado.getDscArquivoProcesso())){
	 		arquivoEditado.setDscConfirmada(true);
		}
    }
	
	public void onRowCancel(RowEditEvent event) {
		ArquivoProcesso arquivoEditado = ((ArquivoProcesso) event.getObject());
		arquivoEditado.setDscConfirmada(true);
	}
	
	public void marcarDocumentoComoNaoConfirmado(ArquivoProcesso arquivoProcesso){
		arquivoProcesso.setDscConfirmada(false);
	}
	
	public void trataArquivo(FileUploadEvent event) {
		
		String caminho = FileUploadUtil.Enviar(event, DiretorioArquivoEnum.PROCESSO.toString());
		
		Pessoa pessoa = ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica().getPessoa();
		
		ArquivoProcesso arq = new ArquivoProcesso(caminho, "", notificacao.getIdeProcesso(), pessoa);
		arq.setDtcCriacao(new Date());
		arq.setIdeNotificacao(notificacao);
		
		try{
			CategoriaDocumento ctgDocumento = categoriaDocumentoService.carregarCategoriaDocumento(CategoriaDocumentoEnum.DOCUMENTO_DA_NOTIFICACAO.getId());
			
			arq.setCategoriaDocumento(ctgDocumento);
			
			if(listaDeDocumentosDaNotificacao == null) listaDeDocumentosDaNotificacao = new ArrayList<ArquivoProcesso>();
			
			listaDeDocumentosDaNotificacao.add(arq);
			
			JsfUtil.addSuccessMessage("O documento foi adicionado a lista.");
		} catch(Exception e){
			JsfUtil.addErrorMessage("Não foi possível adicionar o documento a lista.");
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void excluirDocumentoNotificacao(){
		Exception erro = null;
		try{
			if(documentoNotificacaoSelecionado.getIdeArquivoProcesso()==null){
				listaDeDocumentosDaNotificacao.remove(documentoNotificacaoSelecionado);
			}
			else{
				arquivoProcessoService.deletarArquivo(documentoNotificacaoSelecionado);
				listaDeDocumentosDaNotificacao.remove(documentoNotificacaoSelecionado);
			}			
			JsfUtil.addSuccessMessage("O documento foi removido com sucesso.");
			Html.atualizar("msgNotificacao"); 
		}
		catch(Exception e){
			erro =e;
			JsfUtil.addErrorMessage("Erro ao tentar remover o documento.");			
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
	}
	
	public void carregarListaDeDocumentosDaNotificacao() {
		try{
			if(notificacao == null || notificacao.getIdeNotificacao() == null){
				listaDeDocumentosDaNotificacao = null;
			}
			else{
				listaDeDocumentosDaNotificacao = arquivoProcessoService.listarArquivosProcessoPorNotificacao(notificacao);
			}
		}
		catch(Exception e){
			listaDeDocumentosDaNotificacao = new ArrayList<ArquivoProcesso>();
		}
	}
	
	public void salvarDocumentosNotificacao() {
		for(ArquivoProcesso arq : listaDeDocumentosDaNotificacao) {
			if(arq.getIdeArquivoProcesso()==null){
				arq.setIdeProcesso(notificacao.getIdeProcesso());
				arq.setIdeNotificacao(notificacao);
				arquivoProcessoService.salvar(arq);				
			}
			else{
				arquivoProcessoService.atualizar(arq);
			}
		}
	}

	public Notificacao getNotificacao() {
		return notificacao;
	}

	public void setNotificacao(Notificacao notificacao) {
		this.notificacao = notificacao;
	}

	public List<ArquivoProcesso> getListaDeDocumentosDaNotificacao() {
		return listaDeDocumentosDaNotificacao;
	}

	public void setListaDeDocumentosDaNotificacao(
			List<ArquivoProcesso> listaDeDocumentosDaNotificacao) {
		this.listaDeDocumentosDaNotificacao = listaDeDocumentosDaNotificacao;
	}

	public ArquivoProcesso getDocumentoNotificacaoSelecionado() {
		return documentoNotificacaoSelecionado;
	}

	public void setDocumentoNotificacaoSelecionado(
			ArquivoProcesso documentoNotificacaoSelecionado) {
		this.documentoNotificacaoSelecionado = documentoNotificacaoSelecionado;
	}
}