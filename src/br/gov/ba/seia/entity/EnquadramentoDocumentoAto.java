package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "enquadramento_documento_ato")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "EnquadramentoDocumentoAto.findAll", query = "SELECT e FROM EnquadramentoDocumentoAto e"),
		@NamedQuery(name = "EnquadramentoDocumentoAto.findByIdeEnquadramento", query = "SELECT e FROM EnquadramentoDocumentoAto e WHERE e.enquadramentoDocumentoAtoPK.ideEnquadramento = :ideEnquadramento"),
		@NamedQuery(name = "EnquadramentoDocumentoAto.findByIdeDocumentoAto", query = "SELECT e FROM EnquadramentoDocumentoAto e WHERE e.enquadramentoDocumentoAtoPK.ideDocumentoAto = :ideDocumentoAto") })
public class EnquadramentoDocumentoAto implements Serializable {

	private static final long serialVersionUID = 1L;
	@EmbeddedId
	protected EnquadramentoDocumentoAtoPK enquadramentoDocumentoAtoPK;

	@JoinColumn(name = "ide_enquadramento", referencedColumnName = "ide_enquadramento", insertable = false, updatable = false)
	@ManyToOne(optional = false)
	private Enquadramento enquadramento;
	@JoinColumn(name = "ide_documento_ato", referencedColumnName = "ide_documento_ato", insertable = false, updatable = false)
	@ManyToOne(optional = false)
	private DocumentoAto documentoAto;
	
	public EnquadramentoDocumentoAto() {
	}

	public void gerarPK(){
		this.enquadramentoDocumentoAtoPK = new EnquadramentoDocumentoAtoPK(this.enquadramento.getIdeEnquadramento(), this.documentoAto.getIdeDocumentoAto());
	}
	
	public EnquadramentoDocumentoAto(EnquadramentoDocumentoAtoPK enquadramentoDocumentoAtoPK) {
		this.enquadramentoDocumentoAtoPK = enquadramentoDocumentoAtoPK;
	}

	public EnquadramentoDocumentoAto(Enquadramento enquadramento, DocumentoAto documentoAto) {
		super();
		this.enquadramento = enquadramento;
		this.documentoAto = documentoAto;
	}

	public Enquadramento getEnquadramento() {
		return enquadramento;
	}

	public void setEnquadramento(Enquadramento enquadramento) {
		this.enquadramento = enquadramento;
	}

	public DocumentoAto getDocumentoAto() {
		return documentoAto;
	}

	public void setDocumentoAto(DocumentoAto documentoAto) {
		this.documentoAto = documentoAto;
	}

	public EnquadramentoDocumentoAtoPK getEnquadramentoDocumentoAtoPK() {
		return enquadramentoDocumentoAtoPK;
	}

	public void setEnquadramentoDocumentoAtoPK(EnquadramentoDocumentoAtoPK enquadramentoDocumentoAtoPK) {
		this.enquadramentoDocumentoAtoPK = enquadramentoDocumentoAtoPK;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (enquadramentoDocumentoAtoPK != null ? enquadramentoDocumentoAtoPK.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof EnquadramentoDocumentoAto)) {
			return false;
		}
		EnquadramentoDocumentoAto other = (EnquadramentoDocumentoAto) object;
		if ((this.enquadramentoDocumentoAtoPK == null && other.enquadramentoDocumentoAtoPK != null)
				|| (this.enquadramentoDocumentoAtoPK != null && !this.enquadramentoDocumentoAtoPK
						.equals(other.enquadramentoDocumentoAtoPK))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "[ enquadramento=" + enquadramento + ", documentoAto=" + documentoAto + " ]";
	}
}