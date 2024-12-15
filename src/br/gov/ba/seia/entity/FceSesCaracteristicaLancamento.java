package br.gov.ba.seia.entity;

import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="fce_ses_caracteristica_lancamento")
public class FceSesCaracteristicaLancamento {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fce_ses_caracteristica_lancamento_seq")
	@SequenceGenerator(name = "fce_ses_caracteristica_lancamento_seq", sequenceName = "fce_ses_caracteristica_lancamento_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name="ide_fce_ses_caracteristica_lancamento", nullable = false)
	private Integer ideFceSesCaracteristicaLancamento;
	
	@Column(name="val_efluente_tratado")
	private BigDecimal valorEfluenteTratado;
	
	@Column(name="val_eficiencia_remocao")
	private BigDecimal ValorEficienciRemocao;
	
	@Column(name="val_bruto_efluente")
	private BigDecimal valorBrutoEfluente;
	
	@JoinColumn(name="ide_caracteristica_efluente",referencedColumnName = "ide_caracteristica_efluente", nullable = false)
	@OneToOne(fetch = FetchType.LAZY)
	private CaracteristicaEfluente ideCaracteristicaEfluente;
	
	@JoinColumn(name="ide_fce_ses_lancamento_efluente",referencedColumnName = "ide_fce_ses_lancamento_efluente", nullable = false)
	@OneToOne(fetch = FetchType.LAZY)
	private FceSesCoordenadasLancamentoLocalizacaoGeografica ideCoordenadaFceLancamentoLocalizacaoGeografica;

	@Transient
	private boolean confirmado;
	
	
	public FceSesCaracteristicaLancamento(CaracteristicaEfluente caracteristicaEfluente, FceSesCoordenadasLancamentoLocalizacaoGeografica lancamentoEfluente) {
		this.ideCaracteristicaEfluente = caracteristicaEfluente;
		this.ideCoordenadaFceLancamentoLocalizacaoGeografica = lancamentoEfluente;
	}
	public FceSesCaracteristicaLancamento() {

	}
	
	public Integer getIdeFceSesCaracteristicaLancamento() {
		return ideFceSesCaracteristicaLancamento;
	}

	public void setIdeFceSesCaracteristicaLancamento(
			Integer ideFceSesCaracteristicaLancamento) {
		this.ideFceSesCaracteristicaLancamento = ideFceSesCaracteristicaLancamento;
	}

	public BigDecimal getValorEfluenteTratado() {
		return valorEfluenteTratado;
	}

	public void setValorEfluenteTratado(BigDecimal valorEfluenteTratado) {
		this.valorEfluenteTratado = valorEfluenteTratado;
	}

	public BigDecimal getValorEficienciRemocao() {
		return ValorEficienciRemocao;
	}

	public void setValorEficienciRemocao(BigDecimal valorEficienciRemocao) {
		ValorEficienciRemocao = valorEficienciRemocao;
	}

	public BigDecimal getValorBrutoEfluente() {
		return valorBrutoEfluente;
	}

	public void setValorBrutoEfluente(BigDecimal valorBrutoEfluente) {
		this.valorBrutoEfluente = valorBrutoEfluente;
	}

	public CaracteristicaEfluente getIdeCaracteristicaEfluente() {
		return ideCaracteristicaEfluente;
	}

	public void setIdeCaracteristicaEfluente(
			CaracteristicaEfluente ideCaracteristicaEfluente) {
		this.ideCaracteristicaEfluente = ideCaracteristicaEfluente;
	}

	public boolean isConfirmado() {
		return confirmado;
	}

	public void setConfirmado(boolean confirmado) {
		this.confirmado = confirmado;
	}
	public FceSesCoordenadasLancamentoLocalizacaoGeografica getIdeCoordenadaFceLancamentoLocalizacaoGeografica() {
		return ideCoordenadaFceLancamentoLocalizacaoGeografica;
	}
	public void setIdeCoordenadaFceLancamentoLocalizacaoGeografica(
			FceSesCoordenadasLancamentoLocalizacaoGeografica ideCoordenadaFceLancamentoLocalizacaoGeografica) {
		this.ideCoordenadaFceLancamentoLocalizacaoGeografica = ideCoordenadaFceLancamentoLocalizacaoGeografica;
	}

}
