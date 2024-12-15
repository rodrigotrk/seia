package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "solicitacao_administrativo_tipo_prorrogacao_prazo_validade")
@XmlRootElement
@NamedQuery(name = "SolicitacaoAdmTipoPPValidade.removeByideSolicitacaoAdministrativo", query = "delete FROM SolicitacaoAdministrativoTipoProrrogacaoPrazoValidade sol WHERE sol.ideSolicitacaoAdministrativo = :ideSolicitacaoAdministrativo")
public class SolicitacaoAdministrativoTipoProrrogacaoPrazoValidade implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	protected SolicitacaoAdministrativoTipoProrrogacaoPrazoValidadePK solicitacaoAdministrativoTipoProrrogacaoPrazoValidadePK;

	@NotNull
	@JoinColumn(name = "ide_solicitacao_administrativo", referencedColumnName = "ide_solicitacao_administrativo", nullable = false, insertable = false, updatable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private SolicitacaoAdministrativo ideSolicitacaoAdministrativo;

	@NotNull
	@JoinColumn(name = "ide_tipo_prorrogacao_prazo_validade", referencedColumnName = "ide_tipo_prorrogacao_prazo_validade", nullable = false, insertable = false, updatable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private TipoProrrogacaoPrazoValidade ideTipoProrrogacaoPrazoValidade;

	@NotNull
	@JoinColumn(name = "ide_localizacao_geografica", referencedColumnName = "ide_localizacao_geografica", nullable = false, insertable = false, updatable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private LocalizacaoGeografica ideLocalizacaoGeografica;

	public SolicitacaoAdministrativoTipoProrrogacaoPrazoValidade() {
	}

	public SolicitacaoAdministrativoTipoProrrogacaoPrazoValidade(
			SolicitacaoAdministrativoTipoProrrogacaoPrazoValidadePK solicitacaoAdministrativoTipoProrrogacaoPrazoValidadePK) {
		this.solicitacaoAdministrativoTipoProrrogacaoPrazoValidadePK = solicitacaoAdministrativoTipoProrrogacaoPrazoValidadePK;
	}

	public SolicitacaoAdministrativoTipoProrrogacaoPrazoValidade(int ideSolicitacaoAdministrativo,
			int ideTipoProrrogacaoPrazoValidade, int ideLocalizacaoGeografica) {
		this.solicitacaoAdministrativoTipoProrrogacaoPrazoValidadePK = new SolicitacaoAdministrativoTipoProrrogacaoPrazoValidadePK(
				ideSolicitacaoAdministrativo, ideTipoProrrogacaoPrazoValidade, ideLocalizacaoGeografica);
	}

	public SolicitacaoAdministrativoTipoProrrogacaoPrazoValidadePK getSolicitacaoAdministrativoTipoProrrogacaoPrazoValidadePK() {
		return solicitacaoAdministrativoTipoProrrogacaoPrazoValidadePK;
	}

	public void setSolicitacaoAdministrativoTipoProrrogacaoPrazoValidadePK(
			SolicitacaoAdministrativoTipoProrrogacaoPrazoValidadePK solicitacaoAdministrativoTipoProrrogacaoPrazoValidadePK) {
		this.solicitacaoAdministrativoTipoProrrogacaoPrazoValidadePK = solicitacaoAdministrativoTipoProrrogacaoPrazoValidadePK;
	}

	public SolicitacaoAdministrativo getIdeSolicitacaoAdministrativo() {
		return ideSolicitacaoAdministrativo;
	}

	public void setIdeSolicitacaoAdministrativo(SolicitacaoAdministrativo ideSolicitacaoAdministrativo) {
		this.ideSolicitacaoAdministrativo = ideSolicitacaoAdministrativo;
	}

	public TipoProrrogacaoPrazoValidade getIdeTipoProrrogacaoPrazoValidade() {
		return ideTipoProrrogacaoPrazoValidade;
	}

	public void setIdeTipoProrrogacaoPrazoValidade(TipoProrrogacaoPrazoValidade ideTipoProrrogacaoPrazoValidade) {
		this.ideTipoProrrogacaoPrazoValidade = ideTipoProrrogacaoPrazoValidade;
	}

	public LocalizacaoGeografica getIdeLocalizacaoGeografica() {
		return ideLocalizacaoGeografica;
	}

	public void setIdeLocalizacaoGeografica(LocalizacaoGeografica ideLocalizacaoGeografica) {
		this.ideLocalizacaoGeografica = ideLocalizacaoGeografica;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((solicitacaoAdministrativoTipoProrrogacaoPrazoValidadePK == null) ? 0
						: solicitacaoAdministrativoTipoProrrogacaoPrazoValidadePK.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SolicitacaoAdministrativoTipoProrrogacaoPrazoValidade other = (SolicitacaoAdministrativoTipoProrrogacaoPrazoValidade) obj;
		if (solicitacaoAdministrativoTipoProrrogacaoPrazoValidadePK == null) {
			if (other.solicitacaoAdministrativoTipoProrrogacaoPrazoValidadePK != null)
				return false;
		} else if (!solicitacaoAdministrativoTipoProrrogacaoPrazoValidadePK
				.equals(other.solicitacaoAdministrativoTipoProrrogacaoPrazoValidadePK))
			return false;
		return true;
	}

}
