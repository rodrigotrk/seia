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
 * The persistent class for the fce_licenciamento_mineral_sistema_tratamento
 * database table.
 * 
 */
@Entity
@Table(name = "fce_licenciamento_mineral_sistema_tratamento")
@NamedQuery(name = "FceLicenciamentoMineralSistemaTratamento.removeByIdeFceLicenciamentoMineral", query = "DELETE FROM FceLicenciamentoMineralSistemaTratamento f WHERE f.fceLicenciamentoMineral.ideFceLicenciamentoMineral = :ideFceLicenciamentoMineral")
public class FceLicenciamentoMineralSistemaTratamento implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private FceLicenciamentoMineralSistemaTratamentoPK ideFceLicenciamentoMineralSistemaTratamentoPK;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_fce_licenciamento_mineral", nullable = false, insertable = false, updatable = false)
	private FceLicenciamentoMineral fceLicenciamentoMineral;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_tipo_sistema_tratamento", nullable = false, insertable = false, updatable = false)
	private TipoSistemaTratamento tipoSistemaTratamento;

	public FceLicenciamentoMineralSistemaTratamento() {

	}

	public FceLicenciamentoMineralSistemaTratamento(FceLicenciamentoMineral fceLicenciamentoMineral, TipoSistemaTratamento tipoSistemaTratamento) {
		this.fceLicenciamentoMineral = fceLicenciamentoMineral;
		this.tipoSistemaTratamento = tipoSistemaTratamento;
		this.ideFceLicenciamentoMineralSistemaTratamentoPK = new FceLicenciamentoMineralSistemaTratamentoPK(fceLicenciamentoMineral, tipoSistemaTratamento);
	}

	public FceLicenciamentoMineral getFceLicenciamentoMineral() {
		return fceLicenciamentoMineral;
	}

	public void setFceLicenciamentoMineral(FceLicenciamentoMineral fceLicenciamentoMineral) {
		this.fceLicenciamentoMineral = fceLicenciamentoMineral;
	}

	public TipoSistemaTratamento getTipoSistemaTratamento() {
		return tipoSistemaTratamento;
	}

	public void setTipoSistemaTratamento(TipoSistemaTratamento tipoSistemaTratamento) {
		this.tipoSistemaTratamento = tipoSistemaTratamento;
	}

}