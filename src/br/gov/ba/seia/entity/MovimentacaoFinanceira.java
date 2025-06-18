package br.gov.ba.seia.entity;

import java.math.BigDecimal;
import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import br.gov.ba.seia.entity.generic.AbstractEntity;

@Entity
@Table(name="movimentacao_financeira")
@NamedQuery(name="MovimentacaoFinanceira.findAll", query="SELECT m FROM MovimentacaoFinanceira m")
public class MovimentacaoFinanceira extends AbstractEntity {
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name="MOVIMENTACAO_FINANCEIRA_IDEMOVIMENTACAOFINANCEIRA_GENERATOR", sequenceName="MOVIMENTACAO_FINANCEIRA_IDE_MOVIMENTACAO_FINANCEIRA_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MOVIMENTACAO_FINANCEIRA_IDEMOVIMENTACAOFINANCEIRA_GENERATOR")
	@Column(name="ide_movimentacao_financeira", updatable=false, unique=true, nullable=false)
	private Integer ideMovimentacaoFinanceira;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="dtc_operacao", nullable=false)
	private Date dtcOperacao;
	
	@Column(name="val_operacao", nullable=false, precision=20, scale=2)
	private BigDecimal valOperacao;
	
	@Column(name="num_resolucao", nullable=true, length=100)
	private String numResolucao;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_tcca", nullable=false)
	private Tcca ideTcca;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_produto_execucao")
	private ProdutoExecucao ideProdutoExecucao;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_pessoa_fisica_operacao", nullable=false)
	private PessoaFisica idePessoaFisicaOperacao;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_tcca_historico_reajuste_valor")
	private TccaHistoricoReajusteValor ideTccaHistoricoReajusteValor;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_tcca_projeto_operacao", nullable=false)
	private TccaProjetoOperacao ideTccaProjetoOperacao;
	
	@Transient
	@OneToOne(mappedBy="ideMovimentacaoFinanceira")
	private MovimentacaoFinanceiraTccaProduto ideMovimentacaoFinanceiraTccaProduto;
	
	@OneToOne(mappedBy="ideMovimentacaoFinanceira")
	private ProdutoSaldo ideProdutoSaldo;
	
	@OneToOne(mappedBy="ideMovimentacaoFinanceira")
	private TccaSaldo ideTccaSaldo;
	
	public MovimentacaoFinanceira() {
	}
	
	public Integer getIdeMovimentacaoFinanceira() {
		return ideMovimentacaoFinanceira;
	}

	public void setIdeMovimentacaoFinanceira(Integer ideMovimentacaoFinanceira) {
		this.ideMovimentacaoFinanceira = ideMovimentacaoFinanceira;
	}

	public Date getDtcOperacao() {
		return dtcOperacao;
	}

	public void setDtcOperacao(Date dtcOperacao) {
		this.dtcOperacao = dtcOperacao;
	}

	public BigDecimal getValOperacao() {
		return valOperacao;
	}

	public void setValOperacao(BigDecimal valOperacao) {
		this.valOperacao = valOperacao;
	}

	public String getNumResolucao() {
		return numResolucao;
	}

	public void setNumResolucao(String numResolucao) {
		this.numResolucao = numResolucao;
	}

	public Tcca getIdeTcca() {
		return ideTcca;
	}

	public void setIdeTcca(Tcca ideTcca) {
		this.ideTcca = ideTcca;
	}

	public ProdutoExecucao getIdeProdutoExecucao() {
		return ideProdutoExecucao;
	}

	public void setIdeProdutoExecucao(ProdutoExecucao ideProdutoExecucao) {
		this.ideProdutoExecucao = ideProdutoExecucao;
	}

	public PessoaFisica getIdePessoaFisicaOperacao() {
		return idePessoaFisicaOperacao;
	}

	public void setIdePessoaFisicaOperacao(PessoaFisica idePessoaFisicaOperacao) {
		this.idePessoaFisicaOperacao = idePessoaFisicaOperacao;
	}

	public TccaHistoricoReajusteValor getIdeTccaHistoricoReajusteValor() {
		return ideTccaHistoricoReajusteValor;
	}

	public void setIdeTccaHistoricoReajusteValor(TccaHistoricoReajusteValor ideTccaHistoricoReajusteValor) {
		this.ideTccaHistoricoReajusteValor = ideTccaHistoricoReajusteValor;
	}

	public TccaProjetoOperacao getIdeTccaProjetoOperacao() {
		return ideTccaProjetoOperacao;
	}

	public void setIdeTccaProjetoOperacao(TccaProjetoOperacao ideTccaProjetoOperacao) {
		this.ideTccaProjetoOperacao = ideTccaProjetoOperacao;
	}

	public MovimentacaoFinanceiraTccaProduto getIdeMovimentacaoFinanceiraTccaProduto() {
		return ideMovimentacaoFinanceiraTccaProduto;
	}

	public void setIdeMovimentacaoFinanceiraTccaProduto(MovimentacaoFinanceiraTccaProduto ideMovimentacaoFinanceiraTccaProduto) {
		this.ideMovimentacaoFinanceiraTccaProduto = ideMovimentacaoFinanceiraTccaProduto;
	}

	public ProdutoSaldo getIdeProdutoSaldo() {
		return ideProdutoSaldo;
	}

	public void setIdeProdutoSaldo(ProdutoSaldo ideProdutoSaldo) {
		this.ideProdutoSaldo = ideProdutoSaldo;
	}

	public TccaSaldo getIdeTccaSaldo() {
		return ideTccaSaldo;
	}

	public void setIdeTccaSaldo(TccaSaldo ideTccaSaldo) {
		this.ideTccaSaldo = ideTccaSaldo;
	}
}