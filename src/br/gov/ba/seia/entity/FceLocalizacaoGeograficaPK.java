package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class FceLocalizacaoGeograficaPK implements Serializable{
	private static final long serialVersionUID = 1L;

	@Basic(optional = false)
	@NotNull
	@Column(name="ide_fce")
	private Integer ideFce;

	@Basic(optional = false)
	@NotNull
	@Column(name="ide_localizacao_geografica")
	private Integer ideLocalizacaoGeografica;

	public FceLocalizacaoGeograficaPK(){

	}

	public FceLocalizacaoGeograficaPK(Fce fce, LocalizacaoGeografica localizacaoGeografica) {
		this.ideFce = fce.getIdeFce();
		this.ideLocalizacaoGeografica = localizacaoGeografica.getIdeLocalizacaoGeografica();
	}

	public FceLocalizacaoGeograficaPK(Integer ideFce, Integer ideLocalizacaoGeografica) {
		this.ideFce = ideFce;
		this.ideLocalizacaoGeografica = ideLocalizacaoGeografica;
	}

	public Integer getIdeFce() {
		return ideFce;
	}

	public void setIdeFce(Integer ideFce) {
		this.ideFce = ideFce;
	}

	public Integer getIdeLocalizacaoGeografica() {
		return ideLocalizacaoGeografica;
	}

	public void setIdeLocalizacaoGeografica(Integer ideLocalizacaoGeografica) {
		this.ideLocalizacaoGeografica = ideLocalizacaoGeografica;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ideFce == null) ? 0 : ideFce.hashCode());
		result = prime
				* result
				+ ((ideLocalizacaoGeografica == null) ? 0
						: ideLocalizacaoGeografica.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		FceLocalizacaoGeograficaPK other = (FceLocalizacaoGeograficaPK) obj;
		if (ideFce == null) {
			if (other.ideFce != null) {
				return false;
			}
		} else if (!ideFce.equals(other.ideFce)) {
			return false;
		}
		if (ideLocalizacaoGeografica == null) {
			if (other.ideLocalizacaoGeografica != null) {
				return false;
			}
		} else if (!ideLocalizacaoGeografica
				.equals(other.ideLocalizacaoGeografica)) {
			return false;
		}
		return true;
	}
}
