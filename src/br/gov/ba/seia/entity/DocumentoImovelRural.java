package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.util.FileUploadUtil;

@Entity
@Table(name = "documento_imovel_rural")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "DocumentoImovelRural.findAll", query = "SELECT d FROM DocumentoImovelRural d"),
	@NamedQuery(name = "DocumentoImovelRural.findByIdeDocumentoObrigatorio", query = "SELECT d FROM DocumentoImovelRural d WHERE d.ideDocumentoImovelRural = :ideDocumentoImovelRural"),
	@NamedQuery(name = "DocumentoImovelRural.findByNomDocumentoObrigatorio", query = "SELECT d FROM DocumentoImovelRural d WHERE d.nomDocumentoObrigatorio = :nomDocumentoObrigatorio"),
	@NamedQuery(name = "DocumentoImovelRural.findByDscCaminhoArquivo", query = "SELECT d FROM DocumentoImovelRural d WHERE d.dscCaminhoArquivo = :dscCaminhoArquivo"),
	@NamedQuery(name = "DocumentoImovelRural.remove", query = "DELETE FROM DocumentoImovelRural t WHERE t.ideDocumentoImovelRural = :ideDocumentoImovelRural") })
public class DocumentoImovelRural implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name = "DOCUMENTO_IMOVEL_RURAL_IDE_DOCUMENTO_IMOVEL_RURAL_GENERATOR", sequenceName = "DOCUMENTO_IMOVEL_RURAL_IDE_DOCUMENTO_IMOVEL_RURAL_SEQ", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DOCUMENTO_IMOVEL_RURAL_IDE_DOCUMENTO_IMOVEL_RURAL_GENERATOR")
	@Column(name = "ide_documento_imovel_rural", unique = true, nullable = false)
	private Integer ideDocumentoImovelRural;
	
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 1000)
	@Column(name = "nom_documento_obrigatorio", nullable = false)
	private String nomDocumentoObrigatorio;
	
	@Size(max = 1000)
	@Column(name = "dsc_caminho_arquivo", length = 1000)
	private String dscCaminhoArquivo;
	
	@OneToOne(mappedBy = "ideDocumentoProcuracao", fetch = FetchType.LAZY)
    private ImovelRural imovelRural;
	
	@OneToOne(mappedBy = "ideDocumentoImovelRural", fetch = FetchType.LAZY)
    private DocumentoImovelRuralPosse documentoImovelRuralPosse;
	
	@Transient
	private StreamedContent file;
	
	@Transient
	private Long fileSize;

	public DocumentoImovelRural() {}
	
	public DocumentoImovelRural(Integer ideDocumentoImovelRural) {
		this.ideDocumentoImovelRural = ideDocumentoImovelRural;
	}
	
	public DocumentoImovelRural(Integer ideDocumentoImovelRural, String nomDocumentoObrigatorio, String dscCaminhoArquivo) {
		this.ideDocumentoImovelRural = ideDocumentoImovelRural;
		this.nomDocumentoObrigatorio = nomDocumentoObrigatorio;
		this.dscCaminhoArquivo = dscCaminhoArquivo;
	}
	
	public String getFileName() {
		return FileUploadUtil.getFileName(dscCaminhoArquivo);
	}

	public Long getFileSize() {
		if(fileSize != null){
			return fileSize;
		}
		return null;
	}

	public String getFileSizeFormated() {
		if (this.getFileSize() != null) {
			return Long.valueOf(this.getFileSize() / 1024).toString() + " Kb";
		} else
			return "0 Kb";
	}
	
	/*********************
	/*					 *
	//XXX: GETS AND SETS *
	/* 					 *
	/*********************/

	public Integer getIdeDocumentoObrigatorio() {
		return ideDocumentoImovelRural;
	}

	public void setIdeDocumentoObrigatorio(Integer ideDocumentoImovelRural) {
		this.ideDocumentoImovelRural = ideDocumentoImovelRural;
	}

	public String getNomDocumentoObrigatorio() {
		return nomDocumentoObrigatorio;
	}

	public void setNomDocumentoObrigatorio(String nomDocumentoObrigatorio) {
		this.nomDocumentoObrigatorio = nomDocumentoObrigatorio;
	}

	public String getDscCaminhoArquivo() {
		return dscCaminhoArquivo;
	}

	public void setDscCaminhoArquivo(String dscCaminhoArquivo) {
		this.dscCaminhoArquivo = dscCaminhoArquivo;
	}

	public StreamedContent getFile() {
		return file;
	}

	public void setFile(StreamedContent file) {
		this.file = file;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}
	
	public ImovelRural getImovelRural() {
		return imovelRural;
	}

	public void setImovelRural(ImovelRural imovelRural) {
		this.imovelRural = imovelRural;
	}

	public DocumentoImovelRuralPosse getDocumentoImovelRuralPosse() {
		return documentoImovelRuralPosse;
	}

	public void setDocumentoImovelRuralPosse(DocumentoImovelRuralPosse documentoImovelRuralPosse) {
		this.documentoImovelRuralPosse = documentoImovelRuralPosse;
	}

	public Integer getIdeDocumentoImovelRural() {
		return ideDocumentoImovelRural;
	}

	public void setIdeDocumentoImovelRural(Integer ideDocumentoImovelRural) {
		this.ideDocumentoImovelRural = ideDocumentoImovelRural;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideDocumentoImovelRural == null) ? 0 : ideDocumentoImovelRural.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null) return false;
		if(getClass() != obj.getClass()) return false;
		DocumentoImovelRural other = (DocumentoImovelRural) obj;
		if(ideDocumentoImovelRural == null) {
			if(other.ideDocumentoImovelRural != null) return false;
		} else if(!ideDocumentoImovelRural.equals(other.ideDocumentoImovelRural)) return false;
		return true;
	}

	@Override
	public String toString() {
		return ideDocumentoImovelRural + "";
	}
}
