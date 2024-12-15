package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the motivo_status_reserva_agua database table.
 * 
 */
@Embeddable
public class MotivoStatusReservaAguaPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="ide_status_reserva_agua", insertable=false, updatable=false)
	private Integer ideStatusReservaAgua;

	@Column(name="ide_motivo_reserva_agua", insertable=false, updatable=false)
	private Integer ideMotivoReservaAgua;

	public MotivoStatusReservaAguaPK() {
		
	}
	
	public MotivoStatusReservaAguaPK(StatusReservaAgua statusReservaAgua, MotivoReservaAgua motivoReservaAgua) {
		this.ideStatusReservaAgua = statusReservaAgua.getIdeStatusReservaAgua();
		this.ideMotivoReservaAgua = motivoReservaAgua.getIdeMotivoReservaAgua();
	}
	
	public Integer getIdeStatusReservaAgua() {
		return this.ideStatusReservaAgua;
	}
	
	public void setIdeStatusReservaAgua(Integer ideStatusReservaAgua) {
		this.ideStatusReservaAgua = ideStatusReservaAgua;
	}
	
	public Integer getIdeMotivoReservaAgua() {
		return this.ideMotivoReservaAgua;
	}
	
	public void setIdeMotivoReservaAgua(Integer ideMotivoReservaAgua) {
		this.ideMotivoReservaAgua = ideMotivoReservaAgua;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof MotivoStatusReservaAguaPK)) {
			return false;
		}
		MotivoStatusReservaAguaPK castOther = (MotivoStatusReservaAguaPK)other;
		return 
			this.ideStatusReservaAgua.equals(castOther.ideStatusReservaAgua)
			&& this.ideMotivoReservaAgua.equals(castOther.ideMotivoReservaAgua);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.ideStatusReservaAgua.hashCode();
		hash = hash * prime + this.ideMotivoReservaAgua.hashCode();
		
		return hash;
	}
}