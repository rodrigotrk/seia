package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * Tabela que armazena a associação entre {@link StatusReservaAgua} e {@link MotivoReservaAgua}
 * @see <a href="http://10.105.12.26/redmine/issues/7442">#7442</a>
 */
@Entity
@Table(name="motivo_status_reserva_agua")
@NamedQuery(name="MotivoStatusReservaAgua.findAll", query="SELECT m FROM MotivoStatusReservaAgua m")
public class MotivoStatusReservaAgua implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MotivoStatusReservaAguaPK ideMotivoStatusReservaAguaPK;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	@JoinColumn(name="ide_status_reserva_agua", referencedColumnName="ide_status_reserva_agua", nullable = false, insertable = false, updatable = false)
	private StatusReservaAgua ideStatusReservaAgua;
	
	@ManyToOne(optional=true, fetch=FetchType.LAZY)
	@JoinColumn(name="ide_motivo_reserva_agua", referencedColumnName="ide_motivo_reserva_agua", nullable = false, insertable = false, updatable = false)
	private MotivoReservaAgua ideMotivoReservaAgua;

	public MotivoStatusReservaAgua() {
		
	}
	
	public MotivoStatusReservaAgua(StatusReservaAgua statusReservaAgua, MotivoReservaAgua motivoReservaAgua) {
		this.ideMotivoStatusReservaAguaPK = new MotivoStatusReservaAguaPK(statusReservaAgua, motivoReservaAgua);
		this.ideStatusReservaAgua = statusReservaAgua;
		this.ideMotivoReservaAgua = motivoReservaAgua;
	}

	public MotivoStatusReservaAguaPK getIdeMotivoStatusReservaAguaPK() {
		return ideMotivoStatusReservaAguaPK;
	}

	public void setIdeMotivoStatusReservaAguaPK(MotivoStatusReservaAguaPK ideMotivoStatusReservaAguaPK) {
		this.ideMotivoStatusReservaAguaPK = ideMotivoStatusReservaAguaPK;
	}

	public StatusReservaAgua getIdeStatusReservaAgua() {
		return ideStatusReservaAgua;
	}

	public void setIdeStatusReservaAgua(StatusReservaAgua ideStatusReservaAgua) {
		this.ideStatusReservaAgua = ideStatusReservaAgua;
	}

	public MotivoReservaAgua getIdeMotivoReservaAgua() {
		return ideMotivoReservaAgua;
	}

	public void setIdeMotivoReservaAgua(MotivoReservaAgua ideMotivoReservaAgua) {
		this.ideMotivoReservaAgua = ideMotivoReservaAgua;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ideMotivoStatusReservaAguaPK == null) ? 0 : ideMotivoStatusReservaAguaPK.hashCode());
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
		MotivoStatusReservaAgua other = (MotivoStatusReservaAgua) obj;
		if (ideMotivoStatusReservaAguaPK == null) {
			if (other.ideMotivoStatusReservaAguaPK != null)
				return false;
		}
		else if (!ideMotivoStatusReservaAguaPK.equals(other.ideMotivoStatusReservaAguaPK))
			return false;
		return true;
	}

}