package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.util.Util;

/**
 * @author MJunior
 */
@Entity
@Table(name = "documento_identificacao_requerimento")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "DocumentoIdentificacaoRequerimento.findAll", query = "SELECT d FROM DocumentoIdentificacaoRequerimento d"),
		@NamedQuery(name = "DocumentoIdentificacaoRequerimento.findByIdeDocumentoIdentificacaoRequerimento", query = "SELECT d FROM DocumentoIdentificacaoRequerimento d WHERE d.ideDocumentoIdentificacaoRequerimento = :ideDocumentoIdentificacaoRequerimento"),
		@NamedQuery(name = "DocumentoIdentificacaoRequerimento.findByIndDocumentoValidado", query = "SELECT d FROM DocumentoIdentificacaoRequerimento d WHERE d.indDocumentoValidado = :indDocumentoValidado"),
		@NamedQuery(name = "DocumentoIdentificacaoRequerimento.findByDtcValidacao", query = "SELECT d FROM DocumentoIdentificacaoRequerimento d WHERE d.dtcValidacao = :dtcValidacao") })
public class DocumentoIdentificacaoRequerimento implements Serializable,DocumentoValidacao {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Basic(optional = false)
	@Column(name = "ide_documento_identificacao_requerimento", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "IDE_DOCUMENTO_IDENTIFICACAO_REQUERIMENTO_seq")
	@SequenceGenerator(name = "IDE_DOCUMENTO_IDENTIFICACAO_REQUERIMENTO_seq", sequenceName = "IDE_DOCUMENTO_IDENTIFICACAO_REQUERIMENTO_seq", allocationSize = 1)
	private Integer ideDocumentoIdentificacaoRequerimento;
	
	@Basic(optional = false)
	@Column(name = "ind_documento_validado", nullable = false)
	private boolean indDocumentoValidado;
	
	@Column(name = "dtc_validacao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcValidacao;
	
	@JoinColumn(name = "ide_requerimento", referencedColumnName = "ide_requerimento", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Requerimento ideRequerimento;
	
	@JoinColumn(name = "ide_pessoa_juridica", referencedColumnName = "ide_pessoa_juridica")
	@ManyToOne(fetch = FetchType.LAZY)
	private PessoaJuridica idePessoaJuridica;
	
	@JoinColumn(name = "ide_pessoa_validacao", referencedColumnName = "ide_pessoa")
	@ManyToOne(fetch = FetchType.LAZY)
	private Pessoa idePessoaValidacao;
	
	@JoinColumn(name = "ide_documento_identificacao", referencedColumnName = "ide_documento_identificacao")
	@ManyToOne(fetch = FetchType.LAZY)
	private DocumentoIdentificacao ideDocumentoIdentificacao;
	
	@Column(name = "dsc_caminho_arquivo", length = 1000)
	private String dscCaminhoArquivo;
	
	@Transient
	private StreamedContent file;
	
	@Transient
	private Long fileSize;
	
	@Transient
	private Boolean arquivoChanged;
	
	@Transient
	private FileUploadEvent fileUpload;
	
	@Transient
	private String caminhoArquivoAnterior;

	public DocumentoIdentificacaoRequerimento() {}
	
	public String getDscCaminhoArquivoNome() {
		if (!Util.isNullOuVazio(dscCaminhoArquivo)){
			return dscCaminhoArquivo.substring(dscCaminhoArquivo.lastIndexOf("/")+1);
		}
		return null;
	}

	/**
	 * @return the fileSize
	 */
	public String getFileSizeFormated() {
		if (this.getFileSize() != null) {
			return Long.valueOf(this.getFileSize() / 1024).toString() + " Kb";
		} else
			return "0 Kb";
	}

	@Override
	public String getDescricao() {
		return this.ideDocumentoIdentificacao.getIdeTipoIdentificacao().getNomTipoIdentificacao();
	}
	
	public DocumentoIdentificacaoRequerimento(Integer ideDocumentoIdentificacaoRequerimento) {
		this.ideDocumentoIdentificacaoRequerimento = ideDocumentoIdentificacaoRequerimento;
	}

	public DocumentoIdentificacaoRequerimento(Integer ideDocumentoIdentificacaoRequerimento, boolean indDocumentoValidado) {
		this.ideDocumentoIdentificacaoRequerimento = ideDocumentoIdentificacaoRequerimento;
		this.indDocumentoValidado = indDocumentoValidado;
	}

	public Integer getIdeDocumentoIdentificacaoRequerimento() {
		return ideDocumentoIdentificacaoRequerimento;
	}

	public void setIdeDocumentoIdentificacaoRequerimento(Integer ideDocumentoIdentificacaoRequerimento) {
		this.ideDocumentoIdentificacaoRequerimento = ideDocumentoIdentificacaoRequerimento;
	}

	public boolean getIndDocumentoValidado() {
		return indDocumentoValidado;
	}

	public void setIndDocumentoValidado(boolean indDocumentoValidado) {
		this.indDocumentoValidado = indDocumentoValidado;
	}

	public Date getDtcValidacao() {
		return dtcValidacao;
	}

	public void setDtcValidacao(Date dtcValidacao) {
		this.dtcValidacao = dtcValidacao;
	}

	public Requerimento getIdeRequerimento() {
		return ideRequerimento;
	}

	public void setIdeRequerimento(Requerimento ideRequerimento) {
		this.ideRequerimento = ideRequerimento;
	}

	public PessoaJuridica getIdePessoaJuridica() {
		return idePessoaJuridica;
	}

	public void setIdePessoaJuridica(PessoaJuridica idePessoaJuridica) {
		this.idePessoaJuridica = idePessoaJuridica;
	}

	public Pessoa getIdePessoaValidacao() {
		return idePessoaValidacao;
	}

	public void setIdePessoaValidacao(Pessoa idePessoaValidacao) {
		this.idePessoaValidacao = idePessoaValidacao;
	}

	public DocumentoIdentificacao getIdeDocumentoIdentificacao() {
		return ideDocumentoIdentificacao;
	}

	public void setIdeDocumentoIdentificacao(DocumentoIdentificacao ideDocumentoIdentificacao) {
		this.ideDocumentoIdentificacao = ideDocumentoIdentificacao;
	}

	public Boolean getArquivoChanged() {
		return arquivoChanged;
	}

	public void setArquivoChanged(Boolean arquivoChanged) {
		this.arquivoChanged = arquivoChanged;
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

	public FileUploadEvent getFileUpload() {
		return fileUpload;
	}

	public void setFileUpload(FileUploadEvent fileUpload) {
		this.fileUpload = fileUpload;
	}

	public String getCaminhoArquivoAnterior() {
		return caminhoArquivoAnterior;
	}

	public void setCaminhoArquivoAnterior(String caminhoArquivoAnterior) {
		this.caminhoArquivoAnterior = caminhoArquivoAnterior;
	}

	public String getDscCaminhoArquivo() {
		if (!Util.isNullOuVazio(this.dscCaminhoArquivo)) {
			return dscCaminhoArquivo;
		}
		if (!Util.isNullOuVazio(this.ideDocumentoIdentificacao)) {
			return this.ideDocumentoIdentificacao.getDscCaminhoArquivo();
		}
		return dscCaminhoArquivo;
	}

	public void setDscCaminhoArquivo(String dscCaminhoArquivo) {
		this.dscCaminhoArquivo = dscCaminhoArquivo;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (ideDocumentoIdentificacaoRequerimento != null ? ideDocumentoIdentificacaoRequerimento.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		
		if (!(object instanceof DocumentoIdentificacaoRequerimento)) {
			return false;
		}
		DocumentoIdentificacaoRequerimento other = (DocumentoIdentificacaoRequerimento) object;
		if ((this.ideDocumentoIdentificacaoRequerimento == null && other.ideDocumentoIdentificacaoRequerimento != null)
				|| (this.ideDocumentoIdentificacaoRequerimento != null && !this.ideDocumentoIdentificacaoRequerimento.equals(other.ideDocumentoIdentificacaoRequerimento))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "br.gov.ba.seia.entity.DocumentoIdentificacaoRequerimento[ ideDocumentoIdentificacaoRequerimento=" + ideDocumentoIdentificacaoRequerimento + " ]";
	}
}
