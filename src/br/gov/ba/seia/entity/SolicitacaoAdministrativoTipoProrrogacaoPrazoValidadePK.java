package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class SolicitacaoAdministrativoTipoProrrogacaoPrazoValidadePK implements Serializable {

	private static final long serialVersionUID = 8L;

	@Basic(optional = false)
	@NotNull
	@Column(name = "ide_solicitacao_administrativo", nullable = false)
	private int ideSolicitacaoAdministrativo;

	@Basic(optional = false)
	@NotNull
	@Column(name = "ide_tipo_prorrogacao_prazo_validade", nullable = false)
	private int ideTipoProrrogacaoPrazoValidade;

	@Basic(optional = false)
	@NotNull
	@Column(name = "ide_localizacao_geografica", nullable = false)
	private int ideLocalizacaoGeografica;

	public SolicitacaoAdministrativoTipoProrrogacaoPrazoValidadePK() {
	}

	public SolicitacaoAdministrativoTipoProrrogacaoPrazoValidadePK(int ideSolicitacaoAdministrativo,
			int ideTipoProrrogacaoPrazoValidade, int ideLocalizacaoGeografica) {
		this.ideSolicitacaoAdministrativo = ideSolicitacaoAdministrativo;
		this.ideTipoProrrogacaoPrazoValidade = ideTipoProrrogacaoPrazoValidade;
		this.ideLocalizacaoGeografica = ideLocalizacaoGeografica;
	}

	public int getIdeSolicitacaoAdministrativo() {
		return ideSolicitacaoAdministrativo;
	}

	public void setIdeSolicitacaoAdministrativo(int ideSolicitacaoAdministrativo) {
		this.ideSolicitacaoAdministrativo = ideSolicitacaoAdministrativo;
	}

	public int getIdeTipoProrrogacaoPrazoValidade() {
		return ideTipoProrrogacaoPrazoValidade;
	}

	public void setIdeTipoProrrogacaoPrazoValidade(int ideTipoProrrogacaoPrazoValidade) {
		this.ideTipoProrrogacaoPrazoValidade = ideTipoProrrogacaoPrazoValidade;
	}

	public int getIdeLocalizacaoGeografica() {
		return ideLocalizacaoGeografica;
	}

	public void setIdeLocalizacaoGeografica(int ideLocalizacaoGeografica) {
		this.ideLocalizacaoGeografica = ideLocalizacaoGeografica;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ideLocalizacaoGeografica;
		result = prime * result + ideSolicitacaoAdministrativo;
		result = prime * result + ideTipoProrrogacaoPrazoValidade;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof SolicitacaoAdministrativoTipoProrrogacaoPrazoValidadePK)) {
			return false;
		}
		SolicitacaoAdministrativoTipoProrrogacaoPrazoValidadePK other = (SolicitacaoAdministrativoTipoProrrogacaoPrazoValidadePK) obj;
		if (ideLocalizacaoGeografica != other.ideLocalizacaoGeografica) {
			return false;
		}
		if (ideSolicitacaoAdministrativo != other.ideSolicitacaoAdministrativo) {
			return false;
		}
		if (ideTipoProrrogacaoPrazoValidade != other.ideTipoProrrogacaoPrazoValidade) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "br.gov.ba.seia.entity.SolicitacaoAdministrativoTipoProrrogacaoPrazoValidadePK[ ideSolicitacaoAdministrativo="
				+ ideSolicitacaoAdministrativo
				+ ", ideTipoProrrogacaoPrazoValidade="
				+ ideTipoProrrogacaoPrazoValidade
				+ " ideLocalizacaoGeografica " + ideLocalizacaoGeografica + "  ]";
	}
}
