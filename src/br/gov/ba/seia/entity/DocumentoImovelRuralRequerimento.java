package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import org.primefaces.model.StreamedContent;

@Entity
@Table(name = "documento_imovel_rural_requerimento")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "DocumentoImovelRuralRequerimento.findAll", query = "SELECT d FROM DocumentoImovelRuralRequerimento d"),
		@NamedQuery(name = "DocumentoImovelRuralRequerimento.findByideDocumentoImovelRuralRequerimento", query = "SELECT d FROM DocumentoImovelRuralRequerimento d WHERE d.ideDocumentoImovelRuralRequerimento = :ideDocumentoImovelRuralRequerimento"),
		@NamedQuery(name = "DocumentoImovelRuralRequerimento.remove", query = "DELETE FROM DocumentoImovelRuralRequerimento t WHERE t.ideDocumentoImovelRuralRequerimento = :ideDocumentoImovelRuralRequerimento") })
public class DocumentoImovelRuralRequerimento implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name = "IDE_DOCUMENTO_IMOVEL_RURAL_IDE_REQUERIMENTO_GENERATOR", sequenceName = "ide_documento_imovel_rural_requerimento_seq", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "IDE_DOCUMENTO_IMOVEL_RURAL_IDE_REQUERIMENTO_GENERATOR")
	@Column(name = "ide_documento_imovel_rural_requerimento", unique = true, nullable = false)
	private Integer ideDocumentoImovelRuralRequerimento;
	
	@JoinColumn(name = "ide_requerimento", referencedColumnName = "ide_requerimento")
	@OneToOne(cascade = CascadeType.DETACH)
    private Requerimento ideRequerimento;
	
	@OneToOne
	@JoinColumn(name = "ide_documento_imovel_rural", referencedColumnName = "ide_documento_imovel_rural")
	private DocumentoImovelRural ideDocumentoImovelRural;
	
	@Basic(optional = false)
	@Column(name = "ind_documento_validado")
	private Boolean indDocumentoValidado;
	
	@Column(name = "dtc_validacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcValidacao;
	
	@JoinColumn(name = "ide_pessoa_validacao", referencedColumnName = "ide_pessoa")
	@OneToOne(fetch = FetchType.EAGER)
	private Pessoa idePessoaValidacao;
	
	@Transient
	private StreamedContent file;
	@Transient
	private Long fileSize;

	public DocumentoImovelRuralRequerimento() {
	}
	
	public DocumentoImovelRuralRequerimento(DocumentoImovelRural ideDocumentoImovelRural) {
		this.ideDocumentoImovelRural = ideDocumentoImovelRural;
	}
	
	public DocumentoImovelRuralRequerimento(Integer ideDocumentoImovelRuralRequerimento, String nomDocumentoObrigatorio, String dscCaminhoArquivo) {
		this.ideDocumentoImovelRuralRequerimento = ideDocumentoImovelRuralRequerimento;
	}

	/**
	 * @return the fileSize
	 */
	public Long getFileSize() {
		if(fileSize != null){
			return fileSize;
		}
		return null;
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
	public String getFileSizeFormated() {
		if (this.getFileSize() != null) {
			return Long.valueOf(this.getFileSize() / 1024).toString() + " Kb";
		} else
			return "0 Kb";
	}

	public DocumentoImovelRuralRequerimento(Integer ideDocumentoImovelRuralRequerimento) {
		this.ideDocumentoImovelRuralRequerimento = ideDocumentoImovelRuralRequerimento;
	}

	public DocumentoImovelRuralRequerimento(Integer ideDocumentoImovelRuralRequerimento, Boolean indDocumentoValidado) {
		this.ideDocumentoImovelRuralRequerimento = ideDocumentoImovelRuralRequerimento;
		this.indDocumentoValidado = indDocumentoValidado;
		
	}

	public StreamedContent getFile() {
		return file;
	}

	public void setFile(StreamedContent file) {
		this.file = file;
	}

	@Override
	public String toString() {
		return "DocumentoImovelRuralRequerimento [ideDocumentoImovelRuralRequerimento="
				+ ideDocumentoImovelRuralRequerimento + "]";
	}

	public Integer getIdeDocumentoImovelRuralRequerimento() {
		return ideDocumentoImovelRuralRequerimento;
	}

	public void setIdeDocumentoImovelRuralRequerimento(Integer ideDocumentoImovelRuralRequerimento) {
		this.ideDocumentoImovelRuralRequerimento = ideDocumentoImovelRuralRequerimento;
	}

	public Requerimento getIdeRequerimento() {
		return ideRequerimento;
	}

	public void setIdeRequerimento(Requerimento requerimento) {
		this.ideRequerimento = requerimento;
	}

	public DocumentoImovelRural getIdeDocumentoImovelRural() {
		return ideDocumentoImovelRural;
	}

	public void setIdeDocumentoImovelRural(DocumentoImovelRural ideDocumentoImovelRural) {
		this.ideDocumentoImovelRural = ideDocumentoImovelRural;
	}

	public Boolean getIndDocumentoValidado() {
		return indDocumentoValidado;
	}

	public void setIndDocumentoValidado(Boolean indDocumentoValidado) {
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideDocumentoImovelRural == null) ? 0 : ideDocumentoImovelRural.hashCode());
		result = prime * result + ((ideRequerimento == null) ? 0 : ideRequerimento.hashCode());
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
		DocumentoImovelRuralRequerimento other = (DocumentoImovelRuralRequerimento) obj;
		if (ideDocumentoImovelRural == null) {
			if (other.ideDocumentoImovelRural != null)
				return false;
		} else if (!ideDocumentoImovelRural.equals(other.ideDocumentoImovelRural))
			return false;
		if (ideRequerimento == null) {
			if (other.ideRequerimento != null)
				return false;
		} else if (!ideRequerimento.equals(other.ideRequerimento))
			return false;
		return true;
	}

}
