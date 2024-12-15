package br.gov.ba.seia.entity;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.gov.ba.seia.entity.generic.AbstractEntity;

@Entity
@Table(name="projeto_acao_produto")
@NamedQuery(name="ProjetoAcaoProduto.findAll", query="SELECT p FROM ProjetoAcaoProduto p")
public class ProjetoAcaoProduto extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="PROJETO_ACAO_PRODUTO_IDEPROJETOACAOPRODUTO_GENERATOR", sequenceName="PROJETO_ACAO_PRODUTO_IDE_PROJETO_ACAO_PRODUTO_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PROJETO_ACAO_PRODUTO_IDEPROJETOACAOPRODUTO_GENERATOR")
	@Column(name="ide_projeto_acao_produto", updatable=false, unique=true, nullable=false)
	private Integer ideProjetoAcaoProduto;

	@Column(name="ind_excluido", nullable=false)
	private Boolean indExcluido;

	@Column(name="nom_produto", nullable=false, length=200)
	private String nomProduto;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_projeto_acao", nullable=false)
	private ProjetoAcao ideProjetoAcao;
	
	@OneToMany(mappedBy="ideProjetoAcaoProdutoDestino")
	private List<MovimentacaoFinanceiraTccaProduto> movimentacaoFinanceiraTccaProdutoDestinoCollection;
	
	@OneToMany(mappedBy="ideProjetoAcaoProdutoOrigem")
	private List<MovimentacaoFinanceiraTccaProduto> movimentacaoFinanceiraTccaProdutoOrigemCollection;
	
	@OneToOne(mappedBy="ideProjetoAcaoProduto", fetch = FetchType.LAZY)
	private ProdutoExecucao ideProdutoExecucao;
	
	@OneToMany(mappedBy="ideProjetoAcaoProduto")
	private List<ProdutoSaldo> produtoSaldoCollection;
	
	@Transient
	private BigDecimal valorPrevisto;

	@Transient
	private BigDecimal valorContratado;

	@Transient
	private BigDecimal valorExecutado;
	
	@Transient
	private BigDecimal valorRemanejado;
	
	@Transient
	private BigDecimal valorSuplementado;
	
	@Transient
	private ProdutoSaldo ultimoProdutoSaldo;
	
	public ProjetoAcaoProduto() {
	}

	public Integer getIdeProjetoAcaoProduto() {
		return this.ideProjetoAcaoProduto;
	}

	public void setIdeProjetoAcaoProduto(Integer ideProjetoAcaoProduto) {
		this.ideProjetoAcaoProduto = ideProjetoAcaoProduto;
	}

	public Boolean getIndExcluido() {
		return this.indExcluido;
	}

	public void setIndExcluido(Boolean indExcluido) {
		this.indExcluido = indExcluido;
	}

	public String getNomProduto() {
		return this.nomProduto;
	}

	public void setNomProduto(String nomProduto) {
		this.nomProduto = nomProduto;
	}

	public List<MovimentacaoFinanceiraTccaProduto> getMovimentacaoFinanceiraTccaProdutoDestinoCollection() {
		return this.movimentacaoFinanceiraTccaProdutoDestinoCollection;
	}

	public void setMovimentacaoFinanceiraTccaProdutoDestinoCollection(List<MovimentacaoFinanceiraTccaProduto> movimentacaoFinanceiraTccaProdutoDestinoCollection) {
		this.movimentacaoFinanceiraTccaProdutoDestinoCollection = movimentacaoFinanceiraTccaProdutoDestinoCollection;
	}

	public List<MovimentacaoFinanceiraTccaProduto> getMovimentacaoFinanceiraTccaProdutoOrigemCollection() {
		return this.movimentacaoFinanceiraTccaProdutoOrigemCollection;
	}

	public void setMovimentacaoFinanceiraTccaProdutoOrigemCollection(List<MovimentacaoFinanceiraTccaProduto> movimentacaoFinanceiraTccaProdutoOrigemCollection) {
		this.movimentacaoFinanceiraTccaProdutoOrigemCollection = movimentacaoFinanceiraTccaProdutoOrigemCollection;
	}

	public ProdutoExecucao getIdeProdutoExecucao() {
		return ideProdutoExecucao;
	}

	public void setIdeProdutoExecucao(ProdutoExecucao ideProdutoExecucao) {
		this.ideProdutoExecucao = ideProdutoExecucao;
	}

	public List<ProdutoSaldo> getProdutoSaldoCollection() {
		return this.produtoSaldoCollection;
	}

	public void setProdutoSaldoCollection(List<ProdutoSaldo> produtoSaldoCollection) {
		this.produtoSaldoCollection = produtoSaldoCollection;
	}

	public ProjetoAcao getIdeProjetoAcao() {
		return this.ideProjetoAcao;
	}

	public void setIdeProjetoAcao(ProjetoAcao ideProjetoAcao) {
		this.ideProjetoAcao = ideProjetoAcao;
	}

	public BigDecimal getValorPrevisto() {
		return valorPrevisto;
	}

	public void setValorPrevisto(BigDecimal valorPrevisto) {
		this.valorPrevisto = valorPrevisto;
	}

	public BigDecimal getValorContratado() {
		return valorContratado;
	}

	public void setValorContratado(BigDecimal valorContratado) {
		this.valorContratado = valorContratado;
	}

	public BigDecimal getValorExecutado() {
		return valorExecutado;
	}

	public void setValorExecutado(BigDecimal valorExecutado) {
		this.valorExecutado = valorExecutado;
	}

	public ProdutoSaldo getUltimoProdutoSaldo() {
		return ultimoProdutoSaldo;
	}

	public void setUltimoProdutoSaldo(ProdutoSaldo ultimoProdutoSaldo) {
		this.ultimoProdutoSaldo = ultimoProdutoSaldo;
	}

	public BigDecimal getValorRemanejado() {
		return valorRemanejado;
	}

	public void setValorRemanejado(BigDecimal valorRemanejado) {
		this.valorRemanejado = valorRemanejado;
	}

	public BigDecimal getValorSuplementado() {
		return valorSuplementado;
	}

	public void setValorSuplementado(BigDecimal valorSuplementado) {
		this.valorSuplementado = valorSuplementado;
	}
}