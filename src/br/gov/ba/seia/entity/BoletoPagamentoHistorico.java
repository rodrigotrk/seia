package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Entidade da table boleto_pagamento_historico
 * 
 * @author Vitor Alexis de Almeida Leitao (vitor.leitao@zcr.com.br)
 * @since 21/11/2013
 */
@Entity
@Table(name = "boleto_pagamento_historico")
@XmlRootElement
public class BoletoPagamentoHistorico implements Serializable, Comparable<BoletoPagamentoHistorico>{

	private static final long serialVersionUID = 2995883701287027646L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "boleto_pagamento_historico_ide_boleto_pagamento_historico_seq")
	@SequenceGenerator(name = "boleto_pagamento_historico_ide_boleto_pagamento_historico_seq", sequenceName = "boleto_pagamento_historico_ide_boleto_pagamento_historico_seq", allocationSize = 1)
	@Column(name = "ide_boleto_pagamento_historico", nullable = false)
	private Integer ideBoletoPagamentoHistorico;

	@JoinColumn(name = "ide_boleto_pagamento", referencedColumnName = "ide_boleto_pagamento_requerimento", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private BoletoPagamentoRequerimento ideBoletoPagamento;

	@JoinColumn(name = "ide_status_boleto_pagamento", referencedColumnName = "ide_status_boleto_pagamento", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private StatusBoletoPagamento ideStatusBoletoPagamento;

	@Column(name = "dtc_tramitacao", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcTramitacao;

	@JoinColumn(name = "ide_pessoa", referencedColumnName = "ide_pessoa", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private Pessoa idePessoa;

	@JoinColumn(name = "ide_motivo_cancelamento_boleto", referencedColumnName = "ide_motivo_cancelamento_boleto", nullable = true)
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	private MotivoCancelamentoBoleto ideMotivoCancelamentoBoleto;

	@Column(name = "dsc_observacao", nullable = true)
	private String dscObservacao;
	
	public BoletoPagamentoHistorico(){
		super();
	}
	
	public BoletoPagamentoHistorico(Integer ideBoletoPagamentoHistorico){
		this.ideBoletoPagamentoHistorico = ideBoletoPagamentoHistorico;
	}
	
	public BoletoPagamentoHistorico(BoletoPagamentoRequerimento ideBoletoPagamento, MotivoCancelamentoBoleto ideMotivoCancelamentoBoleto){
		this.ideBoletoPagamento = ideBoletoPagamento;
		this.ideMotivoCancelamentoBoleto = ideMotivoCancelamentoBoleto;
	}
	
	public BoletoPagamentoHistorico(BoletoPagamentoRequerimento ideBoletoPagamento, Integer statusBoletoPagamento, Date dtcTramitacao, Pessoa idePessoa){
		this.ideBoletoPagamento = ideBoletoPagamento;
		this.dtcTramitacao = dtcTramitacao;
		this.ideStatusBoletoPagamento = new StatusBoletoPagamento(statusBoletoPagamento);
		this.idePessoa = idePessoa;
	}

	/**
	 * @return the ideBoletoPagamentoHistorico
	 */
	public Integer getIdeBoletoPagamentoHistorico() {
	
		return ideBoletoPagamentoHistorico;
	}

	/**
	 * @param ideBoletoPagamentoHistorico the ideBoletoPagamentoHistorico to set
	 */
	public void setIdeBoletoPagamentoHistorico(Integer ideBoletoPagamentoHistorico) {
	
		this.ideBoletoPagamentoHistorico = ideBoletoPagamentoHistorico;
	}

	/**
	 * @return the ideBoletoPagamento
	 */
	public BoletoPagamentoRequerimento getIdeBoletoPagamento() {
	
		return ideBoletoPagamento;
	}

	/**
	 * @param ideBoletoPagamento the ideBoletoPagamento to set
	 */
	public void setIdeBoletoPagamento(BoletoPagamentoRequerimento ideBoletoPagamento) {
	
		this.ideBoletoPagamento = ideBoletoPagamento;
	}

	/**
	 * @return the ideStatusBoletoPagamento
	 */
	public StatusBoletoPagamento getIdeStatusBoletoPagamento() {
	
		return ideStatusBoletoPagamento;
	}

	/**
	 * @param ideStatusBoletoPagamento the ideStatusBoletoPagamento to set
	 */
	public void setIdeStatusBoletoPagamento(StatusBoletoPagamento ideStatusBoletoPagamento) {
	
		this.ideStatusBoletoPagamento = ideStatusBoletoPagamento;
	}

	/**
	 * @return the dtcTramitacao
	 */
	public Date getDtcTramitacao() {
	
		return dtcTramitacao;
	}

	/**
	 * @param dtcTramitacao the dtcTramitacao to set
	 */
	public void setDtcTramitacao(Date dtcTramitacao) {
	
		this.dtcTramitacao = dtcTramitacao;
	}

	/**
	 * @return the idePessoa
	 */
	public Pessoa getIdePessoa() {
	
		return idePessoa;
	}

	/**
	 * @param idePessoa the idePessoa to set
	 */
	public void setIdePessoa(Pessoa idePessoa) {
	
		this.idePessoa = idePessoa;
	}

	/**
	 * @return the ideMotivoCancelmanetoBot
	 */
	public MotivoCancelamentoBoleto getIdeMotivoCancelamentoBoleto() {
	
		return ideMotivoCancelamentoBoleto;
	}

	/**
	 * @param ideMotivoCancelmanetoBot the ideMotivoCancelmanetoBot to set
	 */
	public void setIdeMotivoCancelamentoBoleto(MotivoCancelamentoBoleto ideMotivoCancelmanetoBot) {
	
		this.ideMotivoCancelamentoBoleto = ideMotivoCancelmanetoBot;
	}

	/**
	 * @return the dscObservacao
	 */
	public String getDscObservacao() {
	
		return dscObservacao;
	}

	/**
	 * @param dscObservacao the dscObservacao to set
	 */
	public void setDscObservacao(String dscObservacao) {
	
		this.dscObservacao = dscObservacao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideBoletoPagamentoHistorico == null) ? 0 : ideBoletoPagamentoHistorico.hashCode());
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
		BoletoPagamentoHistorico other = (BoletoPagamentoHistorico) obj;
		if (ideBoletoPagamentoHistorico == null) {
			if (other.ideBoletoPagamentoHistorico != null)
				return false;
		} else if (!ideBoletoPagamentoHistorico.equals(other.ideBoletoPagamentoHistorico))
			return false;
		return true;
	}

	@Override
	public int compareTo(BoletoPagamentoHistorico o) {
			if(this.ideBoletoPagamentoHistorico != null){
				return this.ideBoletoPagamentoHistorico.intValue();
			}else			
				return 0;
	}
}