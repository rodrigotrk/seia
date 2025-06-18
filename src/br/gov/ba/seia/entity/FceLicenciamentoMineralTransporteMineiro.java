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
 * The persistent class for the fce_licenciamento_mineral_transporte_minerio
 * database table.
 * 
 */
@Entity
@Table(name = "fce_licenciamento_mineral_transporte_minerio")
@NamedQuery(name = "FceLicenciamentoMineralTransporteMineiro.removeByIdeFceLicenciamentoMineral", query = "DELETE FROM FceLicenciamentoMineralTransporteMineiro f WHERE f.fceLicenciamentoMineral.ideFceLicenciamentoMineral = :ideFceLicenciamentoMineral")
public class FceLicenciamentoMineralTransporteMineiro implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private FceLicenciamentoMineralTransporteMineiroPK ideFceLicenciamentoMineralTransporteMineiroPK;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_fce_licenciamento_mineral", nullable = false, insertable = false, updatable = false)
	private FceLicenciamentoMineral fceLicenciamentoMineral;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_tipo_transporte_minerio", nullable = false, insertable = false, updatable = false)
	private TipoTransporteMinerio ideTipoTransporteMinerio;

	public FceLicenciamentoMineralTransporteMineiro() {

	}

	public FceLicenciamentoMineralTransporteMineiro(FceLicenciamentoMineral fceLicenciamentoMineral, TipoTransporteMinerio tipoTransporteMinerio) {
		this.fceLicenciamentoMineral = fceLicenciamentoMineral;
		this.ideTipoTransporteMinerio = tipoTransporteMinerio;
		this.ideFceLicenciamentoMineralTransporteMineiroPK = new FceLicenciamentoMineralTransporteMineiroPK(fceLicenciamentoMineral, tipoTransporteMinerio);
	}

	public FceLicenciamentoMineral getFceLicenciamentoMineral() {
		return fceLicenciamentoMineral;
	}

	public void setFceLicenciamentoMineral(FceLicenciamentoMineral fceLicenciamentoMineral) {
		this.fceLicenciamentoMineral = fceLicenciamentoMineral;
	}

	public TipoTransporteMinerio getIdeTipoTransporteMinerio() {
		return ideTipoTransporteMinerio;
	}

	public void setIdeTipoTransporteMinerio(TipoTransporteMinerio ideTipoTransporteMinerio) {
		this.ideTipoTransporteMinerio = ideTipoTransporteMinerio;
	}


}