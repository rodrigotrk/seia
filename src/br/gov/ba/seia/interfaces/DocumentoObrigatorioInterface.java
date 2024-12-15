package br.gov.ba.seia.interfaces;

import java.util.Date;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.entity.DocumentoAto;
import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.EnquadramentoAtoAmbiental;
import br.gov.ba.seia.entity.Pessoa;

public interface DocumentoObrigatorioInterface {

	public DocumentoObrigatorio getIdeDocumentoObrigatorio();
	public void setIdeDocumentoObrigatorio(DocumentoObrigatorio documentoObrigatorio);
	
	public DocumentoAto getIdeDocumentoAto();
	public void setIdeDocumentoAto(DocumentoAto documentoAto);
	
	public EnquadramentoAtoAmbiental getIdeEnquadramentoAtoAmbiental();
	public void setIdeEnquadramentoAtoAmbiental(EnquadramentoAtoAmbiental enquadramentoAtoAmbiental);
	
	public Boolean getIndDocumentoValidado();
	public void setIndDocumentoValidado(Boolean indDocumentoValidadao);
	
	public Boolean getIndSigiloso();
	public void setIndSigiloso(Boolean indSigiloso);
	
	public String getDscCaminhoArquivo();
	
	public void setIdePessoaValidacao(Pessoa idePessoaValidacao);
	public void setDtcValidacao(Date dtcValidacao);
	
	public String getFileNameDoc();
	public String getTamanhoDoc();
	
	public void setFileUpload(FileUploadEvent fileUpload);
	public void setFile(StreamedContent file);
	public void setFileSize(Long fileSize);
	public Object clone() throws CloneNotSupportedException;
	
	public void setIdePessoaUpload(Pessoa idePessoaUpload);
}
