package br.gov.ba.seia.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.gov.ba.seia.entity.generic.AbstractEntity;

@Entity
@Table(name="tcca_saldo")
@NamedQuery(name="TccaSaldo.findAll", query="SELECT t FROM TccaSaldo t")
public class TccaSaldo extends AbstractEntity {
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name="TCCA_SALDO_IDETCCASALDO_GENERATOR", sequenceName="TCCA_SALDO_IDE_TCCA_SALDO_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TCCA_SALDO_IDETCCASALDO_GENERATOR")
	@Column(name="ide_tcca_saldo", updatable=false, unique=true, nullable=false)
	private Integer ideTccaSaldo;
	
	@Column(name="val_saldo_nao_destinado_projeto", nullable=false, precision=20, scale=2)
	private BigDecimal valSaldoNaoDestinadoProjeto;
	
	@Column(name="val_saldo_suplementado", nullable=false, precision=20, scale=2)
	private BigDecimal valSaldoSuplementado;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_movimentacao_financeira", nullable=false)
	private MovimentacaoFinanceira ideMovimentacaoFinanceira;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_tcca", nullable=false)
	private Tcca ideTcca;
	
	@Transient
	private BigDecimal valSaldoParaExecucao;
	
	public TccaSaldo() {
	}
	
	public BigDecimal getValSaldoParaExecucao() {
		
		if(valSaldoNaoDestinadoProjeto != null && valSaldoSuplementado != null) {
			valSaldoParaExecucao = valSaldoNaoDestinadoProjeto.add(valSaldoSuplementado);
		}
		
		return valSaldoParaExecucao;
	}
	
	/*****************
	/*				 *
	// GETS AND SETS *
	/* 				 *
	/*****************/
	
	public Integer getIdeTccaSaldo() {
		return this.ideTccaSaldo;
	}
	
	public void setIdeTccaSaldo(Integer ideTccaSaldo) {
		this.ideTccaSaldo = ideTccaSaldo;
	}
	
	public BigDecimal getValSaldoNaoDestinadoProjeto() {
		return this.valSaldoNaoDestinadoProjeto;
	}
	
	public void setValSaldoNaoDestinadoProjeto(BigDecimal valSaldoNaoDestinadoProjeto) {
		this.valSaldoNaoDestinadoProjeto = valSaldoNaoDestinadoProjeto;
	}
	
	public BigDecimal getValSaldoSuplementado() {
		return this.valSaldoSuplementado;
	}
	
	public void setValSaldoSuplementado(BigDecimal valSaldoSuplementado) {
		this.valSaldoSuplementado = valSaldoSuplementado;
	}
	
	public MovimentacaoFinanceira getIdeMovimentacaoFinanceira() {
		return this.ideMovimentacaoFinanceira;
	}
	
	public void setIdeMovimentacaoFinanceira(MovimentacaoFinanceira ideMovimentacaoFinanceira) {
		this.ideMovimentacaoFinanceira = ideMovimentacaoFinanceira;
	}
	
	public Tcca getIdeTcca() {
		return this.ideTcca;
	}
	
	public void setIdeTcca(Tcca ideTcca) {
		this.ideTcca = ideTcca;
	}
	
	public void setValSaldoParaExecucao(BigDecimal valSaldoParaExecucao) {
		this.valSaldoParaExecucao = valSaldoParaExecucao;
	}
}