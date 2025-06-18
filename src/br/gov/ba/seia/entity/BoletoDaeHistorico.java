package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
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
 * Entidade da table boleto_dae_historico
 * 
 * @author micael.coutinho
 */
@Entity
@Table(name = "boleto_dae_historico")
@XmlRootElement
public class BoletoDaeHistorico implements Serializable, Comparable<BoletoDaeHistorico>{

	private static final long serialVersionUID = 2995883701287027646L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BOLETO_DAE_HISTORICO_IDE_BOLETO_DAE_HISTORICO_seq")
	@SequenceGenerator(name = "BOLETO_DAE_HISTORICO_IDE_BOLETO_DAE_HISTORICO_seq", sequenceName = "BOLETO_DAE_HISTORICO_IDE_BOLETO_DAE_HISTORICO_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name = "ide_boleto_dae_historico", nullable = false)
	private Integer ideBoletoDaeHistorico;

	@Basic(optional = false)
	@JoinColumn(name = "ide_boleto_dae_requerimento", referencedColumnName = "ide_boleto_dae_requerimento", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private BoletoDaeRequerimento ideBoletoDaeRequerimento;

	@Basic(optional = false)
	@JoinColumn(name = "ide_status_boleto_pagamento", referencedColumnName = "ide_status_boleto_pagamento", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private StatusBoletoPagamento ideStatusBoletoPagamento;

	@Basic(optional = false)
	@Column(name = "dtc_tramitacao", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcTramitacao;

	@Basic(optional = false)
	@JoinColumn(name = "ide_pessoa", referencedColumnName = "ide_pessoa", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private Pessoa idePessoa;

	@Basic(optional = true)
	@JoinColumn(name = "ide_motivo_cancelamento_boleto", referencedColumnName = "ide_motivo_cancelamento_boleto", nullable = true)
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	private MotivoCancelamentoBoleto ideMotivoCancelamentoBoleto;

	@Basic(optional = true)
	@Column(name = "dsc_observacao", nullable = true)
	private String dscObservacao;
	
	public BoletoDaeHistorico(){
		super();
	}
	
	public BoletoDaeHistorico(Integer ideBoletoDaeHistorico){
		this.ideBoletoDaeHistorico = ideBoletoDaeHistorico;
	}
	
	public BoletoDaeHistorico(BoletoDaeRequerimento ideBoletoDae, MotivoCancelamentoBoleto ideMotivoCancelamentoBoleto){
		this.ideBoletoDaeRequerimento = ideBoletoDae;
		this.ideMotivoCancelamentoBoleto = ideMotivoCancelamentoBoleto;
	}
	
	public BoletoDaeHistorico(BoletoDaeRequerimento ideBoletoDae, Integer statusBoletoPagamento, Date dtcTramitacao, Pessoa idePessoa){
		this.ideBoletoDaeRequerimento = ideBoletoDae;
		this.dtcTramitacao = dtcTramitacao;
		this.ideStatusBoletoPagamento = new StatusBoletoPagamento(statusBoletoPagamento);
		this.idePessoa = idePessoa;
	}

	/**
	 * @return the ideBoletoDaeHistorico
	 */
	public Integer getIdeBoletoDaeHistorico() {
	
		return ideBoletoDaeHistorico;
	}

	/**
	 * @param ideBoletoDaeHistorico the ideBoletoDaeHistorico to set
	 */
	public void setIdeBoletoDaeHistorico(Integer ideBoletoDaeHistorico) {
	
		this.ideBoletoDaeHistorico = ideBoletoDaeHistorico;
	}

	/**
	 * @return the ideBoletoDae
	 */
	public BoletoDaeRequerimento getIdeBoletoDaeRequerimento() {
	
		return ideBoletoDaeRequerimento;
	}

	/**
	 * @param ideBoletoDae the ideBoletoDae to set
	 */
	public void setIdeBoletoDaeRequerimento(BoletoDaeRequerimento ideBoletoDae) {
	
		this.ideBoletoDaeRequerimento = ideBoletoDae;
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
		result = prime * result + ((ideBoletoDaeHistorico == null) ? 0 : ideBoletoDaeHistorico.hashCode());
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
		BoletoDaeHistorico other = (BoletoDaeHistorico) obj;
		if (ideBoletoDaeHistorico == null) {
			if (other.ideBoletoDaeHistorico != null)
				return false;
		} else if (!ideBoletoDaeHistorico.equals(other.ideBoletoDaeHistorico))
			return false;
		return true;
	}

	@Override
	public int compareTo(BoletoDaeHistorico o) {
			if(this.ideBoletoDaeHistorico != null){
				return this.ideBoletoDaeHistorico.intValue();
			}else			
				return 0;
	}
}