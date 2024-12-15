package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the fce_licenciamento_mineral_transporte_minerio
 * database table.
 * 
 */
@Embeddable
public class FceLicenciamentoMineralTransporteMineiroPK implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "ide_fce_licenciamento_mineral", insertable = false, updatable = false)
	private Integer ideFceLicenciamentoMineral;

	@Column(name = "ide_tipo_transporte_minerio", insertable = false, updatable = false)
	private Integer ideTipoTransporteMinerio;

	public FceLicenciamentoMineralTransporteMineiroPK() {

	}

	public FceLicenciamentoMineralTransporteMineiroPK(FceLicenciamentoMineral fceLicenciamentoMineral, TipoTransporteMinerio tipoTransporteMinerio) {
		this.ideFceLicenciamentoMineral = fceLicenciamentoMineral.getIdeFceLicenciamentoMineral();
		this.ideTipoTransporteMinerio = tipoTransporteMinerio.getIdeTipoTransporteMinerio();
	}

	public Integer getIdeFceLicenciamentoMineral() {
		return ideFceLicenciamentoMineral;
	}

	public void setIdeFceLicenciamentoMineral(Integer ideFceLicenciamentoMineral) {
		this.ideFceLicenciamentoMineral = ideFceLicenciamentoMineral;
	}

	public Integer getIdeTipoTransporteMinerio() {
		return ideTipoTransporteMinerio;
	}

	public void setIdeTipoTransporteMinerio(Integer ideTipoTransporteMinerio) {
		this.ideTipoTransporteMinerio = ideTipoTransporteMinerio;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideFceLicenciamentoMineral == null) ? 0 : ideFceLicenciamentoMineral.hashCode());
		result = prime * result + ((ideTipoTransporteMinerio == null) ? 0 : ideTipoTransporteMinerio.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		FceLicenciamentoMineralTransporteMineiroPK other = (FceLicenciamentoMineralTransporteMineiroPK) obj;
		if(ideFceLicenciamentoMineral == null){
			if(other.ideFceLicenciamentoMineral != null)
				return false;
		}
		else if(!ideFceLicenciamentoMineral.equals(other.ideFceLicenciamentoMineral))
			return false;
		if(ideTipoTransporteMinerio == null){
			if(other.ideTipoTransporteMinerio != null)
				return false;
		}
		else if(!ideTipoTransporteMinerio.equals(other.ideTipoTransporteMinerio))
			return false;
		return true;
	}

}