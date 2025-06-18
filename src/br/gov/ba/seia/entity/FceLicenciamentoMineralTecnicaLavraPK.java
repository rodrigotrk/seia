package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;

/**
 * The primary key class for the fce_licenciamento_mineral_tecnica_lavra
 * database table.
 * 
 */
public class FceLicenciamentoMineralTecnicaLavraPK implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "ide_fce_licenciamento_mineral", insertable = false, updatable = false)
	private Integer ideFceLicenciamentoMineral;

	@Column(name = "ide_tecnica_lavra", insertable = false, updatable = false)
	private Integer ideTecnicaLavra;

	public FceLicenciamentoMineralTecnicaLavraPK() {

	}

	public FceLicenciamentoMineralTecnicaLavraPK(FceLicenciamentoMineral fceLicenciamentoMineral, TecnicaLavra ideTecnicaLavra) {
		this.ideFceLicenciamentoMineral = fceLicenciamentoMineral.getIdeFceLicenciamentoMineral();
		this.ideTecnicaLavra = ideTecnicaLavra.getIdeTecnicaLavra();
	}

	public Integer getIdeFceLicenciamentoMineral() {
		return ideFceLicenciamentoMineral;
	}

	public void setIdeFceLicenciamentoMineral(Integer ideFceLicenciamentoMineral) {
		this.ideFceLicenciamentoMineral = ideFceLicenciamentoMineral;
	}

	public Integer getIdeTecnicaLavra() {
		return ideTecnicaLavra;
	}

	public void setIdeTecnicaLavra(Integer idetecnicaLavra) {
		this.ideTecnicaLavra = idetecnicaLavra;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideFceLicenciamentoMineral == null) ? 0 : ideFceLicenciamentoMineral.hashCode());
		result = prime * result + ((ideTecnicaLavra == null) ? 0 : ideTecnicaLavra.hashCode());
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
		FceLicenciamentoMineralTecnicaLavraPK other = (FceLicenciamentoMineralTecnicaLavraPK) obj;
		if(ideFceLicenciamentoMineral == null){
			if(other.ideFceLicenciamentoMineral != null)
				return false;
		}
		else if(!ideFceLicenciamentoMineral.equals(other.ideFceLicenciamentoMineral))
			return false;
		if(ideTecnicaLavra == null){
			if(other.ideTecnicaLavra != null)
				return false;
		}
		else if(!ideTecnicaLavra.equals(other.ideTecnicaLavra))
			return false;
		return true;
	}

}
