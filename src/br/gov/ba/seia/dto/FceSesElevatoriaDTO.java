package br.gov.ba.seia.dto;

import br.gov.ba.seia.entity.FceSesDadosElevatoria;
import br.gov.ba.seia.entity.LocalizacaoGeografica;

public class FceSesElevatoriaDTO {
	
	private FceSesDadosElevatoria dadosElevatoria;
	
	private LocalizacaoGeografica localizacaoElevatoria;
	
	private LocalizacaoGeografica localizacaoExtravazamento;
	
	private Boolean localizacaoFinal;
	
	
	
	public FceSesElevatoriaDTO() {
		
	}
	
	public FceSesElevatoriaDTO(FceSesDadosElevatoria dadosElevatoria,LocalizacaoGeografica localizacaoElevatoria,LocalizacaoGeografica localizacaoExtravazamento) {
	
		this.dadosElevatoria = dadosElevatoria;
		this.localizacaoElevatoria = localizacaoElevatoria;
		this.localizacaoExtravazamento = localizacaoExtravazamento;
		
	}
	
	public FceSesDadosElevatoria getDadosElevatoria() {
		return dadosElevatoria;
	}

	public void setDadosElevatoria(FceSesDadosElevatoria dadosElevatoria) {
		this.dadosElevatoria = dadosElevatoria;
	}

	public LocalizacaoGeografica getLocalizacaoElevatoria() {
		return localizacaoElevatoria;
	}

	public void setLocalizacaoElevatoria(LocalizacaoGeografica localizacaoElevatoria) {
		this.localizacaoElevatoria = localizacaoElevatoria;
	}

	public LocalizacaoGeografica getLocalizacaoExtravazamento() {
		return localizacaoExtravazamento;
	}

	public void setLocalizacaoExtravazamento(
			LocalizacaoGeografica localizacaoExtravazamento) {
		this.localizacaoExtravazamento = localizacaoExtravazamento;
	}

	public Boolean getLocalizacaoFinal() {
		return localizacaoFinal;
	}

	public void setLocalizacaoFinal(Boolean localizacaoFinal) {
		this.localizacaoFinal = localizacaoFinal;
	}
	
}
