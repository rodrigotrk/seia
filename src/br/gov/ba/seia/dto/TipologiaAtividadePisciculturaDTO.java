package br.gov.ba.seia.dto;

import br.gov.ba.seia.entity.TipologiaAtividade;

public class TipologiaAtividadePisciculturaDTO {

	private TipologiaAtividade tipologiaAtividade;
	private Double volume;
	
	public TipologiaAtividadePisciculturaDTO() {
		this.setTipologiaAtividade(null);
		this.setVolume(null);
	}

	public TipologiaAtividade getTipologiaAtividade() {
		return tipologiaAtividade;
	}

	public void setTipologiaAtividade(TipologiaAtividade tipologiaAtividade) {
		this.tipologiaAtividade = tipologiaAtividade;
	}

	public Double getVolume() {
		return volume;
	}

	public void setVolume(Double volume) {
		this.volume = volume;
	}

}
