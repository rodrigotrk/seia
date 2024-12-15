package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Chave composta da tabela {@link ReservaAgua} com as FKs {@link FceReservaAgua}, {@link StatusReservaAgua} e {@link FceOutorgaLocalizacaoGeografica}.
 * @see <a href="http://10.105.12.26/redmine/issues/7442">#7442</a>
 */
@Embeddable
public class ReservaAguaPK implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="ide_fce_reserva_agua", nullable=false)
	private Integer ideFceReservaAgua;

	@Column(name="ide_status_reserva_agua", nullable=false)
	private Integer ideStatusReservaAgua;

	@Column(name="ide_fce_outorga_localizacao_geografica", nullable=false)
	private Integer ideFceOutorgaLocalizacaoGeografica;

	public ReservaAguaPK(){
		
	}
	
	public ReservaAguaPK(FceReservaAgua ideFceReservaAgua, StatusReservaAgua ideStatusReservaAgua, FceOutorgaLocalizacaoGeografica ideFceOutorgaLocalizacaoGeografica) {
		this.ideFceReservaAgua = ideFceReservaAgua.getIdeFceReservaAgua();
		this.ideStatusReservaAgua = ideStatusReservaAgua.getIdeStatusReservaAgua();
		this.ideFceOutorgaLocalizacaoGeografica = ideFceOutorgaLocalizacaoGeografica.getIdeFceOutorgaLocalizacaoGeografica();
	}

	public Integer getIdeFceReservaAgua() {
		return ideFceReservaAgua;
	}

	public void setIdeFceReservaAgua(Integer ideFceReservaAgua) {
		this.ideFceReservaAgua = ideFceReservaAgua;
	}

	public Integer getIdeStatusReservaAgua() {
		return ideStatusReservaAgua;
	}

	public void setIdeStatusReservaAgua(Integer ideStatusReservaAgua) {
		this.ideStatusReservaAgua = ideStatusReservaAgua;
	}

	public Integer getIdeFceOutorgaLocalizacaoGeografica() {
		return ideFceOutorgaLocalizacaoGeografica;
	}

	public void setIdeFceOutorgaLocalizacaoGeografica(Integer ideFceOutorgaLocalizacaoGeografica) {
		this.ideFceOutorgaLocalizacaoGeografica = ideFceOutorgaLocalizacaoGeografica;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ideFceOutorgaLocalizacaoGeografica == null) ? 0 : ideFceOutorgaLocalizacaoGeografica.hashCode());
		result = prime * result + ((ideFceReservaAgua == null) ? 0 : ideFceReservaAgua.hashCode());
		result = prime * result + ((ideStatusReservaAgua == null) ? 0 : ideStatusReservaAgua.hashCode());
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
		ReservaAguaPK other = (ReservaAguaPK) obj;
		if (ideFceOutorgaLocalizacaoGeografica == null) {
			if (other.ideFceOutorgaLocalizacaoGeografica != null)
				return false;
		}
		else if (!ideFceOutorgaLocalizacaoGeografica.equals(other.ideFceOutorgaLocalizacaoGeografica))
			return false;
		if (ideFceReservaAgua == null) {
			if (other.ideFceReservaAgua != null)
				return false;
		}
		else if (!ideFceReservaAgua.equals(other.ideFceReservaAgua))
			return false;
		if (ideStatusReservaAgua == null) {
			if (other.ideStatusReservaAgua != null)
				return false;
		}
		else if (!ideStatusReservaAgua.equals(other.ideStatusReservaAgua))
			return false;
		return true;
	}
	
}