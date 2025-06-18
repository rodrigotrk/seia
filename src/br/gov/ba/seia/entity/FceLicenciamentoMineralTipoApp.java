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
 * The persistent class for the fce_licenciamento_mineral_tipo_app database
 * table.
 * 
 */
@Entity
@Table(name = "fce_licenciamento_mineral_tipo_app")
@NamedQuery(name = "FceLicenciamentoMineralTipoApp.removeByIdeFceLicenciamentoMineral", query = "DELETE FROM FceLicenciamentoMineralTipoApp f WHERE f.fceLicenciamentoMineral.ideFceLicenciamentoMineral = :ideFceLicenciamentoMineral")
public class FceLicenciamentoMineralTipoApp implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private FceLicenciamentoMineralTipoAppPK ideFceLicenciamentoMineralTipoAppPK;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_fce_licenciamento_mineral", nullable = false, insertable = false, updatable = false)
	private FceLicenciamentoMineral fceLicenciamentoMineral;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_tipo_app", nullable = false, insertable = false, updatable = false)
	private TipoApp tipoApp;

	public FceLicenciamentoMineralTipoApp() {
	}

	public FceLicenciamentoMineralTipoApp(FceLicenciamentoMineral fceLicenciamentoMineral, TipoApp tipoApp) {
		this.fceLicenciamentoMineral = fceLicenciamentoMineral;
		this.tipoApp = tipoApp;
	}

	public FceLicenciamentoMineral getFceLicenciamentoMineral() {
		return fceLicenciamentoMineral;
	}

	public void setFceLicenciamentoMineral(FceLicenciamentoMineral fceLicenciamentoMineral) {
		this.fceLicenciamentoMineral = fceLicenciamentoMineral;
	}

	public TipoApp getTipoApp() {
		return tipoApp;
	}

	public void setTipoApp(TipoApp tipoApp) {
		this.tipoApp = tipoApp;
	}

	public FceLicenciamentoMineralTipoAppPK getIdeFceLicenciamentoMineralTipoAppPK() {
		return ideFceLicenciamentoMineralTipoAppPK;
	}

	public void setIdeFceLicenciamentoMineralTipoAppPK(FceLicenciamentoMineralTipoAppPK ideFceLicenciamentoMineralTipoAppPK) {
		this.ideFceLicenciamentoMineralTipoAppPK = ideFceLicenciamentoMineralTipoAppPK;
	}

}