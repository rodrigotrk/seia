package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;

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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.gov.ba.seia.interfaces.BaseEntity;
import br.gov.ba.seia.util.Util;

@Entity
@Table(name = "documento_ato")
@NamedQueries({ @NamedQuery(name = "DocumentoAto.findAll", query = "SELECT d FROM DocumentoAto d") })
public class DocumentoAto implements Serializable, Comparable<DocumentoAto>, BaseEntity {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DOCUMENTO_ATO_IDE_DOCUMENTO_ATO_seq")
	@SequenceGenerator(name = "DOCUMENTO_ATO_IDE_DOCUMENTO_ATO_seq", sequenceName = "DOCUMENTO_ATO_IDE_DOCUMENTO_ATO_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name = "ide_documento_ato", nullable = false)
	private Integer ideDocumentoAto;
	
	@Basic(optional = false)
	@Column(name = "ind_ativo")
	private boolean indAtivo;
	
	@JoinColumn(name = "ide_tipologia", referencedColumnName = "ide_tipologia")
	@ManyToOne (fetch=FetchType.LAZY)
	private Tipologia ideTipologia;
	
	@JoinColumn(name = "ide_tipo_finalidade_uso_agua", referencedColumnName = "ide_tipo_finalidade_uso_agua")
	@ManyToOne
	private TipoFinalidadeUsoAgua ideTipoFinalidadeUsoAgua;
	
	@JoinColumn(name = "ide_documento_obrigatorio", referencedColumnName = "ide_documento_obrigatorio")
	@ManyToOne(optional = false)
	private DocumentoObrigatorio ideDocumentoObrigatorio;
	
	@JoinColumn(name = "ide_ato_ambiental", referencedColumnName = "ide_ato_ambiental")
	@ManyToOne(optional = false)
	private AtoAmbiental ideAtoAmbiental;

	@OneToMany(mappedBy = "documentoAto")
	private Collection<EnquadramentoDocumentoAto> enquadramentoDocumentoAtoCollection;

	@Transient
	private boolean checked;
	
	@Transient
	private EnquadramentoAtoAmbiental enquadramentoAtoAmbiental;
	
	@Transient
	private EnquadramentoAtoAmbiental reenquadramentoAtoAmbiental;

	@Transient
	private boolean checkedReenquadramento;
	
	public DocumentoAto() {
	}

	public DocumentoAto(Integer ideDocumentoAto) {
		this.ideDocumentoAto = ideDocumentoAto;
	}

	public DocumentoAto(Integer ideDocumentoAto, boolean indAtivo) {
		this.ideDocumentoAto = ideDocumentoAto;
		this.indAtivo = indAtivo;
	}

	public Integer getIdeDocumentoAto() {
		return ideDocumentoAto;
	}

	public void setIdeDocumentoAto(Integer ideDocumentoAto) {
		this.ideDocumentoAto = ideDocumentoAto;
	}

	public boolean getIndAtivo() {
		return indAtivo;
	}

	public void setIndAtivo(boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

	public Tipologia getIdeTipologia() {
		return ideTipologia;
	}

	public void setIdeTipologia(Tipologia ideTipologia) {
		this.ideTipologia = ideTipologia;
	}

	public TipoFinalidadeUsoAgua getIdeTipoFinalidadeUsoAgua() {
		return ideTipoFinalidadeUsoAgua;
	}

	public void setIdeTipoFinalidadeUsoAgua(TipoFinalidadeUsoAgua ideTipoFinalidadeUsoAgua) {
		this.ideTipoFinalidadeUsoAgua = ideTipoFinalidadeUsoAgua;
	}

	public DocumentoObrigatorio getIdeDocumentoObrigatorio() {
		return ideDocumentoObrigatorio;
	}

	public void setIdeDocumentoObrigatorio(DocumentoObrigatorio ideDocumentoObrigatorio) {
		this.ideDocumentoObrigatorio = ideDocumentoObrigatorio;
	}

	public AtoAmbiental getIdeAtoAmbiental() {
		return ideAtoAmbiental;
	}

	public void setIdeAtoAmbiental(AtoAmbiental ideAtoAmbiental) {
		this.ideAtoAmbiental = ideAtoAmbiental;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public Collection<EnquadramentoDocumentoAto> getEnquadramentoDocumentoAtoCollection() {
		return enquadramentoDocumentoAtoCollection;
	}

	public void setEnquadramentoDocumentoAtoCollection(
			Collection<EnquadramentoDocumentoAto> enquadramentoDocumentoAtoCollection) {
		this.enquadramentoDocumentoAtoCollection = enquadramentoDocumentoAtoCollection;
	}

	public String getDescricao() {
		if(Util.isNull(ideTipologia)){
			return "";
		}
		
		return this.ideTipologia.getDesTipologia();
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (ideDocumentoAto != null ? ideDocumentoAto.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		
		
		if (!(object instanceof DocumentoAto)) {
			return false;
		}
		DocumentoAto other = (DocumentoAto) object;
		if ((this.ideDocumentoAto == null && other.ideDocumentoAto != null)
				|| (this.ideDocumentoAto != null && !this.ideDocumentoAto.equals(other.ideDocumentoAto))) {
			return false;
		}
		return true;
	}

	

	@Override
	public int compareTo(DocumentoAto doc) {
		
		if(this.ideDocumentoObrigatorio.getIndFormulario() && !doc.getIdeDocumentoObrigatorio().getIndFormulario()){
			return 3;
		}
		
		if(Util.isNullOuVazio(this.ideTipologia) && !doc.getIdeDocumentoObrigatorio().getIndFormulario() && !Util.isNullOuVazio(doc.getIdeTipologia())){
			return 2;
		}
		
		if(!this.ideDocumentoObrigatorio.getIndFormulario() && !doc.getIdeDocumentoObrigatorio().getIndFormulario() 
				&& !Util.isNullOuVazio(doc.getIdeTipologia()) && !Util.isNullOuVazio(ideTipologia)){
			return doc.getIdeDocumentoObrigatorio().getNomDocumentoObrigatorio().compareTo(ideDocumentoObrigatorio.getNomDocumentoObrigatorio());
		}
		
		return 0;
	}

	public EnquadramentoAtoAmbiental getEnquadramentoAtoAmbiental() {
		return enquadramentoAtoAmbiental;
	}

	public void setEnquadramentoAtoAmbiental(EnquadramentoAtoAmbiental enquadramentoAtoAmbiental) {
		this.enquadramentoAtoAmbiental = enquadramentoAtoAmbiental;
	}

	@Override
	public Long getId() {
		return new Long(ideDocumentoAto);
	}
	
	@Override
	public String toString() {
		return ideDocumentoAto.toString() ;
	}

	public EnquadramentoAtoAmbiental getReenquadramentoAtoAmbiental() {
		return reenquadramentoAtoAmbiental;
	}

	public void setReenquadramentoAtoAmbiental(
			EnquadramentoAtoAmbiental reenquadramentoAtoAmbiental) {
		this.reenquadramentoAtoAmbiental = reenquadramentoAtoAmbiental;
	}

	public boolean isCheckedReenquadramento() {
		return checkedReenquadramento;
	}

	public void setCheckedReenquadramento(boolean checkedReenquadramento) {
		this.checkedReenquadramento = checkedReenquadramento;
	}
}
