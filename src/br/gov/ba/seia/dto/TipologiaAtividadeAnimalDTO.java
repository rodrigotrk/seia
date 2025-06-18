package br.gov.ba.seia.dto;

import br.gov.ba.seia.entity.Manejo;
import br.gov.ba.seia.entity.TipologiaAtividade;

public class TipologiaAtividadeAnimalDTO {

	private TipologiaAtividade tipologiaAtividade;
	private Integer qtdCabeca;
	private Integer tipoUsoAgua;
	private String dscTipoUsoAgua;
	private Manejo manejo;
	
	public TipologiaAtividadeAnimalDTO() {
		tipologiaAtividade = null;
		qtdCabeca = null;
		tipoUsoAgua = null;
		dscTipoUsoAgua = null;
		manejo = null;
	}

	public TipologiaAtividade getTipologiaAtividade() {
		return tipologiaAtividade;
	}

	public void setTipologiaAtividade(TipologiaAtividade tipologiaAtividade) {
		this.tipologiaAtividade = tipologiaAtividade;
	}

	public Integer getQtdCabeca() {
		return qtdCabeca;
	}

	public void setQtdCabeca(Integer qtdCabeca) {
		this.qtdCabeca = qtdCabeca;
	}

	public Integer getTipoUsoAgua() {
		return tipoUsoAgua;
	}

	public void setTipoUsoAgua(Integer tipoUsoAgua) {
		this.tipoUsoAgua = tipoUsoAgua;
	}

	public Manejo getManejo() {
		return manejo;
	}

	public void setManejo(Manejo manejo) {
		this.manejo = manejo;
	}

	public String getDscTipoUsoAgua() {
		return dscTipoUsoAgua;
	}

	public void setDscTipoUsoAgua(String dscTipoUsoAgua) {
		this.dscTipoUsoAgua = dscTipoUsoAgua;
	}
	
}
