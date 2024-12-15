package br.gov.ba.seia.dto;

import java.util.Calendar;
import java.util.Date;

public class TccaRenovarPrazoDTO {

	private String numTcca;
	private Date dtcPublicacao;
	private Integer qtdMesesPrazoValidade;
	private Date dtcVencimento;
	
	private Date dtcRenovacao;
	private Integer qtdMesesPrazoValidadeRenovacao;
	private Date newDtcVencimento;
	
	public TccaRenovarPrazoDTO() {}
	
	public Date getDtcVencimento() {
		
		if(dtcPublicacao != null) {
			Calendar c = Calendar.getInstance();
			c.setTime(dtcPublicacao);		
			c.add(Calendar.MONTH, + qtdMesesPrazoValidade );
			
			dtcVencimento = c.getTime();
		}
		
		return dtcVencimento;
	}

	public String getNumTcca() {
		return numTcca;
	}

	public void setNumTcca(String numTcca) {
		this.numTcca = numTcca;
	}

	public Date getDtcPublicacao() {
		return dtcPublicacao;
	}

	public void setDtcPublicacao(Date dtcPublicacao) {
		this.dtcPublicacao = dtcPublicacao;
	}

	public Integer getQtdMesesPrazoValidade() {
		return qtdMesesPrazoValidade;
	}

	public void setQtdMesesPrazoValidade(Integer qtdMesesPrazoValidade) {
		this.qtdMesesPrazoValidade = qtdMesesPrazoValidade;
	}

	public void setDtcVencimento(Date dtcVencimento) {
		this.dtcVencimento = dtcVencimento;
	}

	public Date getDtcRenovacao() {
		return dtcRenovacao;
	}

	public void setDtcRenovacao(Date dtcRenovacao) {
		this.dtcRenovacao = dtcRenovacao;
	}

	public Integer getQtdMesesPrazoValidadeRenovacao() {
		return qtdMesesPrazoValidadeRenovacao;
	}

	public void setQtdMesesPrazoValidadeRenovacao(Integer qtdMesesPrazoValidadeRenovacao) {
		this.qtdMesesPrazoValidadeRenovacao = qtdMesesPrazoValidadeRenovacao;
	}

	public Date getNewDtcVencimento() {
		return newDtcVencimento;
	}

	public void setNewDtcVencimento(Date newDtcVencimento) {
		this.newDtcVencimento = newDtcVencimento;
	}
}