package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 
 * @author luis
 */
@Entity
@Table(name = "ato_ambiental_tipologia")
@NamedQueries({ @NamedQuery(name = "AtoAmbientalTipologia.findAll", query = "SELECT a FROM AtoAmbientalTipologia a") })

public class AtoAmbientalTipologia implements Serializable, Cloneable {
	
	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@Column(name = "ide_ato_ambiental_tipologia")
	private Integer ideAtoAmbientalTipologia;

	@JoinColumn(name = "ide_ato_ambiental", referencedColumnName = "ide_ato_ambiental", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private AtoAmbiental ideAtoAmbiental;

	@JoinColumn(name = "ide_tipologia", referencedColumnName = "ide_tipologia", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Tipologia ideTipologia;

	@OneToMany(mappedBy = "ideAtoAmbientalTipologia", fetch = FetchType.LAZY)
	private Collection<AtoAmbientalTipologiaFinalidade> atoAmbientalTipologiaFinalidadeCollection;
	
	@Transient
	private Collection<DocumentoObrigatorio> documentosObrigatorios;

	public AtoAmbientalTipologia() {
	}

	public AtoAmbientalTipologia(Integer ideAtoAmbientalTipologia) {
		this.ideAtoAmbientalTipologia = ideAtoAmbientalTipologia;
	}

	public AtoAmbientalTipologia(AtoAmbiental ideAtoAmbiental, Tipologia ideTipologia) {
		super();
		this.ideAtoAmbiental = ideAtoAmbiental;
		this.ideTipologia = ideTipologia;
	}

	public Integer getIdeAtoAmbientalTipologia() {
		return ideAtoAmbientalTipologia;
	}

	public void setIdeAtoAmbientalTipologia(Integer ideAtoAmbientalTipologia) {
		this.ideAtoAmbientalTipologia = ideAtoAmbientalTipologia;
	}

	public AtoAmbiental getIdeAtoAmbiental() {
		return ideAtoAmbiental;
	}

	public void setIdeAtoAmbiental(AtoAmbiental ideAtoAmbiental) {
		this.ideAtoAmbiental = ideAtoAmbiental;
	}

	public Tipologia getIdeTipologia() {
		return ideTipologia;
	}

	public void setIdeTipologia(Tipologia ideTipologia) {
		this.ideTipologia = ideTipologia;
	}

	public Collection<DocumentoObrigatorio> getDocumentosObrigatorios() {
		return documentosObrigatorios;
	}

	public void setDocumentosObrigatorios(Collection<DocumentoObrigatorio> documentosObrigatorios) {
		this.documentosObrigatorios = documentosObrigatorios;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideAtoAmbiental == null) ? 0 : ideAtoAmbiental.hashCode());
		result = prime * result + ((ideTipologia == null) ? 0 : ideTipologia.hashCode());
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
		AtoAmbientalTipologia other = (AtoAmbientalTipologia) obj;
		if (ideAtoAmbiental == null) {
			if (other.ideAtoAmbiental != null)
				return false;
		} else if (!ideAtoAmbiental.equals(other.ideAtoAmbiental))
			return false;
		if (ideTipologia == null) {
			if (other.ideTipologia != null)
				return false;
		} else if (!ideTipologia.equals(other.ideTipologia))
			return false;
		return true;
	}

	@Override
	public AtoAmbientalTipologia clone() throws CloneNotSupportedException {
		return (AtoAmbientalTipologia) super.clone();
	}

	@Override
	public String toString() {
		
		if(ideAtoAmbiental != null && ideTipologia != null){
			return "[ideAtoAmbiental=" + ideAtoAmbiental + ", ideTipologia=" + ideTipologia + "]";			
		}
		
		 return "[ideAtoAmbiental=" + ideAtoAmbiental +" }";
		
	}

	public Collection<AtoAmbientalTipologiaFinalidade> getAtoAmbientalTipologiaFinalidadeCollection() {
		return atoAmbientalTipologiaFinalidadeCollection;
	}

	public void setAtoAmbientalTipologiaFinalidadeCollection(Collection<AtoAmbientalTipologiaFinalidade> atoAmbientalTipologiaFinalidadeCollection) {
		this.atoAmbientalTipologiaFinalidadeCollection = atoAmbientalTipologiaFinalidadeCollection;
	}
	
}
