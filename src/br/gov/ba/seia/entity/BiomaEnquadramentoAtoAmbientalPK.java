package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class BiomaEnquadramentoAtoAmbientalPK implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="ide_bioma_requerimento", nullable=false)
	private Integer ideBiomaRequerimento;

	@Column(name="ide_enquadramento_ato_ambiental", nullable=false)
	private Integer ideEnquadramentoAtoAmbiental;

	
	
	public BiomaEnquadramentoAtoAmbientalPK() {
	
	}

	public BiomaEnquadramentoAtoAmbientalPK(Integer ideBiomaRequerimento, Integer ideEnquadramentoAtoAmbiental) {
		this.ideBiomaRequerimento = ideBiomaRequerimento;
		this.ideEnquadramentoAtoAmbiental = ideEnquadramentoAtoAmbiental;
	}

	public Integer getIdeBiomaRequerimento() {
		return ideBiomaRequerimento;
	}

	public void setIdeBiomaRequerimento(Integer ideBiomaRequerimento) {
		this.ideBiomaRequerimento = ideBiomaRequerimento;
	}

	public Integer getIdeEnquadramentoAtoAmbiental() {
		return ideEnquadramentoAtoAmbiental;
	}

	public void setIdeEnquadramentoAtoAmbiental(
			Integer ideEnquadramentoAtoAmbiental) {
		this.ideEnquadramentoAtoAmbiental = ideEnquadramentoAtoAmbiental;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideBiomaRequerimento == null) ? 0 : ideBiomaRequerimento
						.hashCode());
		result = prime
				* result
				+ ((ideEnquadramentoAtoAmbiental == null) ? 0
						: ideEnquadramentoAtoAmbiental.hashCode());
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
		BiomaEnquadramentoAtoAmbientalPK other = (BiomaEnquadramentoAtoAmbientalPK) obj;
		if (ideBiomaRequerimento == null) {
			if (other.ideBiomaRequerimento != null)
				return false;
		} else if (!ideBiomaRequerimento.equals(other.ideBiomaRequerimento))
			return false;
		if (ideEnquadramentoAtoAmbiental == null) {
			if (other.ideEnquadramentoAtoAmbiental != null)
				return false;
		} else if (!ideEnquadramentoAtoAmbiental
				.equals(other.ideEnquadramentoAtoAmbiental))
			return false;
		return true;
	}
	
}