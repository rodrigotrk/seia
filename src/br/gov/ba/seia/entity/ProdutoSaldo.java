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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.ba.seia.entity.generic.AbstractEntity;

@Entity
@Table(name="produto_saldo")
@NamedQuery(name="ProdutoSaldo.findAll", query="SELECT p FROM ProdutoSaldo p")
public class ProdutoSaldo extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="PRODUTO_SALDO_IDEPRODUTOSALDO_GENERATOR", sequenceName="PRODUTO_SALDO_IDE_PRODUTO_SALDO_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PRODUTO_SALDO_IDEPRODUTOSALDO_GENERATOR")
	@Column(name="ide_produto_saldo", updatable=false, unique=true, nullable=false)
	private Integer ideProdutoSaldo;

	@Column(name="val_saldo_produto", nullable=false, precision=20, scale=2)
	private BigDecimal valSaldoProduto;
	
	@Column(name="val_saldo_remanejado", nullable=false, precision=20, scale=2)
	private BigDecimal valSaldoRemanejado;
	
	@Column(name="val_saldo_suplementado", nullable=false, precision=20, scale=2)
	private BigDecimal valSaldoSuplementado;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_movimentacao_financeira", nullable=false)
	private MovimentacaoFinanceira ideMovimentacaoFinanceira;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_projeto_acao_produto", nullable=false)
	private ProjetoAcaoProduto ideProjetoAcaoProduto;

	public ProdutoSaldo() {
	}

	public Integer getIdeProdutoSaldo() {
		return this.ideProdutoSaldo;
	}

	public void setIdeProdutoSaldo(Integer ideProdutoSaldo) {
		this.ideProdutoSaldo = ideProdutoSaldo;
	}
	
	public BigDecimal getValSaldoProduto() {
		return this.valSaldoProduto;
	}

	public void setValSaldoProduto(BigDecimal valSaldoProduto) {
		this.valSaldoProduto = valSaldoProduto;
	}

	public MovimentacaoFinanceira getIdeMovimentacaoFinanceira() {
		return this.ideMovimentacaoFinanceira;
	}

	public void setIdeMovimentacaoFinanceira(MovimentacaoFinanceira ideMovimentacaoFinanceira) {
		this.ideMovimentacaoFinanceira = ideMovimentacaoFinanceira;
	}

	public ProjetoAcaoProduto getIdeProjetoAcaoProduto() {
		return this.ideProjetoAcaoProduto;
	}

	public void setIdeProjetoAcaoProduto(ProjetoAcaoProduto ideProjetoAcaoProduto) {
		this.ideProjetoAcaoProduto = ideProjetoAcaoProduto;
	}

	public BigDecimal getValSaldoRemanejado() {
		return valSaldoRemanejado;
	}

	public void setValSaldoRemanejado(BigDecimal valSaldoRemanejado) {
		this.valSaldoRemanejado = valSaldoRemanejado;
	}

	public BigDecimal getValSaldoSuplementado() {
		return valSaldoSuplementado;
	}

	public void setValSaldoSuplementado(BigDecimal valSaldoSuplementado) {
		this.valSaldoSuplementado = valSaldoSuplementado;
	}
}