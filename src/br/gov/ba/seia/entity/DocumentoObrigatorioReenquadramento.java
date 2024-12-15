package br.gov.ba.seia.entity;

import java.io.File;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.interfaces.DocumentoObrigatorioInterface;
import br.gov.ba.seia.util.FileUploadUtil;
import br.gov.ba.seia.util.Util;
import flexjson.JSON;

/**
 * The persistent class for the documento_obrigatorio_reenquadramento database table.
 * 
 */
@Entity
@Table(name="documento_obrigatorio_reenquadramento")
@NamedQuery(name="DocumentoObrigatorioReenquadramento.findAll", query="SELECT d FROM DocumentoObrigatorioReenquadramento d")
public class DocumentoObrigatorioReenquadramento implements Serializable, Cloneable, DocumentoValidacao, DocumentoObrigatorioInterface  {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="documento_obrigatorio_reenquadramento_generator", sequenceName="documento_obrigatorio_reenquadramento_seq", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="documento_obrigatorio_reenquadramento_generator")
	@Column(name="ide_documento_obrigatorio_reenquadramento")
	private Integer ideDocumentoObrigatorioReenquadramento;

	@Column(name="dsc_caminho_arquivo", length = 1000)
	private String dscCaminhoArquivo;

	@Column(name = "dtc_validacao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcValidacao;

	@JoinColumn(name = "ide_documento_ato", referencedColumnName = "ide_documento_ato")
	@ManyToOne(fetch = FetchType.LAZY)
	private DocumentoAto ideDocumentoAto;

	@JoinColumn(name = "ide_documento_obrigatorio", referencedColumnName = "ide_documento_obrigatorio")
	@ManyToOne(fetch = FetchType.LAZY)
	private DocumentoObrigatorio ideDocumentoObrigatorio;

	@JoinColumn(name = "ide_enquadramento_ato_ambiental", referencedColumnName = "ide_enquadramento_ato_ambiental")
	@ManyToOne(fetch = FetchType.LAZY)
	private EnquadramentoAtoAmbiental ideEnquadramentoAtoAmbiental;

	@JoinColumn(name = "ide_pessoa_upload", referencedColumnName = "ide_pessoa")
	@ManyToOne(fetch = FetchType.LAZY)
	private Pessoa idePessoaUpload;

	@JoinColumn(name = "ide_pessoa_validacao", referencedColumnName = "ide_pessoa")
	@ManyToOne(fetch = FetchType.LAZY)
	private Pessoa idePessoaValidacao;

	@JoinColumn(name = "ide_processo_reenquadramento", referencedColumnName = "ide_processo_reenquadramento")
	@ManyToOne(fetch = FetchType.LAZY)
	private ProcessoReenquadramento ideProcessoReenquadramento;

	@Column(name="ind_documento_validado")
	private Boolean indDocumentoValidado;

	@Column(name="ind_sigiloso")
	private Boolean indSigiloso;

	@Transient
	private FileUploadEvent fileUpload;
	
	@Transient
	private StreamedContent file;
	
	@Transient
	private Long fileSize;
	
	public DocumentoObrigatorioReenquadramento() {
	}

	public Integer getIdeDocumentoObrigatorioReenquadramento() {
		return ideDocumentoObrigatorioReenquadramento;
	}

	public void setIdeDocumentoObrigatorioReenquadramento(
			Integer ideDocumentoObrigatorioReenquadramento) {
		this.ideDocumentoObrigatorioReenquadramento = ideDocumentoObrigatorioReenquadramento;
	}

	public String getDscCaminhoArquivo() {
		return dscCaminhoArquivo;
	}

	public void setDscCaminhoArquivo(String dscCaminhoArquivo) {
		this.dscCaminhoArquivo = dscCaminhoArquivo;
	}

	public Date getDtcValidacao() {
		return dtcValidacao;
	}

	public void setDtcValidacao(Date dtcValidacao) {
		this.dtcValidacao = dtcValidacao;
	}

	public DocumentoAto getIdeDocumentoAto() {
		return ideDocumentoAto;
	}

	public void setIdeDocumentoAto(DocumentoAto ideDocumentoAto) {
		this.ideDocumentoAto = ideDocumentoAto;
	}

	public DocumentoObrigatorio getIdeDocumentoObrigatorio() {
		return ideDocumentoObrigatorio;
	}

	public void setIdeDocumentoObrigatorio(
			DocumentoObrigatorio ideDocumentoObrigatorio) {
		this.ideDocumentoObrigatorio = ideDocumentoObrigatorio;
	}

	public EnquadramentoAtoAmbiental getIdeEnquadramentoAtoAmbiental() {
		return ideEnquadramentoAtoAmbiental;
	}

	public void setIdeEnquadramentoAtoAmbiental(
			EnquadramentoAtoAmbiental ideEnquadramentoAtoAmbiental) {
		this.ideEnquadramentoAtoAmbiental = ideEnquadramentoAtoAmbiental;
	}

	public Pessoa getIdePessoaUpload() {
		return idePessoaUpload;
	}

	public void setIdePessoaUpload(Pessoa idePessoaUpload) {
		this.idePessoaUpload = idePessoaUpload;
	}

	public Pessoa getIdePessoaValidacao() {
		return idePessoaValidacao;
	}

	public void setIdePessoaValidacao(Pessoa idePessoaValidacao) {
		this.idePessoaValidacao = idePessoaValidacao;
	}

	public ProcessoReenquadramento getIdeProcessoReenquadramento() {
		return ideProcessoReenquadramento;
	}

	public void setIdeProcessoReenquadramento(
			ProcessoReenquadramento ideProcessoReenquadramento) {
		this.ideProcessoReenquadramento = ideProcessoReenquadramento;
	}

	public Boolean getIndDocumentoValidado() {
		return indDocumentoValidado;
	}

	public void setIndDocumentoValidado(Boolean indDocumentoValidado) {
		this.indDocumentoValidado = indDocumentoValidado;
	}

	public Boolean getIndSigiloso() {
		return indSigiloso;
	}

	public void setIndSigiloso(Boolean indSigiloso) {
		this.indSigiloso = indSigiloso;
	}

	@Override
	public String getDescricao() {
		return this.ideDocumentoObrigatorio.getNomDocumentoObrigatorio();
	}
	
	public FileUploadEvent getFileUpload() {
		return fileUpload;
	}

	public void setFileUpload(FileUploadEvent fileUpload) {
		this.fileUpload = fileUpload;
	}

	public StreamedContent getFile() {
		return file;
	}

	public void setFile(StreamedContent file) {
		this.file = file;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	@JSON(include = false)
	public String getFileNameDoc() {
		if (!Util.isNullOuVazio(this.dscCaminhoArquivo)) {
			return FileUploadUtil.getFileName(this.dscCaminhoArquivo);
		}
		return "";
	}

	@JSON(include = false)
	public String getTamanhoDoc() {
		File arquivo = null;
		if (!Util.isNull(this.dscCaminhoArquivo)) {
			arquivo = new File(this.dscCaminhoArquivo);
			if (!Util.isNullOuVazio(arquivo)) {
				return Long.valueOf(arquivo.length() / 1024).toString() + " Kb";
			}
		}
		return "";
	}
	
	@Override
	@JSON(include = false)
	public Object clone() throws CloneNotSupportedException {
		return (DocumentoObrigatorioReenquadramento) super.clone();
	}
}