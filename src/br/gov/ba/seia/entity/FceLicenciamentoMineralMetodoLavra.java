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
 * The persistent class for the fce_licenciamento_mineral_metodo_lavra database
 * table.
 * 
 */
@Entity
@Table(name = "fce_licenciamento_mineral_metodo_lavra")
@NamedQuery(name = "FceLicenciamentoMineralMetodoLavra.removeByIdeFceLicenciamentoMineral", query = "DELETE FROM FceLicenciamentoMineralMetodoLavra f WHERE f.fceLicenciamentoMineral.ideFceLicenciamentoMineral = :ideFceLicenciamentoMineral")
public class FceLicenciamentoMineralMetodoLavra implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private FceLicenciamentoMineralMetodoLavraPK ideFceLicenciamentoMineralMetodoLavraPK;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_fce_licenciamento_mineral", nullable = false, insertable = false, updatable = false)
	private FceLicenciamentoMineral fceLicenciamentoMineral;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_metodo_lavra", nullable = false, insertable = false, updatable = false)
	private MetodoLavra ideMetodoLavra;

	public FceLicenciamentoMineralMetodoLavra() {

	}

	public FceLicenciamentoMineralMetodoLavra(FceLicenciamentoMineral fceLicenciamentoMineral, MetodoLavra ideMetodoLavra) {
		this.fceLicenciamentoMineral = fceLicenciamentoMineral;
		this.ideMetodoLavra = ideMetodoLavra;
		this.ideFceLicenciamentoMineralMetodoLavraPK = new FceLicenciamentoMineralMetodoLavraPK(fceLicenciamentoMineral, ideMetodoLavra);
	}

	public FceLicenciamentoMineral getFceLicenciamentoMineral() {
		return fceLicenciamentoMineral;
	}

	public void setFceLicenciamentoMineral(FceLicenciamentoMineral fceLicenciamentoMineral) {
		this.fceLicenciamentoMineral = fceLicenciamentoMineral;
	}

	public MetodoLavra getIdeMetodoLavra() {
		return ideMetodoLavra;
	}

	public void setIdeMetodoLavra(MetodoLavra ideMetodoLavra) {
		this.ideMetodoLavra = ideMetodoLavra;
	}

}
