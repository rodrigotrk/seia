package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author carlos.sousa
 */
@Entity
@Table(name = "instrumento_controle_tipologia")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "InstrumentoControleTipologia.findAll", query = "SELECT i FROM InstrumentoControleTipologia i"),
		@NamedQuery(name = "InstrumentoControleTipologia.findByIdeInstrumentoCtrlTipologia", query = "SELECT i FROM InstrumentoControleTipologia i WHERE i.ideInstrumentoCtrlTipologia = :ideInstrumentoCtrlTipologia") })
public class InstrumentoControleTipologia implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@NotNull
	@Column(name = "ide_instrumento_ctrl_tipologia", nullable = false)
	private Integer ideInstrumentoCtrlTipologia;
	// Cad� me resolva
//	@OneToMany(cascade = CascadeType.ALL, mappedBy = "ideInstrumentoCtrlTipologia", fetch = FetchType.LAZY)
//	private Collection<ParametroReferencia> parametroReferenciaCollection;
	@JoinColumn(name = "ide_tipologia", referencedColumnName = "ide_tipologia", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Tipologia ideTipologia;
	@JoinColumn(name = "ide_instrumento_controle", referencedColumnName = "ide_instrumento_controle", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private InstrumentoControle ideInstrumentoControle;

	public InstrumentoControleTipologia() {
	}

	public InstrumentoControleTipologia(Integer ideInstrumentoCtrlTipologia) {
		this.ideInstrumentoCtrlTipologia = ideInstrumentoCtrlTipologia;
	}

	public Integer getIdeInstrumentoCtrlTipologia() {
		return ideInstrumentoCtrlTipologia;
	}

	public void setIdeInstrumentoCtrlTipologia(
			Integer ideInstrumentoCtrlTipologia) {
		this.ideInstrumentoCtrlTipologia = ideInstrumentoCtrlTipologia;
	}

//	@XmlTransient
//	public Collection<ParametroReferencia> getParametroReferenciaCollection() {
//		return parametroReferenciaCollection;
//	}
//
//	public void setParametroReferenciaCollection(
//			Collection<ParametroReferencia> parametroReferenciaCollection) {
//		this.parametroReferenciaCollection = parametroReferenciaCollection;
//	}

	public Tipologia getIdeTipologia() {
		return ideTipologia;
	}

	public void setIdeTipologia(Tipologia ideTipologia) {
		this.ideTipologia = ideTipologia;
	}

	public InstrumentoControle getIdeInstrumentoControle() {
		return ideInstrumentoControle;
	}

	public void setIdeInstrumentoControle(
			InstrumentoControle ideInstrumentoControle) {
		this.ideInstrumentoControle = ideInstrumentoControle;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (ideInstrumentoCtrlTipologia != null ? ideInstrumentoCtrlTipologia
				.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		
		
		if (!(object instanceof InstrumentoControleTipologia)) {
			return false;
		}
		InstrumentoControleTipologia other = (InstrumentoControleTipologia) object;
		if ((this.ideInstrumentoCtrlTipologia == null && other.ideInstrumentoCtrlTipologia != null)
				|| (this.ideInstrumentoCtrlTipologia != null && !this.ideInstrumentoCtrlTipologia
						.equals(other.ideInstrumentoCtrlTipologia))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "br.gov.ba.seia.entity.InstrumentoControleTipologia[ ideInstrumentoCtrlTipologia="
				+ ideInstrumentoCtrlTipologia + " ]";
	}

}
