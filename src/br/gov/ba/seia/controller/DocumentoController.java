package br.gov.ba.seia.controller;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Collection;

import javax.activation.MimetypesFileTypeMap;

import org.apache.log4j.Level;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.entity.DocumentoIdentificacao;
import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.DocumentoObrigatorioRequerimento;
import br.gov.ba.seia.entity.DocumentoRepresentacaoRequerimento;
import br.gov.ba.seia.util.FileUploadUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

public class DocumentoController {

	public StreamedContent getFileDownload(DocumentoObrigatorio docObrigatorio) {
		StreamedContent file = null;
		
		Exception erro = null;
		try {
			String caminhoArquivo = docObrigatorio.getDscCaminhoArquivo().trim();
			InputStream stream = new DataInputStream(new BufferedInputStream(new FileInputStream(caminhoArquivo)));
			String mimeType = MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(caminhoArquivo);
			file = new DefaultStreamedContent(stream, mimeType, caminhoArquivo.substring(caminhoArquivo.lastIndexOf(File.separator) + 1));
		} catch (FileNotFoundException e) {
			erro= e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			JsfUtil.addErrorMessage("Erro ao tentar fazer o download do arquivo");
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
		return file;
	}

	public StreamedContent getFile(DocumentoObrigatorioRequerimento doc) {
		StreamedContent file = null;
		
		Exception erro = null;
		
		try {
			String caminhoArquivo = doc.getDscCaminhoArquivo().trim();
			InputStream stream = new DataInputStream(new BufferedInputStream(new FileInputStream(caminhoArquivo)));
			String mimeType = MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(caminhoArquivo);
			file = new DefaultStreamedContent(stream, mimeType, caminhoArquivo.substring(caminhoArquivo.lastIndexOf(File.separator) + 1));
		} catch (Exception e) {
			erro = e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			JsfUtil.addErrorMessage("Erro ao tentar fazer o download do arquivo");
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
		return file;
	}

	public StreamedContent getFileDocumentoIdentificacao(DocumentoIdentificacao doc) {
		StreamedContent file = null;
		Exception erro = null;
		try {
			String caminhoArquivo = doc.getDscCaminhoArquivo().trim();
			InputStream stream = new DataInputStream(new BufferedInputStream(new FileInputStream(caminhoArquivo)));
			String mimeType = MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(caminhoArquivo);
			file = new DefaultStreamedContent(stream, mimeType, caminhoArquivo.substring(caminhoArquivo.lastIndexOf(File.separator) + 1));
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			JsfUtil.addErrorMessage("Erro ao tentar fazer o download do arquivo");
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
		return file;
	}

	public StreamedContent getFileDocumentoRepresentacao(DocumentoRepresentacaoRequerimento doc) {
		StreamedContent file = null;
		Exception erro = null;
		
	
		try {
			String dscCaminhoProcuracao = "";
			
			if(!Util.isNull(doc.getIdeProcuradorRepEmpreendimento())){
				dscCaminhoProcuracao = doc.getIdeProcuradorRepEmpreendimento().getDscCaminhoProcuracao();
			}else{
				dscCaminhoProcuracao = doc.getIdeRepresentanteLegal().getDscCaminhoRepresentacao();
			}
			
			String caminhoArquivo = dscCaminhoProcuracao.trim();
			InputStream stream = new DataInputStream(new BufferedInputStream(new FileInputStream(caminhoArquivo)));
			String mimeType = MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(caminhoArquivo);
			file = new DefaultStreamedContent(stream, mimeType, caminhoArquivo.substring(caminhoArquivo.lastIndexOf(File.separator) + 1));
		} catch (Exception e) {
			erro = e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			JsfUtil.addErrorMessage("Erro ao tentar fazer o download do arquivo");
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
		return file;
	}
	
	public String getFileName(DocumentoObrigatorioRequerimento documento) {
		if (!Util.isNull(documento) && !Util.isNull(documento.getDscCaminhoArquivo())) {
			if (documento.getIsArquivoChanged() != null && documento.getIsArquivoChanged()) {
				return documento.getFile().getName();
			} else {
				int lastIndexOf = documento.getDscCaminhoArquivo().lastIndexOf(File.separator);
				return documento.getDscCaminhoArquivo().substring(lastIndexOf + 1);
			}
		}
		return null;
	}

	public String getFileNameRepresentacao(DocumentoRepresentacaoRequerimento documento) {
		if (!Util.isNull(documento) && !Util.isNull(documento.getDscCaminhoArquivo())) {
			if (documento.getArquivoChanged() != null && documento.getArquivoChanged()) {
				return documento.getFile().getName();
			} else {
				int lastIndexOf = documento.getDscCaminhoArquivo().lastIndexOf(File.separator);
				return documento.getDscCaminhoArquivo().substring(lastIndexOf + 1);
			}
		}
		return null;
	}
	
	public String getFileNameDoc(DocumentoObrigatorioRequerimento documento) {
		if (!Util.isNullOuVazio(documento) && !Util.isNullOuVazio(documento.getDscCaminhoArquivo())) {
			if (!Util.isNullOuVazio(documento.getIsArquivoChanged()) && documento.getIsArquivoChanged()) {
				return FileUploadUtil.getFileName(documento.getFile().getName());
			} else {
				return FileUploadUtil.getFileName(documento.getDscCaminhoArquivo());
			}
		} else {
			return "";
		}
	}	
	public String getTamanhoDoc(DocumentoObrigatorioRequerimento documento) {
		File arquivo = null;
		if (!Util.isNull(documento.getDscCaminhoArquivo())) {
			arquivo = new File(documento.getDscCaminhoArquivo());
			if (!Util.isNullOuVazio(arquivo)) {
				return Long.valueOf(arquivo.length() / 1024).toString() + " Kb";
			}
		}
		return "";
	}
	

	public String getFileNameDocumentoIdentificacao(DocumentoIdentificacao documento) {
		return getFileNameDocumento(documento.getDscCaminhoArquivo());
	}
	
	public String getFileNameDocumentoRepresentacao(DocumentoRepresentacaoRequerimento documento) {
		return getFileNameDocumento(documento.getDscCaminhoArquivo());
	}
	
	public String getFileNameDocumento(String caminhoArquivo){
		if (!Util.isNullOuVazio(caminhoArquivo)) {
			return FileUploadUtil.getFileName(caminhoArquivo);
		} else {
			return "";
		}
	}
	
	public boolean isExibirLista(Collection<DocumentoObrigatorioRequerimento> docs){
		return !Util.isNullOuVazio(docs);
	}
	public String mostrarTamanhoArquivoDocumentoIdentificacao(DocumentoIdentificacao documentoIdentificacao) {
		return mostrarTamanhoArquivoDocumento(documentoIdentificacao.getDscCaminhoArquivo());
	}
	public String mostrarTamanhoArquivoDocumentoRepresentacao(DocumentoRepresentacaoRequerimento documentoIdentificacao) {
		return mostrarTamanhoArquivoDocumento(documentoIdentificacao.getDscCaminhoArquivo());
	}
	
	public String mostrarTamanhoArquivoDocumento(String caminhoArquivo){
		File arquivo = null;
		if (!Util.isNull(caminhoArquivo)) {
			arquivo = new File(caminhoArquivo);
			if (!Util.isNullOuVazio(arquivo)) {
				return Long.valueOf(arquivo.length() / 1024).toString() + " Kb";
			}
		}
		return "";
	}
	
}
