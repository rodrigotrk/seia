package br.gov.ba.seia.entity;

import java.io.File;

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
import javax.persistence.Transient;

import org.primefaces.event.FileUploadEvent;

import br.gov.ba.seia.entity.generic.AbstractEntity;
import br.gov.ba.seia.util.Util;

@Entity
@Table(name="tcca_documento_apensado")
@NamedQuery(name="TccaDocumentoApensado.findAll", query="SELECT t FROM TccaDocumentoApensado t")
public class TccaDocumentoApensado extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TCCA_DOCUMENTO_APENSADO_IDETCCADOCUMENTOAPENSADO_GENERATOR", sequenceName="TCCA_DOCUMENTO_APENSADO_IDE_TCCA_DOCUMENTO_APENSADO_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TCCA_DOCUMENTO_APENSADO_IDETCCADOCUMENTOAPENSADO_GENERATOR")
	@Column(name="ide_tcca_documento_apensado", updatable=false, unique=true, nullable=false)
	private Integer ideTccaDocumentoApensado;

	@Column(name="ind_excluido", nullable=false)
	private Boolean indExcluido;

	@Column(name="url_tcca_documento", nullable=false, length=100)
	private String urlTccaDocumento;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_tcca", nullable=false)
	private Tcca ideTcca;
	
	@Transient
	private String nomDocumento;
	
	@Transient
	private FileUploadEvent arquivoUpload;

	public TccaDocumentoApensado() {}
	
	@Transient
	public String getNomDocumento() {
		if(!Util.isNullOuVazio(nomDocumento)) {
			return nomDocumento;
		} else if(!Util.isNullOuVazio(urlTccaDocumento)) {
			return new File(urlTccaDocumento).getName();
		} else {
			return "";
		}
	}
	
	@Transient
	public boolean isExisteArquivo() {
		return !Util.isNullOuVazio(urlTccaDocumento);
	}
	


	public Integer getIdeTccaDocumentoApensado() {
		return this.ideTccaDocumentoApensado;
	}

	public void setIdeTccaDocumentoApensado(Integer ideTccaDocumentoApensado) {
		this.ideTccaDocumentoApensado = ideTccaDocumentoApensado;
	}

	public Boolean getIndExcluido() {
		return this.indExcluido;
	}

	public void setIndExcluido(Boolean indExcluido) {
		this.indExcluido = indExcluido;
	}

	public String getUrlTccaDocumento() {
		return this.urlTccaDocumento;
	}

	public void setUrlTccaDocumento(String urlTccaDocumento) {
		this.urlTccaDocumento = urlTccaDocumento;
	}

	public Tcca getIdeTcca() {
		return this.ideTcca;
	}

	public void setIdeTcca(Tcca ideTcca) {
		this.ideTcca = ideTcca;
	}

	public void setNomDocumento(String nomDocumento) {
		this.nomDocumento = nomDocumento;
	}

	public FileUploadEvent getArquivoUpload() {
		return arquivoUpload;
	}

	public void setArquivoUpload(FileUploadEvent arquivoUpload) {
		this.arquivoUpload = arquivoUpload;
	}
}