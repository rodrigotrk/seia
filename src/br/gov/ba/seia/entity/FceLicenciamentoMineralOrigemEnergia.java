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
 * The persistent class for the fce_licenciamento_mineral_origem_energia
 * database table.
 * 
 */
@Entity
@Table(name = "fce_licenciamento_mineral_origem_energia")
@NamedQuery(name = "FceLicenciamentoMineralOrigemEnergia.removeByIdeFceLicenciamentoMineral", query = "DELETE FROM FceLicenciamentoMineralOrigemEnergia f WHERE f.fceLicenciamentoMineral.ideFceLicenciamentoMineral = :ideFceLicenciamentoMineral")
public class FceLicenciamentoMineralOrigemEnergia implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private FceLicenciamentoMineralOrigemEnergiaPK ideFceLicenciamentoMineralOrigemEnergiaPK;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_fce_licenciamento_mineral", nullable = false, insertable = false, updatable = false)
	private FceLicenciamentoMineral fceLicenciamentoMineral;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_tipo_origem_energia", nullable = false, insertable = false, updatable = false)
	private TipoOrigemEnergia ideTipoOrigemEnergia;

	public FceLicenciamentoMineralOrigemEnergia() {

	}

	public FceLicenciamentoMineralOrigemEnergia(FceLicenciamentoMineral fceLicenciamentoMineral, TipoOrigemEnergia ideTipoOrigemEnergia) {
		this.fceLicenciamentoMineral = fceLicenciamentoMineral;
		this.ideTipoOrigemEnergia = ideTipoOrigemEnergia;
		this.ideFceLicenciamentoMineralOrigemEnergiaPK = new FceLicenciamentoMineralOrigemEnergiaPK(fceLicenciamentoMineral, ideTipoOrigemEnergia);
	}

	public FceLicenciamentoMineral getFceLicenciamentoMineral() {
		return fceLicenciamentoMineral;
	}

	public void setFceLicenciamentoMineral(FceLicenciamentoMineral fceLicenciamentoMineral) {
		this.fceLicenciamentoMineral = fceLicenciamentoMineral;
	}

	public TipoOrigemEnergia getIdeTipoOrigemEnergia() {
		return ideTipoOrigemEnergia;
	}

	public void setIdeTipoOrigemEnergia(TipoOrigemEnergia ideTipoOrigemEnergia) {
		this.ideTipoOrigemEnergia = ideTipoOrigemEnergia;
	}
}