package br.gov.ba.seia.entity;

import java.io.File;
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

import br.gov.ba.seia.interfaces.DocumentoObrigatorioInterface;
import br.gov.ba.seia.util.FileUploadUtil;
import br.gov.ba.seia.util.Util;
import flexjson.JSON;

@Entity
@Table(name = "documento_obrigatorio_requerimento")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "DocumentoObrigatorioRequerimento.findAll", query = "SELECT d FROM DocumentoObrigatorioRequerimento d"),
	@NamedQuery(name = "DocumentoObrigatorioRequerimento.findByIdeDocumentoObrigatorioRequerimento", query = "SELECT d FROM DocumentoObrigatorioRequerimento d WHERE d.ideDocumentoObrigatorioRequerimento = :ideDocumentoObrigatorioRequerimento"),
	@NamedQuery(name = "DocumentoObrigatorioRequerimento.findByIdeRequerimento", query = "SELECT d FROM DocumentoObrigatorioRequerimento d WHERE d.ideRequerimento = :ideRequerimento"),
	@NamedQuery(name = "DocumentoObrigatorioRequerimento.findByDscCaminhoArquivo", query = "SELECT d FROM DocumentoObrigatorioRequerimento d WHERE d.dscCaminhoArquivo = :dscCaminhoArquivo"),
	@NamedQuery(name = "DocumentoObrigatorioRequerimento.findByIndDocumentoValidado", query = "SELECT d FROM DocumentoObrigatorioRequerimento d WHERE d.indDocumentoValidado = :indDocumentoValidado"),
	@NamedQuery(name = "DocumentoObrigatorioRequerimento.atualizarEnquadramentoByRequerimento", query = "UPDATE DocumentoObrigatorioRequerimento d SET d.ideEnquadramentoAtoAmbiental = :ideEnquadramentoAtoAmbiental WHERE d.ideRequerimento = :ideRequerimento"),
	@NamedQuery(name = "DocumentoObrigatorioRequerimento.findByDtcValidacao", query = "SELECT d FROM DocumentoObrigatorioRequerimento d WHERE d.dtcValidacao = :dtcValidacao") })
public class DocumentoObrigatorioRequerimento implements Serializable, Cloneable, DocumentoValidacao, DocumentoObrigatorioInterface {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "IDE_DOCUMENTO_OBRIGATORIO_REQUERIMENTO_GENERATOR", sequenceName = "IDE_DOCUMENTO_OBRIGATORIO_REQUERIMENTO_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "IDE_DOCUMENTO_OBRIGATORIO_REQUERIMENTO_GENERATOR")
	@Basic(optional = false)
	@Column(name = "ide_documento_obrigatorio_requerimento", nullable = false)
	private Integer ideDocumentoObrigatorioRequerimento;

	@Column(name = "dsc_caminho_arquivo", length = 1000)
	private String dscCaminhoArquivo;

	@Basic(optional = false)
	@Column(name = "ind_documento_validado", nullable = false)
	private Boolean indDocumentoValidado;

	@Column(name = "ind_sigiloso", nullable = false)
	private boolean indSigiloso;

	@Column(name = "dtc_validacao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcValidacao;

	@JoinColumn(name = "ide_requerimento", referencedColumnName = "ide_requerimento", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Requerimento ideRequerimento;

	@JoinColumn(name = "ide_pessoa_upload", referencedColumnName = "ide_pessoa")
	@ManyToOne(fetch = FetchType.LAZY)
	private Pessoa idePessoaUpload;

	@JoinColumn(name = "ide_pessoa_validacao", referencedColumnName = "ide_pessoa")
	@ManyToOne(fetch = FetchType.LAZY)
	private Pessoa idePessoaValidacao;

	@JoinColumn(name = "ide_documento_ato", referencedColumnName = "ide_documento_ato", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private DocumentoAto ideDocumentoAto;

	@JoinColumn(name = "ide_documento_obrigatorio", referencedColumnName = "ide_documento_obrigatorio", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private DocumentoObrigatorio ideDocumentoObrigatorio;

	@JoinColumn(name = "ide_enquadramento_ato_ambiental", referencedColumnName = "ide_enquadramento_ato_ambiental")
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private EnquadramentoAtoAmbiental ideEnquadramentoAtoAmbiental;

	@Transient
	private AtoAmbiental atoAmbiental;

	@Transient
	private String tipoDocumento;

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

	@Transient
	private Boolean isArquivoChanged;

	public DocumentoObrigatorioRequerimento() {}

	public DocumentoObrigatorioRequerimento(Requerimento requerimento){
		this.ideRequerimento = requerimento;
	}

	public DocumentoObrigatorioRequerimento(Integer ideDocumentoObrigatorioRequerimento) {
		this.ideDocumentoObrigatorioRequerimento = ideDocumentoObrigatorioRequerimento;
	}

	public DocumentoObrigatorioRequerimento(Integer ideDocumentoObrigatorioRequerimento, String dscCaminhoArquivo,
			Boolean indDocumentoValidado) {
		this.ideDocumentoObrigatorioRequerimento = ideDocumentoObrigatorioRequerimento;
		this.dscCaminhoArquivo = dscCaminhoArquivo;
		this.indDocumentoValidado = indDocumentoValidado;
	}

	public DocumentoObrigatorioRequerimento(FileUploadEvent fileUploadEvent, Requerimento requerimento, DocumentoObrigatorio docObrigatorio) {
		this.fileUpload = fileUploadEvent;
		this.fileSize = fileUploadEvent.getFile().getSize();
		this.ideDocumentoObrigatorio = docObrigatorio;
		this.ideRequerimento = requerimento;
		this.indDocumentoValidado = false;
		this.indSigiloso = false;
	}

	@JSON(include = false)
	public Integer getIdeDocumentoObrigatorioRequerimento() {
		return ideDocumentoObrigatorioRequerimento;
	}

	public void setIdeDocumentoObrigatorioRequerimento(Integer ideDocumentoObrigatorioRequerimento) {
		this.ideDocumentoObrigatorioRequerimento = ideDocumentoObrigatorioRequerimento;
	}

	@JSON(include = false)
	public String getDscCaminhoArquivo() {
		return dscCaminhoArquivo;
	}

	@JSON(include = false)
	public String getDscCaminhoArquivoNome() {
		return dscCaminhoArquivo.substring(dscCaminhoArquivo.lastIndexOf("/")+1);
	}

	public void setDscCaminhoArquivo(String dscCaminhoArquivo) {
		this.dscCaminhoArquivo = dscCaminhoArquivo;
	}

	@JSON(include = false)
	public Boolean getIndDocumentoValidado() {
		return indDocumentoValidado;
	}

	public void setIndDocumentoValidado(Boolean indDocumentoValidado) {
		this.indDocumentoValidado = indDocumentoValidado;
	}

	@JSON(include = false)
	public Date getDtcValidacao() {
		return dtcValidacao;
	}

	public void setDtcValidacao(Date dtcValidacao) {
		this.dtcValidacao = dtcValidacao;
	}

	@JSON(include = false)
	public Requerimento getIdeRequerimento() {
		return ideRequerimento;
	}

	public void setIdeRequerimento(Requerimento ideRequerimento) {
		this.ideRequerimento = ideRequerimento;
	}

	@JSON(include = false)
	public Pessoa getIdePessoaUpload() {
		return idePessoaUpload;
	}

	public void setIdePessoaUpload(Pessoa idePessoaUpload) {
		this.idePessoaUpload = idePessoaUpload;
	}

	@JSON(include = false)
	public Pessoa getIdePessoaValidacao() {
		return idePessoaValidacao;
	}

	public void setIdePessoaValidacao(Pessoa idePessoaValidacao) {
		this.idePessoaValidacao = idePessoaValidacao;
	}

	/**
	 * @return the tipoDocumento
	 */
	@JSON(include = false)
	public String getTipoDocumento() {
		return tipoDocumento;
	}

	/**
	 * @param tipoDocumento
	 *            the tipoDocumento to set
	 */
	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	/**
	 * @return the fileSize
	 */
	@JSON(include = false)
	public Long getFileSize() {
		if (Util.isNullOuVazio(fileSize)) {
			File file = new File(this.dscCaminhoArquivo);
			setFileSize(file.length());
		}
		return fileSize;
	}

	/**
	 * @param fileSize
	 *            the fileSize to set
	 */
	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	/**
	 * @return the fileSize
	 */
	@JSON(include = false)
	public String getFileSizeFormated() {
		if (this.getFileSize() != null) {
			return Long.valueOf(this.getFileSize() / 1024).toString() + " Kb";
		} else {
			return "0 Kb";
		}
	}

	/**
	 * @return the arquivoChanged
	 */
	@JSON(include = false)
	public Boolean getArquivoChanged() {
		return arquivoChanged;
	}

	/**
	 * @param arquivoChanged
	 *            the arquivoChanged to set
	 */
	public void setArquivoChanged(Boolean arquivoChanged) {
		this.arquivoChanged = arquivoChanged;
	}

	@JSON(include = false)
	public StreamedContent getFile() {
		return file;
	}

	public void setFile(StreamedContent file) {
		this.file = file;
	}

	@JSON(include = false)
	public FileUploadEvent getFileUpload() {
		return fileUpload;
	}

	public void setFileUpload(FileUploadEvent fileUpload) {
		this.fileUpload = fileUpload;
	}

	@JSON(include = false)
	public String getCaminhoArquivoAnterior() {
		return caminhoArquivoAnterior;
	}

	public void setCaminhoArquivoAnterior(String caminhoArquivoAnterior) {
		this.caminhoArquivoAnterior = caminhoArquivoAnterior;
	}

	@JSON(include = false)
	public AtoAmbiental getAtoAmbiental() {
		return atoAmbiental;
	}

	public void setAtoAmbiental(AtoAmbiental atoAmbiental) {
		this.atoAmbiental = atoAmbiental;
	}

	@JSON(include = false)
	public Boolean getIsArquivoChanged() {
		return isArquivoChanged;
	}

	public void setIsArquivoChanged(Boolean isArquivoChanged) {
		this.isArquivoChanged = isArquivoChanged;
	}

	@JSON(include = false)
	public DocumentoAto getIdeDocumentoAto() {
		return ideDocumentoAto;
	}

	public void setIdeDocumentoAto(DocumentoAto ideDocumentoAto) {
		this.ideDocumentoAto = ideDocumentoAto;
	}

	@JSON(include = false)
	public Boolean getIndSigiloso() {
		return indSigiloso;
	}

	public void setIndSigiloso(Boolean indSigiloso) {
		this.indSigiloso = indSigiloso;
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
	public String getDescricao() {
		return this.ideDocumentoAto.getIdeDocumentoObrigatorio().getNomDocumentoObrigatorio();
	}

	@Override
	@JSON(include = false)
	public DocumentoObrigatorioRequerimento clone() throws CloneNotSupportedException {
		return (DocumentoObrigatorioRequerimento) super.clone();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideDocumentoAto == null) ? 0 : ideDocumentoAto.hashCode());
		result = prime * result + ((ideRequerimento == null) ? 0 : ideRequerimento.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		DocumentoObrigatorioRequerimento other = (DocumentoObrigatorioRequerimento) obj;
		if (ideDocumentoAto == null) {
			if (other.ideDocumentoAto != null) {
				return false;
			}
		} else if (!ideDocumentoAto.equals(other.ideDocumentoAto)) {
			return false;
		}
		if (ideRequerimento == null) {
			if (other.ideRequerimento != null) {
				return false;
			}
		} else if (!ideRequerimento.equals(other.ideRequerimento)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return String.valueOf(ideDocumentoObrigatorioRequerimento);
	}

	@JSON(include = false)
	public DocumentoObrigatorio getIdeDocumentoObrigatorio() {
		return ideDocumentoObrigatorio;
	}

	public void setIdeDocumentoObrigatorio(DocumentoObrigatorio ideDocumentoObrigatorio) {
		this.ideDocumentoObrigatorio = ideDocumentoObrigatorio;
	}

	public EnquadramentoAtoAmbiental getIdeEnquadramentoAtoAmbiental() {
		return ideEnquadramentoAtoAmbiental;
	}

	public void setIdeEnquadramentoAtoAmbiental(
			EnquadramentoAtoAmbiental ideEnquadramentoAtoAmbiental) {
		this.ideEnquadramentoAtoAmbiental = ideEnquadramentoAtoAmbiental;
	}
}
