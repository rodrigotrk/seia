package br.gov.ba.seia.entity;

import java.io.Serializable;

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

@Entity
@Table(name="movimentacao_financeira_tcca_produto")
@NamedQuery(name="MovimentacaoFinanceiraTccaProduto.findAll", query="SELECT m FROM MovimentacaoFinanceiraTccaProduto m")
public class MovimentacaoFinanceiraTccaProduto implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="MOVIMENTACAO_FINANCEIRA_TCCA_PRODUTO_IDEMOVIMENTACAOFINANCEIRATCCAPRODUTO_GENERATOR", sequenceName="MOVIMENTACAO_FINANCEIRA_TCCA_PRODUTO_IDE_MOVIMENTACAO_FINANCEIRA_TCCA_PRODUTO_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="MOVIMENTACAO_FINANCEIRA_TCCA_PRODUTO_IDEMOVIMENTACAOFINANCEIRATCCAPRODUTO_GENERATOR")
	@Column(name="ide_movimentacao_financeira_tcca_produto", updatable=false, unique=true, nullable=false)
	private Integer ideMovimentacaoFinanceiraTccaProduto;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_movimentacao_financeira", nullable=false)
	private MovimentacaoFinanceira ideMovimentacaoFinanceira;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_projeto_acao_produto_destino")
	private ProjetoAcaoProduto ideProjetoAcaoProdutoDestino;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_projeto_acao_produto_origem")
	private ProjetoAcaoProduto ideProjetoAcaoProdutoOrigem;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_tcca_destino")
	private Tcca ideTccaDestino;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_tcca_origem")
	private Tcca ideTccaOrigem;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_tipo_destino")
	private TipoOrigemDestino ideTipoDestino;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_tipo_origem")
	private TipoOrigemDestino ideTipoOrigem;

	public MovimentacaoFinanceiraTccaProduto() {
	}

	public Integer getIdeMovimentacaoFinanceiraTccaProduto() {
		return this.ideMovimentacaoFinanceiraTccaProduto;
	}

	public void setIdeMovimentacaoFinanceiraTccaProduto(Integer ideMovimentacaoFinanceiraTccaProduto) {
		this.ideMovimentacaoFinanceiraTccaProduto = ideMovimentacaoFinanceiraTccaProduto;
	}

	public MovimentacaoFinanceira getIdeMovimentacaoFinanceira() {
		return this.ideMovimentacaoFinanceira;
	}

	public void setIdeMovimentacaoFinanceira(MovimentacaoFinanceira ideMovimentacaoFinanceira) {
		this.ideMovimentacaoFinanceira = ideMovimentacaoFinanceira;
	}

	public ProjetoAcaoProduto getIdeProjetoAcaoProdutoDestino() {
		return this.ideProjetoAcaoProdutoDestino;
	}

	public void setIdeProjetoAcaoProdutoDestino(ProjetoAcaoProduto ideProjetoAcaoProdutoDestino) {
		this.ideProjetoAcaoProdutoDestino = ideProjetoAcaoProdutoDestino;
	}

	public ProjetoAcaoProduto getIdeProjetoAcaoProdutoOrigem() {
		return this.ideProjetoAcaoProdutoOrigem;
	}

	public void setIdeProjetoAcaoProdutoOrigem(ProjetoAcaoProduto ideProjetoAcaoProdutoOrigem) {
		this.ideProjetoAcaoProdutoOrigem = ideProjetoAcaoProdutoOrigem;
	}

	public Tcca getIdeTccaDestino() {
		return this.ideTccaDestino;
	}

	public void setIdeTccaDestino(Tcca ideTccaDestino) {
		this.ideTccaDestino = ideTccaDestino;
	}

	public Tcca getIdeTccaOrigem() {
		return this.ideTccaOrigem;
	}

	public void setIdeTccaOrigem(Tcca ideTccaOrigem) {
		this.ideTccaOrigem = ideTccaOrigem;
	}

	public TipoOrigemDestino getIdeTipoDestino() {
		return this.ideTipoDestino;
	}

	public void setIdeTipoDestino(TipoOrigemDestino ideTipoDestino) {
		this.ideTipoDestino = ideTipoDestino;
	}

	public TipoOrigemDestino getIdeTipoOrigem() {
		return this.ideTipoOrigem;
	}

	public void setIdeTipoOrigem(TipoOrigemDestino ideTipoOrigem) {
		this.ideTipoOrigem = ideTipoOrigem;
	}
}