package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "bioma_enquadramento_ato_ambiental")
public class BiomaEnquadramentoAtoAmbiental implements Serializable, Cloneable {
    
	private static final long serialVersionUID = 3952604804474204139L;

    @EmbeddedId
	private BiomaEnquadramentoAtoAmbientalPK biomaEnquadramentoAtoAmbientalPK;
	
    @JoinColumn(name="ide_bioma_requerimento", referencedColumnName="ide_bioma_requerimento", insertable = false, updatable = false)
    @OneToOne(fetch=FetchType.EAGER)
    private BiomaRequerimento ideBiomaRequerimento;
   
    @JoinColumn(name="ide_enquadramento_ato_ambiental", referencedColumnName="ide_enquadramento_ato_ambiental", insertable = false, updatable = false)
    @OneToOne(fetch=FetchType.EAGER)
    private EnquadramentoAtoAmbiental ideEnquadramentoAtoAmbiental;
   
    public BiomaEnquadramentoAtoAmbiental() {
    	
    }

	public BiomaEnquadramentoAtoAmbiental(BiomaEnquadramentoAtoAmbientalPK biomaEnquadramentoAtoAmbientalPK) {
		this.biomaEnquadramentoAtoAmbientalPK = biomaEnquadramentoAtoAmbientalPK;
	}

	public BiomaEnquadramentoAtoAmbientalPK getBiomaEnquadramentoAtoAmbientalPK() {
		return biomaEnquadramentoAtoAmbientalPK;
	}

	public void setBiomaEnquadramentoAtoAmbientalPK(
			BiomaEnquadramentoAtoAmbientalPK biomaEnquadramentoAtoAmbientalPK) {
		this.biomaEnquadramentoAtoAmbientalPK = biomaEnquadramentoAtoAmbientalPK;
	}

	public BiomaRequerimento getIdeBiomaRequerimento() {
		return ideBiomaRequerimento;
	}

	public void setIdeBiomaRequerimento(BiomaRequerimento ideBiomaRequerimento) {
		this.ideBiomaRequerimento = ideBiomaRequerimento;
	}

	public EnquadramentoAtoAmbiental getIdeEnquadramentoAtoAmbiental() {
		return ideEnquadramentoAtoAmbiental;
	}

	public void setIdeEnquadramentoAtoAmbiental(
			EnquadramentoAtoAmbiental ideEnquadramentoAtoAmbiental) {
		this.ideEnquadramentoAtoAmbiental = ideEnquadramentoAtoAmbiental;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((biomaEnquadramentoAtoAmbientalPK == null) ? 0
						: biomaEnquadramentoAtoAmbientalPK.hashCode());
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
		BiomaEnquadramentoAtoAmbiental other = (BiomaEnquadramentoAtoAmbiental) obj;
		if (biomaEnquadramentoAtoAmbientalPK == null) {
			if (other.biomaEnquadramentoAtoAmbientalPK != null)
				return false;
		} else if (!biomaEnquadramentoAtoAmbientalPK
				.equals(other.biomaEnquadramentoAtoAmbientalPK))
			return false;
		return true;
	}
    
	@Override
	public BiomaEnquadramentoAtoAmbiental clone() throws CloneNotSupportedException {
		return (BiomaEnquadramentoAtoAmbiental) super.clone();
	}
}