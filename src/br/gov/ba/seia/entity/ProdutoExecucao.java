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
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.ba.seia.entity.generic.AbstractEntity;

@Entity
@Table(name="produto_execucao")
@NamedQuery(name="ProdutoExecucao.findAll", query="SELECT p FROM ProdutoExecucao p")
public class ProdutoExecucao extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="PRODUTO_EXECUCAO_IDEPRODUTOEXECUCAO_GENERATOR", sequenceName="PRODUTO_EXECUCAO_IDE_PRODUTO_EXECUCAO_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PRODUTO_EXECUCAO_IDEPRODUTOEXECUCAO_GENERATOR")
	@Column(name="ide_produto_execucao", updatable=false, unique=true, nullable=false)
	private Integer ideProdutoExecucao;

	@Column(name="val_contratado", precision=20, scale=2)
	private BigDecimal valContratado;

	@Column(name="val_executado", precision=20, scale=2)
	private BigDecimal valExecutado;

	@Column(name="val_previsto", nullable=false, precision=20, scale=2)
	private BigDecimal valPrevisto;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_projeto_acao_produto", referencedColumnName= "ide_projeto_acao_produto" )
	private ProjetoAcaoProduto ideProjetoAcaoProduto;
	
	@OneToMany(mappedBy="ideProdutoExecucao")
	private List<MovimentacaoFinanceira> movimentacaoFinanceiraCollection;

	public ProdutoExecucao() {
	}

	public ProdutoExecucao(ProjetoAcaoProduto ideProjetoAcaoProduto) {
		this.ideProjetoAcaoProduto = ideProjetoAcaoProduto;
	}

	public Integer getIdeProdutoExecucao() {
		return this.ideProdutoExecucao;
	}

	public void setIdeProdutoExecucao(Integer ideProdutoExecucao) {
		this.ideProdutoExecucao = ideProdutoExecucao;
	}

	public BigDecimal getValContratado() {
		return this.valContratado;
	}

	public void setValContratado(BigDecimal valContratado) {
		this.valContratado = valContratado;
	}

	public BigDecimal getValExecutado() {
		return this.valExecutado;
	}

	public void setValExecutado(BigDecimal valExecutado) {
		this.valExecutado = valExecutado;
	}

	public BigDecimal getValPrevisto() {
		return this.valPrevisto;
	}

	public void setValPrevisto(BigDecimal valPrevisto) {
		this.valPrevisto = valPrevisto;
	}

	public List<MovimentacaoFinanceira> getMovimentacaoFinanceiraCollection() {
		return this.movimentacaoFinanceiraCollection;
	}

	public void setMovimentacaoFinanceiraCollection(List<MovimentacaoFinanceira> movimentacaoFinanceiraCollection) {
		this.movimentacaoFinanceiraCollection = movimentacaoFinanceiraCollection;
	}

	public ProjetoAcaoProduto getIdeProjetoAcaoProduto() {
		return this.ideProjetoAcaoProduto;
	}

	public void setIdeProjetoAcaoProduto(ProjetoAcaoProduto ideProjetoAcaoProduto) {
		this.ideProjetoAcaoProduto = ideProjetoAcaoProduto;
	}
}