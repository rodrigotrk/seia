package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.validation.constraints.NotNull;

public class OutorgaLocalizacaoGeograficaImovelPK implements Serializable {

	private static final long serialVersionUID = 8L;

	@Basic(optional = false)
	@NotNull
	@Column(name = "ide_outorga_localizacao_geografica", nullable = false)
	private int ideOutorgaLocalizacaoGeografica;

	@Basic(optional = false)
	@NotNull
	@Column(name = "ide_imovel", nullable = false)
	private int ideImovel;

	public OutorgaLocalizacaoGeograficaImovelPK() {

	}

	public OutorgaLocalizacaoGeograficaImovelPK(int ideOutorgaLocalizacaoGeografica, int ideImovel) {
		this.ideOutorgaLocalizacaoGeografica = ideOutorgaLocalizacaoGeografica;
		this.ideImovel = ideImovel;
	}

	public int getIdeOutorgaLocalizacaoGeografica() {
		return ideOutorgaLocalizacaoGeografica;
	}

	public void setIdeOutorgaLocalizacaoGeografica(int ideOutorgaLocalizacaoGeografica) {
		this.ideOutorgaLocalizacaoGeografica = ideOutorgaLocalizacaoGeografica;
	}

	public int getIdeImovel() {
		return ideImovel;
	}

	public void setIdeImovel(int ideImovel) {
		this.ideImovel = ideImovel;
	}

}
