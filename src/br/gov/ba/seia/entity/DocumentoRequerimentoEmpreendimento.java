package br.gov.ba.seia.entity;

import java.io.File;
import java.util.Date;

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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.util.FileUploadUtil;
import br.gov.ba.seia.util.Util;

@Entity
@Table(name="documento_requerimento_empreendimento")
@NamedQueries({
	@NamedQuery(name = "DocumentoRequerimentoEmpreendimento.removeByIde", query = "DELETE FROM DocumentoRequerimentoEmpreendimento c WHERE c.ideDocumentoRequerimentoEmpreendimento = :ideDocumentoRequerimentoEmpreendimento")

})
public class DocumentoRequerimentoEmpreendimento {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="ide_documento_requerimento_empreendimento_seq")
	@SequenceGenerator(sequenceName="ide_documento_requerimento_empreendimento_seq",name="ide_documento_requerimento_empreendimento_seq", allocationSize=1)
	@Column(name="ide_documento_requerimento_empreendimento", nullable=false)
	private Integer ideDocumentoRequerimentoEmpreendimento;
	
	@Column(name = "nom_documento", nullable = false)
	private String nomDocumento;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="ide_empreendimento_requerimento", referencedColumnName = "ide_empreendimento_requerimento")
	private EmpreendimentoRequerimento ideEmpreendimentoRequerimento;
	
	@Column(name="ind_documento_validado")
	private boolean indDocumentoValidado;
	
	@Column(name="dtc_validacao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcValidacao;
	
	@JoinColumn(name = "ide_pessoa_validacao", referencedColumnName = "ide_pessoa")
	@OneToOne(fetch = FetchType.EAGER)
	private Pessoa idePessoaValidacao;
	
	@Column(name="dsc_caminho_arquivo")
	private String dscCaminhoArquivo;
	
	@Column(name="ide_pessoa_fisica", nullable = false)
	private Integer idePessoaFisica;
	
	@Transient
	private StreamedContent file;
	
	@Transient
	private Long fileSize;
	
	@Transient
	private FileUploadEvent fileUpload;
	
	@Transient
	private String dscCaminhoArquivoAntigo;
	
	public DocumentoRequerimentoEmpreendimento() {
	}

	public Integer getIdeDocumentoRequerimentoEmpreendimento() {
		return ideDocumentoRequerimentoEmpreendimento;
	}

	public void setIdeDocumentoRequerimentoEmpreendimento(
			Integer ideDocumentoRequerimentoEmpreendimento) {
		this.ideDocumentoRequerimentoEmpreendimento = ideDocumentoRequerimentoEmpreendimento;
	}

	public String getNomDocumento() {
		return nomDocumento;
	}

	public void setNomDocumento(String nomDocumento) {
		this.nomDocumento = nomDocumento;
	}

	public EmpreendimentoRequerimento getIdeEmpreendimentoRequerimento() {
		return ideEmpreendimentoRequerimento;
	}

	public void setIdeEmpreendimentoRequerimento(
			EmpreendimentoRequerimento ideEmpreendimentoRequerimento) {
		this.ideEmpreendimentoRequerimento = ideEmpreendimentoRequerimento;
	}

	public boolean isIndDocumentoValidado() {
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

	public Pessoa getIdePessoaValidacao() {
		return idePessoaValidacao;
	}

	public void setIdePessoaValidacao(Pessoa idePessoaValidacao) {
		this.idePessoaValidacao = idePessoaValidacao;
	}

	public String getDscCaminhoArquivo() {
		return dscCaminhoArquivo;
	}

	public void setDscCaminhoArquivo(String dscCaminhoArquivo) {
		this.dscCaminhoArquivo = dscCaminhoArquivo;
	}

	public Integer getIdePessoaFisica() {
		return idePessoaFisica;
	}

	public void setIdePessoaFisica(Integer idePessoaFisica) {
		this.idePessoaFisica = idePessoaFisica;
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

	public String getDscCaminhoArquivoAntigo() {
		return dscCaminhoArquivoAntigo;
	}

	public void setDscCaminhoArquivoAntigo(String dscCaminhoArquivoAntigo) {
		this.dscCaminhoArquivoAntigo = dscCaminhoArquivoAntigo;
	}

	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideDocumentoRequerimentoEmpreendimento == null) ? 0
						: ideDocumentoRequerimentoEmpreendimento.hashCode());
		result = prime * result
				+ ((idePessoaFisica == null) ? 0 : idePessoaFisica.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DocumentoRequerimentoEmpreendimento other = (DocumentoRequerimentoEmpreendimento) obj;
		if (ideDocumentoRequerimentoEmpreendimento == null) {
			if (other.ideDocumentoRequerimentoEmpreendimento != null)
				return false;
		} else if (!ideDocumentoRequerimentoEmpreendimento
				.equals(other.ideDocumentoRequerimentoEmpreendimento))
			return false;
		if (idePessoaFisica == null) {
			if (other.idePessoaFisica != null)
				return false;
		} else if (!idePessoaFisica.equals(other.idePessoaFisica))
			return false;
		return true;
	}

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
	
	public String getFileNameDoc() {
		if (!Util.isNullOuVazio(this.dscCaminhoArquivo)) {
			return FileUploadUtil.getFileName(this.dscCaminhoArquivo);
		}
		return "";
	}
}
