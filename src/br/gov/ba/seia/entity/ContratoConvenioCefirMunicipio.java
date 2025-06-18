package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Entity implementation class for Entity: ContratoConvenioCefirMunicipio
 * @author carlos.duarte
 */
@Entity
@XmlRootElement
@Table(name="contrato_convenio_cefir_municipio")
@NamedQueries({ 
	@NamedQuery(name="ContratoConvenioCefirMunicipio.findAll", query="SELECT c FROM ContratoConvenioCefirMunicipio c"),
	@NamedQuery(name="ContratoConvenioCefirMunicipio.findByContratoConvenioCefir", query="SELECT c FROM ContratoConvenioCefirMunicipio c WHERE c.ideContratoConvenioCefir = :ideContratoConvenioCefir"),
	@NamedQuery(name="ContratoConvenioCefirMunicipio.findByMunicipio", query="SELECT c FROM ContratoConvenioCefirMunicipio c WHERE c.ideMunicipio = :ideMunicipio")
})
public class ContratoConvenioCefirMunicipio implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@JoinColumn(name="ide_contrato_convenio_cefir", referencedColumnName="ide_contrato_convenio_cefir", nullable=false)
	@NotNull
	@ManyToOne(optional=false)
	private ContratoConvenioCefir ideContratoConvenioCefir;
	
	@Id 
	@JoinColumn(name="ide_municipio", referencedColumnName="ide_municipio", nullable=false)
	@NotNull
	@ManyToOne(optional=false)
	private Municipio ideMunicipio;

	public ContratoConvenioCefirMunicipio() {
		
	}
		
	public ContratoConvenioCefirMunicipio(ContratoConvenioCefir ideContratoConvenioCefir, Municipio ideMunicipio) {
		this.ideContratoConvenioCefir = ideContratoConvenioCefir;
		this.ideMunicipio = ideMunicipio;
	}
	
	
	public ContratoConvenioCefir getIdeContratoConvenioCefir() {
		return this.ideContratoConvenioCefir;
	}

	public void setIdeContratoConvenioCefir(ContratoConvenioCefir ideContratoConvenioCefir) {
		this.ideContratoConvenioCefir = ideContratoConvenioCefir;
	}   
	public Municipio getIdeMunicipio() {
		return this.ideMunicipio;
	}

	public void setIdeMunicipio(Municipio ideMunicipio) {
		this.ideMunicipio = ideMunicipio;
	}
	
	@Override
	public String toString() {
		return String.valueOf(this.ideContratoConvenioCefir + "_" + this.ideMunicipio);
	}
	
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof ContratoConvenioCefirMunicipio)) {
			return false;
		}
		ContratoConvenioCefirMunicipio other = (ContratoConvenioCefirMunicipio) object;
		if (this.ideContratoConvenioCefir != null && other.ideContratoConvenioCefir != null
				&& this.ideMunicipio != null && other.ideMunicipio != null
				&& this.ideContratoConvenioCefir == other.ideContratoConvenioCefir
				&& this.ideMunicipio == other.ideMunicipio) {
			return true;
		}
		return false;
	}
   
}
