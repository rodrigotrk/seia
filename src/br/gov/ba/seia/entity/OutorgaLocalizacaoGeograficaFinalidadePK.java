package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.validation.constraints.NotNull;

public class OutorgaLocalizacaoGeograficaFinalidadePK implements Serializable {

	private static final long serialVersionUID = 8L;

	@Basic(optional = false)
	@NotNull
	@Column(name = "ide_outorga_localizacao_geografica", nullable = false)
	private int ideOutorgaLocalizacaoGeografica;

	@Basic(optional = false)
	@NotNull
	@Column(name = "ide_tipo_finalidade_uso_agua", nullable = false)
	private int ideTipoFinalidadeUsoAgua;

	public OutorgaLocalizacaoGeograficaFinalidadePK() {

	}

	public OutorgaLocalizacaoGeograficaFinalidadePK(int ideOutorgaLocalizacaoGeografica, int ideTipoFinalidadeUsoAgua) {
		this.ideOutorgaLocalizacaoGeografica = ideOutorgaLocalizacaoGeografica;
		this.ideTipoFinalidadeUsoAgua = ideTipoFinalidadeUsoAgua;
	}

	public int getIdeOutorgaLocalizacaoGeografica() {
		return ideOutorgaLocalizacaoGeografica;
	}

	public void setIdeOutorgaLocalizacaoGeografica(int ideOutorgaLocalizacaoGeografica) {
		this.ideOutorgaLocalizacaoGeografica = ideOutorgaLocalizacaoGeografica;
	}

	public int getIdeTipoFinalidadeUsoAgua() {
		return ideTipoFinalidadeUsoAgua;
	}

	public void setIdeTipoFinalidadeUsoAgua(int ideTipoFinalidadeUsoAgua) {
		this.ideTipoFinalidadeUsoAgua = ideTipoFinalidadeUsoAgua;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ideOutorgaLocalizacaoGeografica;
		result = prime * result + ideTipoFinalidadeUsoAgua;
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
		OutorgaLocalizacaoGeograficaFinalidadePK other = (OutorgaLocalizacaoGeograficaFinalidadePK) obj;
		if (ideOutorgaLocalizacaoGeografica != other.ideOutorgaLocalizacaoGeografica)
			return false;
		if (ideTipoFinalidadeUsoAgua != other.ideTipoFinalidadeUsoAgua)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "br.gov.ba.seia.entity.OutorgaLocalizacaoGeograficaFinalidadePK [ideOutorgaLocalizacaoGeografica="
				+ ideOutorgaLocalizacaoGeografica + ", ideTipoFinalidadeUsoAgua=" + ideTipoFinalidadeUsoAgua + "]";
	}

}