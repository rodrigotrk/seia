/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.Hibernate;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum;
import br.gov.ba.seia.interfaces.BaseEntity;
import br.gov.ba.seia.util.Util;
import flexjson.JSON;

@Entity
@Table(name = "documento_obrigatorio")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "DocumentoObrigatorio.findAll", query = "SELECT d FROM DocumentoObrigatorio d"),
		@NamedQuery(name = "DocumentoObrigatorio.findByIdeDocumentoObrigatorio", query = "SELECT d FROM DocumentoObrigatorio d WHERE d.ideDocumentoObrigatorio = :ideDocumentoObrigatorio"),
		@NamedQuery(name = "DocumentoObrigatorio.findByNomDocumentoObrigatorio", query = "SELECT d FROM DocumentoObrigatorio d WHERE d.nomDocumentoObrigatorio = :nomDocumentoObrigatorio"),
		@NamedQuery(name = "DocumentoObrigatorio.findByNumTamanho", query = "SELECT d FROM DocumentoObrigatorio d WHERE d.numTamanho = :numTamanho"),
		@NamedQuery(name = "DocumentoObrigatorio.findByIndFormulario", query = "SELECT d FROM DocumentoObrigatorio d WHERE d.indFormulario = :indFormulario"),
		@NamedQuery(name = "DocumentoObrigatorio.findByDscCaminhoArquivo", query = "SELECT d FROM DocumentoObrigatorio d WHERE d.dscCaminhoArquivo = :dscCaminhoArquivo"),
		@NamedQuery(name = "DocumentoObrigatorio.remove", query = "DELETE FROM DocumentoObrigatorio t WHERE t.ideDocumentoObrigatorio = :ideDocumentoObrigatorio") })
@NamedNativeQuery(name = "DocumentoObrigatorio.inserirEnquadramentoDocumento", query = "insert into enquadramento_documento_obrigatorio(ide_documento_obrigatorio,ide_enquadramento) values(:ideDocumentoObrigatorio,:ideEnquadramento)", resultClass = DocumentoObrigatorio.class)
public class DocumentoObrigatorio implements Serializable, BaseEntity {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name = "DOCUMENTO_OBRIGATORIO_IDEDOCUMENTOOBRIGATORIO_GENERATOR", sequenceName = "DOCUMENTO_OBRIGATORIO_IDE_DOCUMENTO_OBRIGATORIO_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DOCUMENTO_OBRIGATORIO_IDEDOCUMENTOOBRIGATORIO_GENERATOR")
	@Column(name = "ide_documento_obrigatorio", unique = true, nullable = false)
	private Integer ideDocumentoObrigatorio;
	
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 1000)
	@Column(name = "nom_documento_obrigatorio", nullable = false)
	private String nomDocumentoObrigatorio;
	
	// @Max(value=?) @Min(value=?)//if you know range of your decimal fields
	// consider using these annotations to enforce field validation
	@Basic(optional = false)
	@NotNull
	@Column(name = "num_tamanho", nullable = false, precision = 10, scale = 3)
	private BigDecimal numTamanho;
	
	@Basic(optional = false)
	@NotNull
	@Column(name = "ind_formulario", nullable = false)
	private boolean indFormulario;
	
	@Column(name = "ind_formulario_digital", nullable = false)
	private boolean indFormularioDigital;
	
	@Column(name = "ind_publico", nullable = false)
	private Boolean indPublico;
	
	@Column(name = "ind_ativo", nullable = false)
	private Boolean indAtivo;
	
	@Size(max = 1000)
	@Column(name = "dsc_caminho_arquivo", length = 1000)
	private String dscCaminhoArquivo;
	
	@JoinTable(name = "enquadramento_documento_obrigatorio", joinColumns = { @JoinColumn(name = "ide_documento_obrigatorio", referencedColumnName = "ide_documento_obrigatorio", nullable = false) }, inverseJoinColumns = { @JoinColumn(name = "ide_enquadramento", referencedColumnName = "ide_enquadramento", nullable = false) })
	@ManyToMany(fetch = FetchType.LAZY)
	private Collection<Enquadramento> enquadramentoCollection;
	
	@JoinColumn(name = "ide_tipo_documento_obrigatorio", referencedColumnName = "ide_tipo_documento_obrigatorio")
	@OneToOne(fetch = FetchType.EAGER)
	private TipoDocumentoObrigatorio ideTipoDocumentoObrigatorio;
	
	@OneToMany(mappedBy = "ideDocumentoObrigatorio", fetch = FetchType.LAZY)
	private Collection<DocumentoAto> documentoAtoCollection;
	
	@OneToMany(mappedBy = "ideDocumentoObrigatorio", fetch = FetchType.LAZY)
	private Collection<DocumentoObrigatorioRequerimento> documentoObrigatorioRequerimentoCollection;
	
	@Transient
	private StreamedContent file;
	
	@Transient
	private Long fileSize;

	public DocumentoObrigatorio() {
		enquadramentoCollection = new ArrayList<Enquadramento>();
		documentoAtoCollection = new ArrayList<DocumentoAto>();
		documentoObrigatorioRequerimentoCollection = new ArrayList<DocumentoObrigatorioRequerimento>();
	}

	public DocumentoObrigatorio(Integer ideDocumentoObrigatorio, String nomDocumentoObrigatorio, String dscCaminhoArquivo) {
		this.ideDocumentoObrigatorio = ideDocumentoObrigatorio;
		this.nomDocumentoObrigatorio = nomDocumentoObrigatorio;
		this.dscCaminhoArquivo = dscCaminhoArquivo;
	}

	public DocumentoObrigatorio(Integer ideDocumentoObrigatorio) {
		this.ideDocumentoObrigatorio = ideDocumentoObrigatorio;
	}

	public DocumentoObrigatorio(Integer ideDocumentoObrigatorio, String nomDocumentoObrigatorio, BigDecimal numTamanho, boolean indFormulario) {
		this.ideDocumentoObrigatorio = ideDocumentoObrigatorio;
		this.nomDocumentoObrigatorio = nomDocumentoObrigatorio;
		this.numTamanho = numTamanho;
		this.indFormulario = indFormulario;
	}

	public DocumentoObrigatorio(Integer ideDocumentoObrigatorio, String nomDocumentoObrigatorio) {
		this.ideDocumentoObrigatorio = ideDocumentoObrigatorio;
		this.nomDocumentoObrigatorio = nomDocumentoObrigatorio;
	}

	public Integer getIdeDocumentoObrigatorio() {
		return ideDocumentoObrigatorio;
	}

	public void setIdeDocumentoObrigatorio(Integer ideDocumentoObrigatorio) {
		this.ideDocumentoObrigatorio = ideDocumentoObrigatorio;
	}

	public String getNomDocumentoObrigatorio() {
		return nomDocumentoObrigatorio;
	}

	public void setNomDocumentoObrigatorio(String nomDocumentoObrigatorio) {
		this.nomDocumentoObrigatorio = nomDocumentoObrigatorio;
	}

	public BigDecimal getNumTamanho() {
		return numTamanho;
	}

	public void setNumTamanho(BigDecimal numTamanho) {
		this.numTamanho = numTamanho;
	}

	public boolean getIndFormulario() {
		return indFormulario;
	}

	public void setIndFormulario(boolean indFormulario) {
		this.indFormulario = indFormulario;
	}

	public String getDscCaminhoArquivo() {
		return dscCaminhoArquivo;
	}

	public void setDscCaminhoArquivo(String dscCaminhoArquivo) {
		this.dscCaminhoArquivo = dscCaminhoArquivo;
	}

	@XmlTransient
	public Collection<Enquadramento> getEnquadramentoCollection() {
		return enquadramentoCollection;
	}

	public void setEnquadramentoCollection(Collection<Enquadramento> enquadramentoCollection) {
		this.enquadramentoCollection = enquadramentoCollection;
	}

	@XmlTransient
	public Collection<DocumentoAto> getDocumentoAtoCollection() {
		return documentoAtoCollection;
	}

	public void setDocumentoAtoCollection(Collection<DocumentoAto> documentoAtoCollection) {
		this.documentoAtoCollection = documentoAtoCollection;
	}

	@XmlTransient
	public Collection<DocumentoObrigatorioRequerimento> getDocumentoObrigatorioRequerimentoCollection() {
		return Util.isNull(documentoObrigatorioRequerimentoCollection) ? (documentoObrigatorioRequerimentoCollection = new ArrayList<DocumentoObrigatorioRequerimento>())
				: documentoObrigatorioRequerimentoCollection;
	}

	public void setDocumentoObrigatorioRequerimentoCollection(Collection<DocumentoObrigatorioRequerimento> documentoObrigatorioRequerimentoCollection) {
		this.documentoObrigatorioRequerimentoCollection = documentoObrigatorioRequerimentoCollection;
	}

	public Boolean getIndPublico() {
		return indPublico;
	}

	public void setIndPublico(Boolean indPublico) {
		this.indPublico = indPublico;
	}

	public DocumentoObrigatorioRequerimento getDocumentoObrigatorioRequerimento(Requerimento requerimento) {
		Hibernate.initialize(documentoObrigatorioRequerimentoCollection);
		for (DocumentoObrigatorioRequerimento docReq : this.documentoObrigatorioRequerimentoCollection) {
			Hibernate.initialize(docReq.getIdeRequerimento());
			if (this.equals(docReq.getIdeDocumentoObrigatorio()) && docReq.getIdeRequerimento().equals(requerimento)) {
				return docReq;
			}
		}
		return null;
	}
	
	public boolean isErb() {
		return ideDocumentoObrigatorio.equals(DocumentoObrigatorioEnum.ERB.getId());
	}
	
	public boolean isPosto() {
		return ideDocumentoObrigatorio.equals(DocumentoObrigatorioEnum.POSTO.getId());
	}
	
	public boolean isTransportadora() {
		return ideDocumentoObrigatorio.equals(DocumentoObrigatorioEnum.TRANSPORTADORA.getId());
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (ideDocumentoObrigatorio != null ? ideDocumentoObrigatorio.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		
		// not
		// set
		if (!(object instanceof DocumentoObrigatorio)) {
			return false;
		}
		DocumentoObrigatorio other = (DocumentoObrigatorio) object;
		if ((this.ideDocumentoObrigatorio == null && other.ideDocumentoObrigatorio != null)
				|| (this.ideDocumentoObrigatorio != null && !this.ideDocumentoObrigatorio.equals(other.ideDocumentoObrigatorio))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return ideDocumentoObrigatorio.toString();
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
	
	public String getFileSizeFormated() {
		if (this.getFileSize() != null) {
			return Long.valueOf(this.getFileSize() / 1024).toString() + " Kb";
		} else
			return "0 Kb";
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	@JSON(include=false)
	@Override
	public Long getId() {
		return new Long(this.ideDocumentoObrigatorio);
	}

	public Boolean getIndAtivo() {
		return indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

	public TipoDocumentoObrigatorio getIdeTipoDocumentoObrigatorio() {
		return ideTipoDocumentoObrigatorio;
	}

	public void setIdeTipoDocumentoObrigatorio(
			TipoDocumentoObrigatorio ideTipoDocumentoObrigatorio) {
		this.ideTipoDocumentoObrigatorio = ideTipoDocumentoObrigatorio;
	}

	public boolean isIndFormularioDigital() {
		return indFormularioDigital;
	}

	public void setIndFormularioDigital(boolean indFormularioDigital) {
		this.indFormularioDigital = indFormularioDigital;
	}

}
