package br.gov.ba.seia.dto;

import br.gov.ba.seia.entity.MetodoIrrigacao;
import br.gov.ba.seia.entity.TipologiaAtividade;

public class TipologiaAtividadeAgriculturaDTO {

	private TipologiaAtividade tipologiaAtividade;
	private String dscMeses;
	private String numMeses;
	private MetodoIrrigacao metodoIrrigacao;
	
	public TipologiaAtividadeAgriculturaDTO(){
		tipologiaAtividade = null;
		dscMeses = null;
		numMeses = null;
		metodoIrrigacao = null;
	}

	public TipologiaAtividade getTipologiaAtividade() {
		return tipologiaAtividade;
	}

	public void setTipologiaAtividade(TipologiaAtividade tipologiaAtividade) {
		this.tipologiaAtividade = tipologiaAtividade;
	}

	public String getDscMeses() {
		return dscMeses;
	}

	public void setDscMeses(String dscMeses) {
		this.dscMeses = dscMeses;
	}	

	public MetodoIrrigacao getMetodoIrrigacao() {
		return metodoIrrigacao;
	}

	public void setMetodoIrrigacao(MetodoIrrigacao metodoIrrigacao) {
		this.metodoIrrigacao = metodoIrrigacao;
	}

	public String getNumMeses() {
		return numMeses;
	}

	public void setNumMeses(String numMeses) {
		this.numMeses = numMeses;
	}

}
