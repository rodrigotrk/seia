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
 * The persistent class for the fce_licenciamento_mineral_tipo_residuo database
 * table.
 * 
 */
@Entity
@Table(name = "fce_licenciamento_mineral_tipo_residuo")
@NamedQuery(name = "FceLicenciamentoMineralTipoResiduo.removeByIdeFceLicenciamentoMineral", query = "DELETE FROM FceLicenciamentoMineralTipoResiduo f WHERE f.fceLicenciamentoMineral.ideFceLicenciamentoMineral = :ideFceLicenciamentoMineral")
public class FceLicenciamentoMineralTipoResiduo implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private FceLicenciamentoMineralTipoResiduoPK ideFceLicenciamentoMineralTipoResiduoPK;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_fce_licenciamento_mineral", nullable = false, insertable = false, updatable = false)
	private FceLicenciamentoMineral fceLicenciamentoMineral;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_tipo_residuo_gerado", nullable = false, insertable = false, updatable = false)
	private TipoResiduoGerado tipoResiduoGerado;

	public FceLicenciamentoMineralTipoResiduo() {
	}

	public FceLicenciamentoMineralTipoResiduo(FceLicenciamentoMineral fceLicenciamentoMineral, TipoResiduoGerado tipoResiduoGerado) {
		this.fceLicenciamentoMineral = fceLicenciamentoMineral;
		this.tipoResiduoGerado = tipoResiduoGerado;
		this.ideFceLicenciamentoMineralTipoResiduoPK = new FceLicenciamentoMineralTipoResiduoPK(fceLicenciamentoMineral, tipoResiduoGerado);
	}

	public FceLicenciamentoMineral getFceLicenciamentoMineral() {
		return fceLicenciamentoMineral;
	}

	public void setFceLicenciamentoMineral(FceLicenciamentoMineral fceLicenciamentoMineral) {
		this.fceLicenciamentoMineral = fceLicenciamentoMineral;
	}

	public TipoResiduoGerado getTipoResiduoGerado() {
		return tipoResiduoGerado;
	}

	public void setTipoResiduoGerado(TipoResiduoGerado tipoResiduoGerado) {
		this.tipoResiduoGerado = tipoResiduoGerado;
	}

}