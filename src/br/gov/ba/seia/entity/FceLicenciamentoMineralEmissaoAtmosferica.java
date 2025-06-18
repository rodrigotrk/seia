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
 * The persistent class for the fce_licenciamento_mineral_emissao_atmosferica
 * database table.
 * 
 */
@Entity
@Table(name = "fce_licenciamento_mineral_emissao_atmosferica")
@NamedQuery(name = "FceLicenciamentoMineralEmissaoAtmosferica.removeByIdeFceLicenciamentoMineral", query = "DELETE FROM FceLicenciamentoMineralEmissaoAtmosferica f WHERE f.fceLicenciamentoMineral.ideFceLicenciamentoMineral = :ideFceLicenciamentoMineral")
public class FceLicenciamentoMineralEmissaoAtmosferica implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private FceLicenciamentoMineralEmissaoAtmosfericaPK ideFceLicenciamentoMineralEmissaoAtmosfericaPK;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_fce_licenciamento_mineral", nullable = false, insertable = false, updatable = false)
	private FceLicenciamentoMineral fceLicenciamentoMineral;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_tipo_emissao_atmosferica", nullable = false, insertable = false, updatable = false)
	private TipoEmissaoAtmosferica tipoEmissaoAtmosferica;

	public FceLicenciamentoMineralEmissaoAtmosferica() {

	}

	public FceLicenciamentoMineralEmissaoAtmosferica(FceLicenciamentoMineral fceLicenciamentoMineral, TipoEmissaoAtmosferica tipoEmissaoAtmosferica) {
		this.fceLicenciamentoMineral = fceLicenciamentoMineral;
		this.tipoEmissaoAtmosferica = tipoEmissaoAtmosferica;
		this.ideFceLicenciamentoMineralEmissaoAtmosfericaPK = new FceLicenciamentoMineralEmissaoAtmosfericaPK(tipoEmissaoAtmosferica, fceLicenciamentoMineral);
	}

	public FceLicenciamentoMineral getFceLicenciamentoMineral() {
		return fceLicenciamentoMineral;
	}

	public void setFceLicenciamentoMineral(FceLicenciamentoMineral fceLicenciamentoMineral) {
		this.fceLicenciamentoMineral = fceLicenciamentoMineral;
	}

	public TipoEmissaoAtmosferica getTipoEmissaoAtmosferica() {
		return tipoEmissaoAtmosferica;
	}

	public void setTipoEmissaoAtmosferica(TipoEmissaoAtmosferica tipoEmissaoAtmosferica) {
		this.tipoEmissaoAtmosferica = tipoEmissaoAtmosferica;
	}

}