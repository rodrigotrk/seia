package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
/**
 * 
 * @author eduardo.fernandes
 *
 */
@Embeddable
public class LacTransporteResiduoPK implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
    @Basic(optional = false)
	@NotNull
    @Column(name = "ide_lac_transporte", nullable = false)
	private int ideLacTransporte;
	
	@Basic(optional = false)
	@NotNull
    @Column(name = "ide_Residuo", nullable = false)
	private int ideResiduo;

	public LacTransporteResiduoPK() {
	}

	public LacTransporteResiduoPK(int ideLacTransporte, int ideResiduo) {
		this.ideLacTransporte = ideLacTransporte;
		this.ideResiduo = ideResiduo;
	}    
    
	public int getIdeLacTransporte() {
		return ideLacTransporte;
	}

	public void setIdeLacTransporte(int ideLacTransporte) {
		this.ideLacTransporte = ideLacTransporte;
	}

	public int getIdeResiduo() {
		return ideResiduo;
	}

	public void setIdeResiduo(int ideResiduo) {
		this.ideResiduo = ideResiduo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ideLacTransporte;
		result = prime * result + ideResiduo;
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
		LacTransporteResiduoPK other = (LacTransporteResiduoPK) obj;
		if (ideLacTransporte != other.ideLacTransporte)
			return false;
		if (ideResiduo != other.ideResiduo)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "LacTransporteResiduoPK [ideLacTransporte=" + ideLacTransporte + ", ideResiduo=" + ideResiduo + "]";
	}
}