package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.CascadeType;
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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.util.Util;

@Entity
@Table(name = "enquadramento_ato_ambiental")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "EnquadramentoAtoAmbiental.findAll", query = "SELECT e FROM EnquadramentoAtoAmbiental e"),
		@NamedQuery(name = "EnquadramentoAtoAmbiental.findByIdeEnquadramento", query = "SELECT e FROM EnquadramentoAtoAmbiental e WHERE e.enquadramento = :ideEnquadramento"),
		@NamedQuery(name = "EnquadramentoAtoAmbiental.findByIdeAtoAmbiental", query = "SELECT e FROM EnquadramentoAtoAmbiental e WHERE e.atoAmbiental = :ideAtoAmbiental") })
public class EnquadramentoAtoAmbiental implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;
	
	@Id
    @SequenceGenerator(name = "enquadramento_ato_ambiental_ide_enquadramento_ato_ambiental_GENERATOR", sequenceName = "enquadramento_ato_ambiental_ide_enquadramento_ato_ambiental_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "enquadramento_ato_ambiental_ide_enquadramento_ato_ambiental_GENERATOR")
    @Column(name = "ide_enquadramento_ato_ambiental")
    private Integer ideEnquadramentoAtoAmbiental;
	
	@JoinColumn(name = "ide_tipologia", referencedColumnName = "ide_tipologia", updatable = false, nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Tipologia tipologia;
	
	@JoinColumn(name = "ide_enquadramento", referencedColumnName = "ide_enquadramento", updatable = false, nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private Enquadramento enquadramento;
	
	@JoinColumn(name = "ide_ato_ambiental", referencedColumnName = "ide_ato_ambiental", updatable = false, nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private AtoAmbiental atoAmbiental;
	
	@OneToOne(mappedBy = "ideEnquadramentoAtoAmbiental", fetch = FetchType.EAGER)
	private BiomaEnquadramentoAtoAmbiental biomaEnquadramentoAtoAmbiental;
	
	@OneToMany(mappedBy = "ideEnquadramentoAtoAmbiental", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Collection<EnquadramentoFinalidadeUsoAgua> enquadramentoFinalidadeUsoAguaCollection;
	
	@Transient
	private Collection<DocumentoAto> listaDocumentosAtos;
	
	@Transient
	private ProcessoReenquadramentoHistAto processoReenquadramentoHistAtoTransient;
	
	@Transient
	private boolean alteracao;
	
	public EnquadramentoAtoAmbiental() {}

	public EnquadramentoAtoAmbiental(Enquadramento enquadramento, AtoAmbiental atoAmbiental, Tipologia tipologia) {
		super();
		this.tipologia = tipologia;
		this.enquadramento = enquadramento;
		this.atoAmbiental = atoAmbiental;
	}
	
	public EnquadramentoAtoAmbiental(int ideEnquadramento, int ideAtoAmbiental) {
		this.enquadramento = new Enquadramento(ideEnquadramento);
		this.atoAmbiental = new AtoAmbiental(ideAtoAmbiental);
	}
	
	public EnquadramentoAtoAmbiental(Tipologia tipologia, AtoAmbiental atoAmbiental) {
		this.tipologia = tipologia;
		this.atoAmbiental = atoAmbiental;
	}

	public Tipologia getTipologia() {
		return tipologia;
	}

	public void setTipologia(Tipologia tipologia) {
		this.tipologia = tipologia;
	}

	public Enquadramento getEnquadramento() {
		return enquadramento;
	}

	public void setEnquadramento(Enquadramento enquadramento) {
		this.enquadramento = enquadramento;
	}

	public AtoAmbiental getAtoAmbiental() {
		return Util.isNull(atoAmbiental) ? atoAmbiental = new AtoAmbiental(): atoAmbiental;
	}

	public void setAtoAmbiental(AtoAmbiental atoAmbiental) {
		this.atoAmbiental = atoAmbiental;
	}

	public Collection<DocumentoAto> getListaDocumentosAtos() {
		return listaDocumentosAtos;
	}

	public void setListaDocumentosAtos(Collection<DocumentoAto> listaDocumentosAtos) {
		this.listaDocumentosAtos = listaDocumentosAtos;
	}


	public Integer getIdeEnquadramentoAtoAmbiental() {
		return ideEnquadramentoAtoAmbiental;
	}

	public void setIdeEnquadramentoAtoAmbiental(Integer ideEnquadramentoAtoAmbiental) {
		this.ideEnquadramentoAtoAmbiental = ideEnquadramentoAtoAmbiental;
	}

	@Override
	public String toString() {
		return "EnquadramentoAtoAmbiental [ideEnquadramentoAtoAmbiental=" + ideEnquadramentoAtoAmbiental + "]";
	}

	public Collection<EnquadramentoFinalidadeUsoAgua> getEnquadramentoFinalidadeUsoAguaCollection() {
		return enquadramentoFinalidadeUsoAguaCollection;
	}

	public void setEnquadramentoFinalidadeUsoAguaCollection(
			Collection<EnquadramentoFinalidadeUsoAgua> enquadramentoFinalidadeUsoAguaCollection) {
		this.enquadramentoFinalidadeUsoAguaCollection = enquadramentoFinalidadeUsoAguaCollection;
	}

	public BiomaEnquadramentoAtoAmbiental getBiomaEnquadramentoAtoAmbiental() {
		return biomaEnquadramentoAtoAmbiental;
	}

	public void setBiomaEnquadramentoAtoAmbiental(
			BiomaEnquadramentoAtoAmbiental biomaEnquadramentoAtoAmbiental) {
		this.biomaEnquadramentoAtoAmbiental = biomaEnquadramentoAtoAmbiental;
	}

	@Override
	public EnquadramentoAtoAmbiental clone() throws CloneNotSupportedException {
		return (EnquadramentoAtoAmbiental) super.clone();
	}

	public ProcessoReenquadramentoHistAto getProcessoReenquadramentoHistAtoTransient() {
		return processoReenquadramentoHistAtoTransient;
	}

	public void setProcessoReenquadramentoHistAtoTransient(
			ProcessoReenquadramentoHistAto processoReenquadramentoHistAtoTransient) {
		this.processoReenquadramentoHistAtoTransient = processoReenquadramentoHistAtoTransient;
	}

	public boolean isAlteracao() {
		return alteracao;
	}

	public void setAlteracao(boolean alteracao) {
		this.alteracao = alteracao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((atoAmbiental == null) ? 0 : atoAmbiental.hashCode());
		result = prime
				* result
				+ ((ideEnquadramentoAtoAmbiental == null) ? 0
						: ideEnquadramentoAtoAmbiental.hashCode());
		result = prime * result
				+ ((tipologia == null) ? 0 : tipologia.hashCode());
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
		EnquadramentoAtoAmbiental other = (EnquadramentoAtoAmbiental) obj;
		if (atoAmbiental == null) {
			if (other.atoAmbiental != null)
				return false;
		} else if (!atoAmbiental.equals(other.atoAmbiental))
			return false;
		if (ideEnquadramentoAtoAmbiental == null) {
			if (other.ideEnquadramentoAtoAmbiental != null)
				return false;
		} else if (!ideEnquadramentoAtoAmbiental
				.equals(other.ideEnquadramentoAtoAmbiental))
			return false;
		if (tipologia == null) {
			if (other.tipologia != null)
				return false;
		} else if (!tipologia.equals(other.tipologia))
			return false;
		return true;
	}

	

}
