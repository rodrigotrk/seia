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
 * The persistent class for the fce_licenciamento_exploracao_regime_exploracao
 * database table.
 * 
 */
@Entity
@Table(name = "fce_licenciamento_exploracao_regime_exploracao")
@NamedQuery(name = "FceLicenciamentoExploracaoRegimeExploracao.removeByIdeFceLicenciamentoMineral", query = "DELETE FROM FceLicenciamentoExploracaoRegimeExploracao f WHERE f.fceLicenciamentoMineral.ideFceLicenciamentoMineral = :ideFceLicenciamentoMineral")
public class FceLicenciamentoExploracaoRegimeExploracao implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private FceLicenciamentoExploracaoRegimeExploracaoPK ideFceLicenciamentoExploracaoRegimeExploracaoPK;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_fce_licenciamento_mineral", nullable = false, insertable = false, updatable = false)
	private FceLicenciamentoMineral fceLicenciamentoMineral;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_regime_exploracao", nullable = false, insertable = false, updatable = false)
	private RegimeExploracao ideRegimeExploracao;

	public FceLicenciamentoExploracaoRegimeExploracao() {

	}

	public FceLicenciamentoExploracaoRegimeExploracao(FceLicenciamentoMineral fceLicenciamentoMineral, RegimeExploracao ideRegimeExploracao) {
		this.fceLicenciamentoMineral = fceLicenciamentoMineral;
		this.ideRegimeExploracao = ideRegimeExploracao;
		this.ideFceLicenciamentoExploracaoRegimeExploracaoPK = new FceLicenciamentoExploracaoRegimeExploracaoPK(fceLicenciamentoMineral, ideRegimeExploracao);
	}

	public FceLicenciamentoMineral getFceLicenciamentoMineral() {
		return fceLicenciamentoMineral;
	}

	public void setFceLicenciamentoMineral(FceLicenciamentoMineral fceLicenciamentoMineral) {
		this.fceLicenciamentoMineral = fceLicenciamentoMineral;
	}

	public RegimeExploracao getIdeRegimeExploracao() {
		return ideRegimeExploracao;
	}

	public void setIdeRegimeExploracao(RegimeExploracao ideRegimeExploracao) {
		this.ideRegimeExploracao = ideRegimeExploracao;
	}

}