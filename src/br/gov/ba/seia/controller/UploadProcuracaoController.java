package br.gov.ba.seia.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.entity.DocumentoImovelRural;
import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.TipoVinculoImovel;
import br.gov.ba.seia.enumerator.DiretorioArquivoEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.GerenciaArquivoService;
import br.gov.ba.seia.service.ImovelRuralService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.FileUploadUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Named("uploadProcuracaoController")
@ViewScoped
public class UploadProcuracaoController {

	@EJB
	ImovelRuralService imovelRuralService;
	
	@EJB
	GerenciaArquivoService gerenciaArquivoService;
		
	private String caminhoArquivo;
	private String labelUpload;
	private boolean temArquivo;
	private List<String> listaArquivo;
	private Boolean disableUploadDoc;
	private StreamedContent arquivoBaixar;
	private String arquivoAExcluir;
	private TipoVinculoImovel tipoVinculo;

	private String arquivoSelecionado; 
	
	@PostConstruct
	public void init(){
		caminhoArquivo = null;
		temArquivo = false;
		listaArquivo = new ArrayList<String>();
		if(!Util.isNullOuVazio(ContextoUtil.getContexto().getProcuracaoEmEdicao()))
			listaArquivo.add(ContextoUtil.getContexto().getProcuracaoEmEdicao().getDscCaminhoArquivo());
		disableUploadDoc = true;
		labelUpload = "Upload da Procuração";
	}

	public void trataDocDoImovel(FileUploadEvent event) {
		if(!Util.isNullOuVazio(ContextoUtil.getContexto().getImovelRural()) && !Util.isNullOuVazio(ContextoUtil.getContexto().getImovelRural().getIdeImovelRural())){
			uploadDocumentoImovel(event);
		}
	}
	
	private void uploadDocumentoImovel(FileUploadEvent event){
		Exception erro = null;
		try {
			ImovelRural imRur = ContextoUtil.getContexto().getImovelRural();
			String tipoDocumento = "Procuracao";
			
			caminhoArquivo = FileUploadUtil.EnviarShape(event, DiretorioArquivoEnum.IMOVELRURAL.toString()+FileUploadUtil.getFileSeparator()+imRur.getIdeImovelRural().toString(),
					imRur.getIdeImovelRural().toString()+"_"+tipoDocumento+"_"+imRur.getDesDenominacao());
			if(!Util.isNullOuVazio(imRur.getIdeDocumentoProcuracao())){
				imRur.getIdeDocumentoProcuracao().setDscCaminhoArquivo(caminhoArquivo);
				imRur.getIdeDocumentoProcuracao().setNomDocumentoObrigatorio(tipoDocumento);
			}else{
				imRur.setIdeDocumentoProcuracao(new DocumentoImovelRural(null, tipoDocumento, caminhoArquivo));
				File file = new File(caminhoArquivo.trim());				
				imRur.getIdeDocumentoProcuracao().setFileSize(file.length());
			}
			temArquivo = true;
			//imRur.getImovel().setDtcCriacao(dtcCriacao);
			imovelRuralService.atualizar(imRur);
			if(listaArquivo.size()<=1){
				System.out.println("Procuração inserida pela Primeira vez no Imóvel Rural de ID="+imRur.getIdeImovelRural()+" e denomicacao: "+imRur.getDesDenominacao()+", realizado pelo usuario de CPF= "+ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica().getNumCpf());
				JsfUtil.addSuccessMessage("Arquivo Enviado com Sucesso.");
			}
			else{
				String hora = new SimpleDateFormat("HH:mm:ss").format(new Date()); 
				System.out.println(hora+"- Documento do imóvel rural "+imRur.getIdeImovelRural().toString()+" foi sobrescrito, pelo usuario de CPF= "+ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica().getNumCpf());
				JsfUtil.addSuccessMessage("Arquivo sobrescrito e atualizado com sucesso!");		
			}
			listaArquivo.clear();
			listaArquivo.add(caminhoArquivo);
			} catch (Exception e) {
				erro =e;
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
				JsfUtil.addErrorMessage("O processo de Upload do arquivo não foi concluído, Por favor tente novamente.");
			}finally{
				if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
			}
	}
	
	public void excluirDocumento(){
		if(!Util.isNullOuVazio(ContextoUtil.getContexto().getImovelRural()) && !Util.isNullOuVazio(ContextoUtil.getContexto().getImovelRural().getIdeImovelRural())){
			String hora = new SimpleDateFormat("HH:mm:ss").format(new Date());
			Exception erro = null;
			try {
				imovelRuralService.removerDocumentoProcuracao(ContextoUtil.getContexto().getImovelRural());
				listaArquivo.clear();
				this.caminhoArquivo = "";
				JsfUtil.addSuccessMessage("Arquivo removido com sucesso!");
				System.out.println(hora+"- Documento do imóvel rural "+ContextoUtil.getContexto().getImovelRural().getIdeImovelRural().toString()+" foi removido, pelo usuario de CPF= "+ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica().getNumCpf());
			} catch (Exception e) {
				erro =e;
				JsfUtil.addErrorMessage("Não foi possível excluir o arquivo!");
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			}finally{
				if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
			}
		}
	}

	public String getCaminhoArquivo() {
		return caminhoArquivo;
	}

	public void setCaminhoArquivo(String caminhoArquivo) {
		this.caminhoArquivo = caminhoArquivo;
	}

	public boolean isTemArquivo() {
		return temArquivo;
	}

	public void setTemArquivo(boolean temArquivo) {
		this.temArquivo = temArquivo;
	}

	public List<String> getListaArquivo() {
		return listaArquivo;
	}
	
	public Boolean getListaArquivoIsEmpty() {
		return Util.isNullOuVazio(listaArquivo);
	}

	public void setListaArquivo(List<String> listaArquivo) {
		this.listaArquivo = listaArquivo;
	}

	public Boolean getDisableUploadDoc() {
		return disableUploadDoc;
	}
	
	public Boolean getDisableUploadDocAtualizadoDoContexto() {
		disableUploadDoc = ContextoUtil.getContexto().getDisableUploadDoc();
		return disableUploadDoc;
	}

	public void setDisableUploadDoc(Boolean disableUploadDoc) {
		this.disableUploadDoc = disableUploadDoc;
	}

	public String getLabelUploadAtualizado() {
		labelUpload = ContextoUtil.getContexto().getLabelUpload();
		return labelUpload;
	}
	
	public String getLabelUpload() {
		return labelUpload;
	}

	public void setLabelUpload(String labelUpload) {
		this.labelUpload = labelUpload;
	}

	public StreamedContent getArquivoBaixar() {
		if(!Util.isNullOuVazio(arquivoSelecionado)) {
			Exception erro = null;
			try {
				arquivoBaixar = gerenciaArquivoService.getContentFile(arquivoSelecionado);
			} catch (Exception e) {
				erro =e;
				
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			}finally{
				if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
			}
		}
			return arquivoBaixar;
	}

	public void setArquivoBaixar(StreamedContent arquivoBaixar) {
		this.arquivoBaixar = arquivoBaixar;
	}

	public String getArquivoAExcluir() {
		return arquivoAExcluir;
	}

	public void setArquivoAExcluir(String arquivoAExcluir) {
		this.arquivoAExcluir = arquivoAExcluir;
	}

	public String getArquivoSelecionado() {
		return arquivoSelecionado;
	}

	public void setArquivoSelecionado(String arquivoSelecionado) {
		this.arquivoSelecionado = arquivoSelecionado;
	}

	public TipoVinculoImovel getTipoVinculo() {
		return tipoVinculo;
	}

	public void setTipoVinculo(TipoVinculoImovel tipoVinculo) {
		this.tipoVinculo = tipoVinculo;
	}

	//INEMA
}