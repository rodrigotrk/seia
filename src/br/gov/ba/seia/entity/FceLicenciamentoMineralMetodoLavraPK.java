package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;

/**
 * The primary key class for the fce_licenciamento_mineral_metodo_lavra database
 * table.
 * 
 */
public class FceLicenciamentoMineralMetodoLavraPK implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "ide_fce_licenciamento_mineral", insertable = false, updatable = false)
	private Integer ideFceLicenciamentoMineral;

	@Column(name = "ide_metodo_lavra", insertable = false, updatable = false)
	private Integer ideMetodoLavra;

	public FceLicenciamentoMineralMetodoLavraPK() {

	}

	public FceLicenciamentoMineralMetodoLavraPK(FceLicenciamentoMineral fceLicenciamentoMineral, MetodoLavra metodoLavra) {
		this.ideFceLicenciamentoMineral = fceLicenciamentoMineral.getIdeFceLicenciamentoMineral();
		this.ideMetodoLavra = metodoLavra.getIdeMetodoLavra();
	}

	public Integer getIdeFceLicenciamentoMineral() {
		return ideFceLicenciamentoMineral;
	}

	public void setIdeFceLicenciamentoMineral(Integer ideFceLicenciamentoMineral) {
		this.ideFceLicenciamentoMineral = ideFceLicenciamentoMineral;
	}

	public Integer getIdeMetodoLavra() {
		return ideMetodoLavra;
	}

	public void setIdeMetodoLavra(Integer ideMetodoLavra) {
		this.ideMetodoLavra = ideMetodoLavra;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideFceLicenciamentoMineral == null) ? 0 : ideFceLicenciamentoMineral.hashCode());
		result = prime * result + ((ideMetodoLavra == null) ? 0 : ideMetodoLavra.hashCode());
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
		FceLicenciamentoMineralMetodoLavraPK other = (FceLicenciamentoMineralMetodoLavraPK) obj;
		if(ideFceLicenciamentoMineral == null){
			if(other.ideFceLicenciamentoMineral != null)
				return false;
		}
		else if(!ideFceLicenciamentoMineral.equals(other.ideFceLicenciamentoMineral))
			return false;
		if(ideMetodoLavra == null){
			if(other.ideMetodoLavra != null)
				return false;
		}
		else if(!ideMetodoLavra.equals(other.ideMetodoLavra))
			return false;
		return true;
	}

}
