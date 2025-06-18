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
 * The persistent class for the fce_licenciamento_mineral_tecnica_lavra database
 * table.
 * 
 */
@Entity
@Table(name = "fce_licenciamento_mineral_tecnica_lavra")
@NamedQuery(name = "FceLicenciamentoMineralTecnicaLavra.removeByIdeFceLicenciamentoMineral", query = "DELETE FROM FceLicenciamentoMineralTecnicaLavra f WHERE f.fceLicenciamentoMineral.ideFceLicenciamentoMineral = :ideFceLicenciamentoMineral")
public class FceLicenciamentoMineralTecnicaLavra implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private FceLicenciamentoMineralTecnicaLavraPK ideFceLicenciamentoMineralMetodoLavraPK;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_fce_licenciamento_mineral", nullable = false, insertable = false, updatable = false)
	private FceLicenciamentoMineral fceLicenciamentoMineral;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_tecnica_lavra", nullable = false, insertable = false, updatable = false)
	private TecnicaLavra ideTecnicaLavra;

	public FceLicenciamentoMineralTecnicaLavra() {

	}

	public FceLicenciamentoMineralTecnicaLavra(FceLicenciamentoMineral fceLicenciamentoMineral, TecnicaLavra ideTecnicaLavra) {
		this.fceLicenciamentoMineral = fceLicenciamentoMineral;
		this.ideTecnicaLavra = ideTecnicaLavra;
		this.ideFceLicenciamentoMineralMetodoLavraPK = new FceLicenciamentoMineralTecnicaLavraPK(fceLicenciamentoMineral, ideTecnicaLavra);
	}

	public FceLicenciamentoMineral getFceLicenciamentoMineral() {
		return fceLicenciamentoMineral;
	}

	public void setFceLicenciamentoMineral(FceLicenciamentoMineral fceLicenciamentoMineral) {
		this.fceLicenciamentoMineral = fceLicenciamentoMineral;
	}

	public TecnicaLavra getIdeTecnicaLavra() {
		return ideTecnicaLavra;
	}

	public void setIdeTecnicaLavra(TecnicaLavra ideTecnicaLavra) {
		this.ideTecnicaLavra = ideTecnicaLavra;
	}

}
