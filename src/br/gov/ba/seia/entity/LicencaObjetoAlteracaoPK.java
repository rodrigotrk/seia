package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the licenca_objeto_alteracao database table.
 * 
 */
@Embeddable
public class LicencaObjetoAlteracaoPK implements Serializable {
	// default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name = "ide_objeto_alteracao")
	private Integer ideObjetoAlteracao;

	@Column(name = "ide_licenca")
	private Integer ideLicenca;

	public LicencaObjetoAlteracaoPK() {
	}

	public LicencaObjetoAlteracaoPK(Integer licenca, Integer objeto) {
		this.ideLicenca = licenca;
		this.ideObjetoAlteracao = objeto;
	}

	public Integer getIdeObjetoAlteracao() {
		return this.ideObjetoAlteracao;
	}

	public void setIdeObjetoAlteracao(Integer ideObjetoAlteracao) {
		this.ideObjetoAlteracao = ideObjetoAlteracao;
	}

	public Integer getIdeLicenca() {
		return this.ideLicenca;
	}

	public void setIdeLicenca(Integer ideLicenca) {
		this.ideLicenca = ideLicenca;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof LicencaObjetoAlteracaoPK)) {
			return false;
		}
		LicencaObjetoAlteracaoPK castOther = (LicencaObjetoAlteracaoPK) other;
		return this.ideObjetoAlteracao.equals(castOther.ideObjetoAlteracao)
				&& this.ideLicenca.equals(castOther.ideLicenca);

	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.ideObjetoAlteracao.hashCode();
		hash = hash * prime + this.ideLicenca.hashCode();

		return hash;
	}
}